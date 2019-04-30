/*
package zity.net.basecommonmodule.widgets;

import android.content.Context;
import android.util.AttributeSet;

import wendu.dsbridge.DWebView;


*/
/**
 * author：hanshaokai
 * date： 2018/8/16 0016 14:42
 * describe：监听webview滚动，setOnScrollChangeListener 这个方法要api ＝ 23 才可以用.
 * swiperefreshLayout 官方文档里是建议 如果要子布局上滑手势生效是重写swiperefreshLayout
 * 的 boolean canChildScrollUp ()方法。返回true 就是子布局手势，false 就是自己使用。
 * 和手势拦截原则是一致了，viewGroup 优先取到手势，决定是不是传给子布局。没有用官方推荐的重写boolean canChildScrollUp ()
 * 方式，因为webview 是否滚到顶部识别不出来，也要用重写回调的方式就不适合再用该方式了。
 *//*


public class ModelExtendDWebView extends DWebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public ModelExtendDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModelExtendDWebView(Context context) {
        super(context);
    }

 */
/*  @Override
    protected void tbs_onScrollChanged(int l, int t, int oldl, int oldt, View view) {
        this.super_onScrollChanged(l, t, oldl, oldt);
        //X5WebView 父类屏蔽了 onScrollChanged 方法 要用该方法
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }*//*


    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //普通webview用这个
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int l, int t);
    }

}
*/
