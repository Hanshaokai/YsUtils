package zity.net.basecommonmodule.commonbase;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.SPUtils;
import zity.net.basecommonmodule.widgets.MyProgressDialog;

/**
 * author：hanshaokai
 * date： 2018/5/30 19:26
 * describe：
 */


public abstract class BaseFragment extends RxFragment {
    protected int userId;
    protected AppCompatActivity mBaseActivity;
    private Unbinder mBind;
    private BaseEmptyView mBaseEmptyView;
    //控件是否已经初始化
    private boolean isCreateView = false;
    private Bundle bundle;
    //是否已经加载过数据
    private boolean isLoadData = false;
    private Dialog mMyProgressDialog;
    protected int mOrgId;
    protected String tokenValue;
    //protected ModelExtendDWebView mWebView;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected String mUserName;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreateView = false;
        isLoadData = false;
       /* if (mWebView != null) {
            mWebView.destroy();
        }*/
        mBind.unbind();
        mCompositeDisposable.clear();
    }

    /**
     * 展示空列表或者异常界面
     *
     * @param target 界面布局放在 fragment 该view处  如显示列表时  放置 recyclerView
     * @param status 对应不同效果
     */
    protected void showEmptyView(@NonNull View target, int status) {
        if (mBaseEmptyView == null) {
            return;
        }
        mBaseEmptyView.showResultView(target, status);
    }

    /**
     * @param target
     * @param status
     * @param alertMsg
     */
    protected void showEmptyView(@NonNull View target, int status, String alertMsg) {
        if (mBaseEmptyView == null) {
            return;
        }
        mBaseEmptyView.showResultView(target, status, alertMsg);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView && !isLoadData) {
            //加载数据操作
            isLoadData = true;
            doViewLogic(bundle);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mBaseActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container);
        mBind = ButterKnife.bind(this, view);
        //初始化异常界面
        mBaseEmptyView = new BaseEmptyView(mBaseActivity, this);
        mBaseEmptyView.setOnReloadingListener(new BaseEmptyView.OnReloadingListener() {
            @Override
            public void onReloading() {
                onReloadData();
            }
        });
        isCreateView = true;
        bundle = savedInstanceState;
        userId = SPUtils.getInstance().getInt(Constants.USER_ID_KEY);
        mUserName = SPUtils.getInstance().getString(Constants.USER_NAME_KEY);
        mOrgId = SPUtils.getInstance().getInt(Constants.ORG_ID_KEY);
        tokenValue = SPUtils.getInstance().getString(Constants.APP_TOKEN_KEY);
        return view;
    }

    @Override
    public void onPause() {
        /*if (mWebView != null)
            mWebView.onPause();*/
        super.onPause();
    }

    @Override
    public void onResume() {
       /* if (mWebView != null)
            mWebView.onResume();*/
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //非懒加载fragment不走setUserVisibleHint 走这里 保证可以加载数据
        //懒加载fragment 在之前方法setUserVisibleHint 中已经加载好
        if (!isLoadData && getUserVisibleHint()) {
            doViewLogic(bundle);
            isLoadData = true;
        }
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 重新加载时调用
     */
    protected void onReloadData() {
    }

    protected abstract void doViewLogic(Bundle savedInstanceState);

    /**
     * 展示加载动画页面
     *
     * @param target 动画在布局中该view处展示
     * @param status
     */
    protected void showLoadingView(@NonNull View target, int status) {
        if (mBaseEmptyView == null) {
            return;
        }
        mBaseEmptyView.showLoadingView(target, status);
    }

    protected void showDataView() {
        if (mBaseEmptyView == null) {
            return;
        }
        mBaseEmptyView.showTargetView();
    }

    /**
     * 加载进度动画 在同一界面多个请求重用，导致 msg显示不能改变
     */
    public void showLoadDialog(String msg) {
        if (mMyProgressDialog == null) {
            mMyProgressDialog = MyProgressDialog.getInstance().createLoadingDialog(mBaseActivity, msg);
        } else {
            if (!mMyProgressDialog.isShowing()) {
                mMyProgressDialog.show();
            }
        }
    }

    public void hideLoadDialog() {
        if (mMyProgressDialog != null && mMyProgressDialog.isShowing()) {
            mMyProgressDialog.cancel();
        }
    }
}
