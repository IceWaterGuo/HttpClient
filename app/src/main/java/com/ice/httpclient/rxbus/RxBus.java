package com.ice.httpclient.rxbus;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.jakewharton.rxrelay3.PublishRelay;
import com.jakewharton.rxrelay3.Relay;
import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Desc:事件总线
 * Created by icewater on 2024/01/03.
 */
public class RxBus {
    private volatile static RxBus mDefaultInstance;
    private Relay<Object> mBus = null;
    private final Map<Class<?>, Object> mStickyEventMap;

    /*private void RxBus(){}

    public static RxBus getInstance(){
        return rxBusInstance;
    }

    static class RxBusInner{
        static RxBus rxBusInstance = new RxBus();
    }*/

    //禁用构造方法
    private RxBus() {
        mBus = PublishRelay.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

//    public static RxBus get() {
//        return Holder.BUS;
//    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.accept(event);
    }

    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        mBus.accept(event);
    }

    /**
     * 根据传递的eventType类型返回特定类型(eventType)的被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 使用Rxlifecycle解决RxJava引起的内存泄漏
     */
    public <T> Observable<T> toObservable(LifecycleOwner owner, Class<T> eventType) {
        LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(owner);
        return mBus.ofType(eventType).compose(provider.<T>bindToLifecycle());
    }

    /**
     * 根据传递的eventType类型返回特定类型(eventType)的被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                        e.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * 使用Rxlifecycle解决RxJava引起的内存泄漏（自动取消订阅）
     */
    public <T> Observable<T> toObservableSticky(LifecycleOwner owner, final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(owner);
            Observable<T> observable = mBus.ofType(eventType).compose(provider.<T>bindToLifecycle());
            final Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        emitter.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Scheduler scheduler, Consumer<T> onNext) {
        return toObservable(owner, eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError,
                                   Action onComplete) {
        return toObservable(owner, eventType).observeOn(scheduler).subscribe(onNext, onError, onComplete);
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError) {
        return toObservable(owner, eventType).observeOn(scheduler).subscribe(onNext, onError);
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Consumer<T> onNext) {
        return toObservable(owner, eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Consumer<T> onNext, Consumer onError,
                                   Action onComplete) {
        return toObservable(owner, eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete);
    }

    public <T> Disposable register(LifecycleOwner owner, Class<T> eventType, Consumer<T> onNext, Consumer onError) {
        return toObservable(owner, eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    public <T> Disposable registerSticky(LifecycleOwner owner, Class<T> eventType, Scheduler scheduler, Consumer<T> onNext) {
        return toObservableSticky(owner, eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable registerSticky(LifecycleOwner owner, Class<T> eventType, Consumer<T> onNext) {
        return toObservableSticky(owner, eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> Disposable registerSticky(LifecycleOwner owner, Class<T> eventType, Consumer<T> onNext, Consumer onError) {
        return toObservableSticky(owner, eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,onError);
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }

    /**
     * 解绑所有订阅（防止内存泄漏）
     */
    public void unregister(CompositeDisposable disposable) {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.clear();
        }
    }

    public void reset() {
        mDefaultInstance = null;
    }

//    private static class Holder {
//        private static final RxBus BUS = new RxBus();
//    }

}
