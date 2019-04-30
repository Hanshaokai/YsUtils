package zity.net.basecommonmodule.widgets;

import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import zity.net.basecommonmodule.R;


/**
 * @date ：2018/1/26
 * @author： seitlc
 * @describe: 自定义dialog
 */

public class MyProgressDialog {
    private static MyProgressDialog instance;
    private Dialog loadingDialog;

    public static synchronized MyProgressDialog getInstance() {
        if (instance == null) {
            instance = new MyProgressDialog();
        }
        return instance;
    }

    public Dialog createLoadingDialog(Context context, String msg) {
        if (((AppCompatActivity) context).isFinishing()) {
            return null;
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.model_base_empty_loading_view, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_empty_loading_msg);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        // 创建自定义样式dialog
        loadingDialog = new Dialog(context, R.style.loadingDialogStyle);
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }


    /**
     * 关闭dialog
     */


}
