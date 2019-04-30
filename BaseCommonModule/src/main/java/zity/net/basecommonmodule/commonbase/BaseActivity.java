package zity.net.basecommonmodule.commonbase;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.SPUtils;
import zity.net.basecommonmodule.utils.ToastUtils;
import zity.net.basecommonmodule.widgets.MyProgressDialog;


/**
 * author：hanshaokai
 * date： 2018/5/30 19:03
 * describe： 为了和城管项目兼容 不继承 RxAppCompatActivity
 */


public abstract class BaseActivity extends RxAppCompatActivity {
    protected BaseActivity mBaseActivity;
    private Unbinder mBind;
    private BaseEmptyView mBaseEmptyView;
    private Dialog mMyProgressDialog;
    protected int mUserId;
    protected int totalRows;
    protected int mOrgId;
    protected String tokenValue;
    // protected ModelExtendDWebView mWebView;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mUserId = SPUtils.getInstance().getInt(Constants.USER_ID_KEY);
        mOrgId = SPUtils.getInstance().getInt(Constants.ORG_ID_KEY);
        mBaseActivity = BaseActivity.this;
        setContentView(getLayoutId());
        mBind = ButterKnife.bind(this);
        //初始化异常界面
        mBaseEmptyView = new BaseEmptyView(this);
        mBaseEmptyView.setOnReloadingListener(new BaseEmptyView.OnReloadingListener() {
            @Override
            public void onReloading() {
                onReloadData();
            }
        });
        if (!BaseActivity.this.getClass().getSimpleName().contains("WelcomeActivity")
        ) {
            //  StatusBarUtil.StatusBarLightMode(this, getResources().getColor(R.color.bg_blue));// 在华为上起作用在三星上不起作用 用来设置黑色图标
            //   StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.bg_blue));
        }

        tokenValue = SPUtils.getInstance().getString(Constants.APP_TOKEN_KEY);
        doViewLogic(savedInstanceState);
    }

    protected void toastS(String msg) {
        ToastUtils.showShort(msg);
    }
    protected void toastL(String msg) {
        ToastUtils.showLong(msg);
    }

    protected void addDisposable(Disposable mDis) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDis);
    }

    /**
     * 返回视图id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 重新加载时调用
     */
    protected void onReloadData() {
    }

    /**
     * 执行视图变更
     *
     * @param savedInstanceState
     */
    protected abstract void doViewLogic(@Nullable Bundle savedInstanceState);

    @Override
    protected void onPause() {
       /* if (mWebView != null) {
            mWebView.onPause();
        }*/
        super.onPause();
    }

    @Override
    protected void onResume() {
       /* if (mWebView != null) {
            mWebView.onResume();
        }
        MobclickAgent.onResume(this);*/
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mBind.unbind();
      /*  if (mWebView != null) {
            mWebView.destroy();
        }*/
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    /**
     * 展示空列表或者异常界面
     *
     * @param target 界面布局放在 activity 该view处
     * @param status 对应不同效果
     */
    protected void showEmptyView(@NonNull View target, int status) {
        if (mBaseEmptyView == null) {
            return;
        }
        mBaseEmptyView.showResultView(target, status);
    }


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
     * 加载进度动画
     */
    public void showLoadDialog(String msg) {
        if (mMyProgressDialog != null) {
            if (!mMyProgressDialog.isShowing())
                mMyProgressDialog.show();
        } else {
            mMyProgressDialog = MyProgressDialog.getInstance().createLoadingDialog(mBaseActivity, msg);
        }
    }

    public void hideLoadDialog() {
        if (mMyProgressDialog != null && mMyProgressDialog.isShowing()) {
            mMyProgressDialog.cancel();
        }
    }


}
