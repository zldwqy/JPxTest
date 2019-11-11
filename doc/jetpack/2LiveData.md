#### 2.LiveData 定义、用法和源码分析

##### LiveData 几个重要的方法


```
1.observe(LifecycleOwner owner, Observer<T> observer)
  添加观察者
2. observeForever(Observer<T> observer)
  添加永久观察者，于方法1不同的是，需要手动移除，否则会引起内存泄漏
3.removeObserver(Observer<T> observer)
4.removeObservers(LifecycleOwner owner)
5.onActive() 
  当活动观察者的数量从0变为1时调用。
6.onInactive()
  当活动观察者的数量从1变为0时调用
7.setValue(T value) 
  设置值，必须在主线程调用
8.postValue(T value)
  与7不同，可以在非主线程调用。
```

##### LiveData 两个重要的子类

```
1.MutableLiveData 

public class MutableLiveData<T> extends LiveData<T> {
    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }
}

这个类使用最多，它之重写了LiveData 两个方法，将两个方法变成public 
```


```
2.MediatorLiveData

public class MediatorLiveData<T> extends MutableLiveData<T> 

	addSource(LiveData<S> source, Observer<S> onChanged)
	removeSource(LiveData<S> toRemote)
	
这个类是MutableLiveData的子类，增加了两个方法，使换个类可以担任LiveData集合的作用，统一管理所有的LiveData.
```


##### 使用
```
1.添加观察者
        // Observe comments
        model.getComments().observe(this, new Observer<List<CommentEntity>>() {
            @Override
            public void onChanged(@Nullable List<CommentEntity> commentEntities) {
                if (commentEntities != null) {
                    mBinding.setIsLoading(false);
                    mCommentAdapter.setCommentList(commentEntities);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
        
    
```


```
2.和Room 搭配使用
可以作用查询结果的返回值
@Query("SELECT * FROM comments where productId = :productId")
LiveData<List<CommentEntity>> loadComments(int productId);
```

```
2.自定义LiveData
public class NetworkLiveData extends LiveData<NetworkInfo> {

    private final Context mContext;
    static NetworkLiveData mNetworkLiveData;
    private NetworkReceiver mNetworkReceiver;
    private final IntentFilter mIntentFilter;

    private static final String TAG = "NetworkLiveData";

    public NetworkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetworkLiveData getInstance(Context context) {
        if (mNetworkLiveData == null) {
            mNetworkLiveData = new NetworkLiveData(context);
        }
        return mNetworkLiveData;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive:");
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive: ");
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            getInstance(context).setValue(activeNetwork);

        }
    }
}
```

##### 源码分析：

```
添加观察者
1.在添加之前首先判断owner的状态，这个owner指Activity或Fragment，如果owner已经destory,则不再添加
2.对观察者进行包装，然后存储在mObservers 这个map 对象中。
3.最后把观察者再添加到owner的观察者中
    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        assertMainThread("observe");
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
        //在这添加观察者
        ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        owner.getLifecycle().addObserver(wrapper);
    }
    
    
对象发生变化之后，通过dispatchingValue 通知变化
    @MainThread
    protected void setValue(T value) {
        assertMainThread("setValue");
        mVersion++;
        mData = value;
        dispatchingValue(null);
    }
    
    private void dispatchingValue(@Nullable LifecycleBoundObserver initiator) {
        if (mDispatchingValue) {
            mDispatchInvalidated = true;
            return;
        }
        mDispatchingValue = true;
        do {
            mDispatchInvalidated = false;
            if (initiator != null) {
                considerNotify(initiator);
                initiator = null;
            } else {
            //这里循环遍历所有观察者，一一通知
                for (Iterator<Map.Entry<Observer<T>, LifecycleBoundObserver>> iterator =
                        mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
                    //通知前判断
                    considerNotify(iterator.next().getValue());
                    if (mDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (mDispatchInvalidated);
        mDispatchingValue = false;
    }
    
    private void considerNotify(LifecycleBoundObserver observer) {
        if (!observer.active) {
            return;
        }
        // Check latest state b4 dispatch. Maybe it changed state but we didn't get the event yet.
        //
        // we still first check observer.active to keep it as the entrance for events. So even if
        // the observer moved to an active state, if we've not received that event, we better not
        // notify for a more predictable notification order.
        //判断owner 是否处于STARTED 之后的状态
        if (!isActiveState(observer.owner.getLifecycle().getCurrentState())) {
            return;
        }
        if (observer.lastVersion >= mVersion) {
            return;
        }
        observer.lastVersion = mVersion;
        //noinspection unchecked
        observer.observer.onChanged((T) mData);
    }

```