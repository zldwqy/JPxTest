#### 2.ViewModel 定义、用法和源码分析
##### 定义：ViewModel类是被设计用来以可感知生命周期的方式存储和管理UI相关数据，ViewModel中数据会一直存活即使活动配置发生变化，比如横竖屏切换的时候。
- 数据存储，尤其屏幕旋转时能保存数据不被销毁
- Fragment之间传递数据
- 感知生命周期变化，防止内存泄露


##### viewmodel 生命周期
viewmode的生命周期从Activity onCreate 到“真正”的Finish，这个真正代表activity在一些意外情况下不会重新创建，比如屏幕旋转导致的activiy重新创建。这里避免了我们在onSaveInstanceState 和 onRestoreInstanceState 中进行数据保存和恢复。至于原因，可以从源码中得到。
![image](https://developer.android.google.cn/images/topic/libraries/architecture/viewmodel-lifecycle.png)


##### 用法：
1.  继承ViewModel 内部的属性一般使用LiveData
2.  使用ViewModelProviders.of(this).get(ViewModel.class)创建对象
3.  在Activity或Fragment 之中添加观察者。
```

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<User>> users;
    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<User>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}
//创建不带参数的Viewmodel 
public class MyActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.

        MyViewModel model = ViewModelProviders.of(this).get(MyViewModel.class);
        model.getUsers().observe(this, users -> {
            // update UI
        });
    }
}

//创建带参数的ViewModel
//首先创建一个工厂类对象
//接着调用ViewModelProviders.of(this, factory) 创建对象
   /**
     * 工厂类
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mProductId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(mApplication, mRepository, mProductId);
        }
    }
@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //先创建工厂类对象，把参数传递到工厂类的对象中
        ProductViewModel.Factory factory = new ProductViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_PRODUCT_ID));
        //创建viewmodel对象
        final ProductViewModel model = ViewModelProviders.of(this, factory)
                .get(ProductViewModel.class);

        mBinding.setProductViewModel(model);

        subscribeToModel(model);
    }


```
###### 在Fragment 之间共享数据

```
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }
}


public class MasterFragment extends Fragment {
    private SharedViewModel model;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //传入的是activity 对象，因为Fragment返回的是同一对象
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
    }
}

public class DetailFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, { item ->
           // Update the UI.
        });
    }
}

```
##### 使用viewmodel、Room和LiveData加载数据

![image](https://developer.android.google.cn/images/topic/libraries/architecture/viewmodel-replace-loader.png)


##### 源码分析：
###### Viewmodel 如何便面内存泄露的
###### 在Activity 的onDestorty 中 ViewModelStore 调用clear 方法，回调所有存储的viewmodel的oncleared() 方法，viewmodel 可以重写此方法清理相关对象，防止内存泄露，之后调用map.clear清除所有存储的viewmodel对象。
```
androidx.fragment.app.FragmentActivity
    /**
     * Destroy all fragments.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity或Fragment 销毁时会判断是否为配置参数改变（如屏幕旋转）
        if (mViewModelStore != null && !isChangingConfigurations()) {
            //调用viewmodel 的clear清理内部的对象，防止内存泄露
            mViewModelStore.clear();
        }

        mFragments.dispatchDestroy();
    }


public class ViewModelStore {
    //用于存储所有的viewmodel对象
    private final HashMap<String, ViewModel> mMap = new HashMap<>();

    final void put(String key, ViewModel viewModel) {
        ViewModel oldViewModel = mMap.put(key, viewModel);
        if (oldViewModel != null) {
            oldViewModel.onCleared();
        }
    }

    final ViewModel get(String key) {
        return mMap.get(key);
    }

    /**
     *  Clears internal storage and notifies ViewModels that they are no longer used.
     */
    public final void clear() {
        for (ViewModel vm : mMap.values()) {
            vm.onCleared();
        }
        mMap.clear();
    }
}

public abstract class ViewModel {
    /**
     * 重写此方法，把相关属性置空防止内存泄露
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * <p>
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    @SuppressWarnings("WeakerAccess")
    protected void onCleared() {
    }
}
```


