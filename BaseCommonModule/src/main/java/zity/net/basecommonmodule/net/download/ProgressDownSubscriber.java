package zity.net.basecommonmodule.net.download;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;
import zity.net.basecommonmodule.net.RxManager;
import zity.net.basecommonmodule.net.db.GreenDaoManager;
import zity.net.basecommonmodule.net.listener.DownLoadProgressListener;
import zity.net.basecommonmodule.net.listener.HttpDownOnNextListener;
import zity.net.basecommonmodule.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 16:10
 * describe：
 */


public class ProgressDownSubscriber<T> extends DisposableObserver<T> implements DownLoadProgressListener {

    //弱引用结果回调
    private SoftReference<HttpDownOnNextListener> mSubscriberOnNextListener;
    //下载数据
    private DownInfo downInfo;

    public ProgressDownSubscriber(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }

    public void setDownInfo(DownInfo downInfo) {
        this.mSubscriberOnNextListener = new SoftReference<HttpDownOnNextListener>(downInfo.getListener());
        this.downInfo = downInfo;
    }

    /**
     * 订阅开始时调用 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onStart();
        }
        downInfo.setState(DownStateEnum.START);
    }


    /**
     * @param read
     * @param count
     * @param done
     */
    @Override
    public void update(long read, long count, final boolean done) {
        if (downInfo.getCountLength() > count) {
            read = downInfo.getCountLength() - count + read;
        } else {
            downInfo.setCountLength(count);
        }
        downInfo.setReadLength(read);
        if (mSubscriberOnNextListener.get() != null) {
            /*接受进度消息造成UI阻塞如果不需要显示进度可去掉实现逻辑减少压力*/
            Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<Long>() {
                        @Override
                        public void onNext(Long aLong) {
                            {
                                /*如果暂停或停止状态延迟，不需要继续发送回调，影响显示*/
                                if (downInfo.getState() == DownStateEnum.PAUSE
                                        || downInfo.getState() == DownStateEnum.STOP) {
                                    return;
                                }
                                LogUtils.d("获取数据" + aLong);
                                downInfo.setState(DownStateEnum.DOWN);
                                mSubscriberOnNextListener.get().updateProgress(aLong, downInfo.getCountLength());
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */

    @Override
    public void onError(Throwable e) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(e);
        }
        RxManager.getInstance().remove(downInfo);
        downInfo.setState(DownStateEnum.ERROR);
        GreenDaoManager.getInstance().update(downInfo);
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onComplete();
        }
        RxManager.getInstance().remove(downInfo);
        downInfo.setState(DownStateEnum.ERROR);
        GreenDaoManager.getInstance().update(downInfo);
    }

    /**
     * 将onNext 方法中的返回结果交给activity 或Fragment 自己处理
     *
     * @param t 创建Subscriber 时的泛型类型
     */

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }
}
