package zity.net.basecommonmodule.net.listener;


import io.reactivex.Observable;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:36
 * describe：
 */


public abstract class HttpOnNextListener<T> {

    /**
     * 成功后回调方法
     *
     * @param t
     */
    protected abstract void onNext(T t);

    /**
     * 緩存回調結果
     *
     * @param string
     */
    protected void onCacheNext(String string) {

    }

    /**
     * 成功后的ober返回，扩展链接式调用
     *
     * @param observable
     */
    protected void onNext(Observable observable) {

    }

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     *
     * @param e
     */
    protected void onError(Throwable e) {

    }

    /**
     * 取消回調
     */
    protected void onCancel() {

    }


}
