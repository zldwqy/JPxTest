#### 1.Lifecycle
定义：用于感知Activity、Fragment生命周期的组件

用法：
- 1.继承LifecycleObserver，通过注解的方式设置回调方法。
- 2.在Activity或Fragment中添加观察者
```
public class MyObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        ...
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        ...
    }
}

myLifecycleOwner.getLifecycle().addObserver(new MyObserver());
```
源码解析：

```
public abstract class Lifecycle {
    //添加观察者
    public abstract void addObserver(@NonNull LifecycleObserver observer);
    //移除观察者
    public abstract void removeObserver(@NonNull LifecycleObserver observer);
}
//子类实现了这个相关方法,在Activity、Fragment中有对象
public class LifecycleRegistry extends Lifecycle {
    
    
    @Override
    public void addObserver(@NonNull LifecycleObserver observer) {
        State initialState = mState == DESTROYED ? DESTROYED : INITIALIZED;
        ObserverWithState statefulObserver = new ObserverWithState(observer, initialState);
        ObserverWithState previous = mObserverMap.putIfAbsent(observer, statefulObserver);

        if (previous != null) {
            return;
        }
        //判断组件对象是否存在不存在则不添加观察者
        //lifecycleOwner即是Activity或Fragment
        LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            // it is null we should be destroyed. Fallback quickly
            return;
        }

        boolean isReentrance = mAddingObserverCounter != 0 || mHandlingEvent;
        State targetState = calculateTargetState(observer);
        mAddingObserverCounter++;
        while ((statefulObserver.mState.compareTo(targetState) < 0
                && mObserverMap.contains(observer))) {
            pushParentState(statefulObserver.mState);
            statefulObserver.dispatchEvent(lifecycleOwner, upEvent(statefulObserver.mState));
            popParentState();
            // mState / subling may have been changed recalculate
            targetState = calculateTargetState(observer);
        }

        if (!isReentrance) {
            // we do sync only on the top level.
            sync();
        }
        mAddingObserverCounter--;
    }
}
```