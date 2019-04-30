package zity.net.basecommonmodule.net.listener;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:38
 * describe：
 */


public abstract class HttpDownOnNextListener<T> {
    /**
     * 成功回调方法
     *
     * @param t
     */

    public abstract void onNext(T t);

    public abstract void onStart();

    public abstract void onComplete();

    /**
     * 下载进度
     */
    public abstract void updateProgress(long readLength, long countLength);

    public void onError(Throwable e) {

    }

    public void onPuase() {

    }

    public void onStop() {
    }


}
