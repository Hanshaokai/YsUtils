package zity.net.basecommonmodule.commonbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.R;
import zity.net.basecommonmodule.app.BaseCommonApp;
import zity.net.basecommonmodule.utils.LogUtils;

/**
 * hanshaokai
 * 2018/5/28
 * describe
 */


public class BaseEmptyView implements View.OnClickListener {
    private View emptyView;
    private View loadingView;
    private View emptyTargetView;
    private ImageView emptyImage;
    private TextView emptyMsg;
    private Context mContext;

    private OnReloadingListener mOnReloadingListener;
    private Dialog mLoadingDialog;
    private TextView mLoadingMsg;
    private Fragment mFragment;

    private static HashMap<Integer, String> warMap = new HashMap<Integer, String>();
    private static HashMap<Integer, Drawable> drawableMap = new HashMap<Integer, Drawable>();
    private FrameLayout.LayoutParams mLayoutParams;
    private String mAlertMsg;

    public BaseEmptyView(Context context) {
        initView(context);
    }

    static {
        warMap.put(Constants.ERRO_NET_NOT_CONNECT, "网络连接断开");
        drawableMap.put(Constants.ERRO_NET_NOT_CONNECT, ContextCompat.getDrawable(BaseCommonApp.getAppClication(), R.drawable.ic_no_content));
        warMap.put(Constants.NO_CONTENT, "暂无数据");
        drawableMap.put(Constants.NO_CONTENT, ContextCompat.getDrawable(BaseCommonApp.getAppClication(), R.drawable.ic_no_content));
        warMap.put(Constants.REQUEST_EXCEPTION, "请求异常");
        drawableMap.put(Constants.REQUEST_EXCEPTION, ContextCompat.getDrawable(BaseCommonApp.getAppClication(), R.drawable.ic_no_content));
        warMap.put(Constants.NO_CONTACT, "没有相关联系人");
        drawableMap.put(Constants.NO_CONTACT, ContextCompat.getDrawable(BaseCommonApp.getAppClication(), R.drawable.ic_no_content));

        warMap.put(Constants.NOT_WORK_STATUS, "");
        drawableMap.put(Constants.NOT_WORK_STATUS, ContextCompat.getDrawable(BaseCommonApp.getAppClication(),R.drawable.ic_no_content));
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutInflater mLayoutInflater;
        mLayoutInflater = LayoutInflater.from(context);
        //初始化空白界面 可显示为 空内容界面  以及 异常界面
        emptyView = mLayoutInflater.inflate(R.layout.model_base_empty_view, null);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReloadingListener.onReloading();
            }
        });
        emptyView.setLayoutParams(mLayoutParams);
        emptyView.setId(R.id.base_empty_view);
        emptyImage = emptyView.findViewById(R.id.iv_empty_image);
        emptyMsg = emptyView.findViewById(R.id.tv_empty_msg);
        //初始化 加载动画页面
        loadingView = mLayoutInflater.inflate(R.layout.model_base_empty_loading_view, null);
        // 加载提示文字
        mLoadingMsg = loadingView.findViewById(R.id.tv_empty_loading_msg);
        loadingView.setId(R.id.base_loading_view);
    }

    public BaseEmptyView(Context context, Fragment fragment) {
        mFragment = fragment;
        initView(context);
    }

    /**
     * dialog 方式显示加载动画
     * @param context
     * @param msg
     * @return
     */
   /* public Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View loadingView = inflater.inflate(R.layout.base_empty_loading_view, null);// 得到加载view
        LinearLayout layout = loadingView
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = loadingView.findViewById(R.id.tv_empty_loading_msg);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        // 创建自定义样式dialog
        mLoadingDialog = new Dialog(context, R.style.loadingDialogStyle);
        // 是否可以按“返回键”消失
        mLoadingDialog.setCancelable(true);
        // 点击加载框以外的区域
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        *//**
     *将显示Dialog的方法封装在这里面
     *//*
        Window window = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        mLoadingDialog.show();
        return mLoadingDialog;
    }
*/

    /**
     * 显示异常视图（emptyView）
     *
     * @param target 想替换掉的部分，想在RecyclerView的部分显示异常，就传RecyclerView
     * @param status 服务器返回的状态码，根据不同状态码显示的错误信息不同
     */
    public void showResultView(View target, int status) {
        LogUtils.d(warMap.toString() + "错误码" + status);
        showResultView(target, status, warMap);
    }

    /**
     * @param target
     * @param status
     * @param alertMsg 提示语
     */
    public void showResultView(View target, int status, String alertMsg) {
        LogUtils.d(warMap.toString() + "错误码" + status);
        mAlertMsg = alertMsg;
        showResultView(target, status, warMap);
    }

    /**
     * 显示异常视图（emptyView）
     *
     * @param target 想替换掉的部分，想在RecyclerView的部分显示异常，就传RecyclerView
     * @param status 服务器返回的状态码，根据不同状态码显示的错误信息不同
     * @param msgs   自定义的错误信息，比如有的信息为空异常要显示成“该XX下没有XX”，有的要显示“您还没有XX”，自定义更方便
     */
    public void showResultView(View target, int status, Map<Integer, String> msgs) {
        //隐藏进度条
        showTargetView();
        if (emptyTargetView == null) {
            emptyTargetView = target;
            emptyTargetView.setId(R.id.base_target_view);
        }
        //找到target的父视图
        ViewGroup parent = (ViewGroup) target.getParent();
        View tempTips = parent.findViewById(R.id.base_empty_view);
        if (tempTips == null) {
            //给父布局上加一个emptyView，即异常显示视图
            parent.addView(emptyView);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
        // TODO: 2018/5/28  设置对应状态图

        emptyImage.setImageDrawable(drawableMap.get(status));
        //getErrorMsg(status,errorCode)根据状态码status和自定义的错误集，来得到错误信息
        if (TextUtils.isEmpty(mAlertMsg)) {
            emptyMsg.setText(msgs.get(status));
        } else {
            emptyMsg.setText(mAlertMsg);
        }
        //隐藏原布局，如果传进来的是RecyclerView，那么RecyclerView就会被隐藏，emptyView会被显示出来
        target.setVisibility(View.GONE);
    }

    /**
     * 隐藏加载动画和异常界面  显示出目标视图
     */
    public void showTargetView() {
        if (mFragment != null && mFragment.getView() != null) {
            // 从布局文件中找id  避免与布局中的id 重复
            if (mFragment.getView().findViewById(R.id.base_loading_view) != null) {
                LogUtils.d("隐藏掉了loadingView");
                mFragment.getView().findViewById(R.id.base_loading_view).setVisibility(View.GONE);
            }
        } else {
            if (((Activity) mContext).findViewById(R.id.base_loading_view) != null) {
                ((Activity) mContext).findViewById(R.id.base_loading_view).setVisibility(View.GONE);
            }
        }
        hideExceptionView();
    }

    /**
     * 隐藏异常视图emptyView
     */
    private void hideExceptionView() {
        if (mFragment != null && mFragment.getView() != null) {
            if (mFragment.getView().findViewById(R.id.base_empty_view) != null) {
                mFragment.getView().findViewById(R.id.base_empty_view).setVisibility(View.GONE);
            }
            if (mFragment.getView().findViewById(R.id.base_target_view) != null) {
                mFragment.getView().findViewById(R.id.base_target_view).setVisibility(View.VISIBLE);
            }
        } else {
            if (((Activity) mContext).findViewById(R.id.base_empty_view) != null) {
                ((Activity) mContext).findViewById(R.id.base_empty_view).setVisibility(View.GONE);
            }
            if (((Activity) mContext).findViewById(R.id.base_target_view) != null) {
                ((Activity) mContext).findViewById(R.id.base_target_view).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 根据不同加载界面显示不同 提示语
     *
     * @param target 放置位置
     * @param status 状态值
     */
    public void showLoadingView(View target, int status) {
        showLoadingView(target, status, new HashMap<Integer, String>());
    }

    /**
     * 显示一个进度条，此进度条会屏蔽当前界面，此时用户无法操作界面   ((Activity) mContext).addContentView(loadingView, params);
     */

    private void showLoadingView(View loadTargetView, int status, Map<Integer, String> msgs) {
        hideExceptionView();
        ViewGroup parent = (ViewGroup) loadTargetView.getParent();
        if (parent.findViewById(R.id.base_loading_view) == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            parent.addView(loadingView, params);
        }
        loadingView.setVisibility(View.VISIBLE);
        mLoadingMsg.setText("加载中...");
    }

    public void setOnReloadingListener(OnReloadingListener onReloadingListener) {
        this.mOnReloadingListener = onReloadingListener;
    }

    public interface OnReloadingListener {
        void onReloading();
    }

    @Override
    public void onClick(View v) {
        if (mOnReloadingListener != null) {
            mOnReloadingListener.onReloading();
        }
    }


}
