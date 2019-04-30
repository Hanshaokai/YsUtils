package zity.net.basecommonmodule;


import java.io.File;

import zity.net.basecommonmodule.app.BaseCommonApp;
import zity.net.basecommonmodule.utils.SDCardUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:58
 * describe：
 */


public class Constants {
    /**
     * 路径传输值key
     */
    public static final String WEB_URL = "web_url";
    public static final String PLEASE_WATIE = "请稍等...";
    public static final String ON_COMMITING = "提交中...";
    /**
     * 刷新key
     */
    public static final String REFRESH = "刷新";
    /**
     * 文件路径key
     */
    public static final String FILE_PATH_KEY = "file_path";
    public static final String USER_ROLE_KEY = "userRoleKey";
    public static final String RONG_YUN_TOKEN_KEY = "rytoken";


    /**
     * 请求根地址
     */
    public static String BASE_URL = BuildConfig.BaseURL;
    /**
     * 加载列表提示语
     */
    public static final String LIST_LODE_ALERT_MSG = "加载中...";
    public static final String LOAD_MSG_FILE = "下载文件中...";
    public static final String COMMIT_LOADING = "正在提交...";
    public static final String COMMIT_FAILE = "提交失败";
    public static final String COMMIT_SUCCESS = "提交成功";
    /**
     * 请求过程中的异常码即页面展示状态码  网络未连接  网络请求异常 服务器内部错误
     */
    public static final int ERRO_NET_NOT_CONNECT = 4001;
    public static final int REQUEST_EXCEPTION = 4003;
    public static final int SEVER_ERROR = 4004;
    // 表示 是工作日的界面
    public static final int NOT_WORK_STATUS = 5000;
    /**
     * 请求成功后的状态码  （REQUEST_SUCCESS 与 CODE_ON_CORRECT 含义等同）正常请求状态码 token值不一致状态 数据版本不支持状态
     * 登录失败返回码
     * 修改失败返回码
     */
    public static final int REQUEST_SUCCESS = 2000;
    public static final int TOKEN_ERROR_CODE = 1001;
    public static final int API_VERSION_NOT_SUPPOSED_CODE = 1002;
    //以下统归 操作失败
    public static final int LOGIN_ERROR = 1003;
    public static final int PASSWORD_CORRECT_ERROR = 2001;
    /**
     * 请求数据情况 页面展示状态码  内容为空 没有联系人
     */
    public static final int NO_CONTENT = 4002;
    public static final int NO_CONTACT = 5003;
    /**
     * 登录请求信息存储key值
     */
    public static final String APP_TOKEN_KEY = "token_key";
    public static final String ORG_ID_KEY = "organization_id";
    public static final String ORG_NAME = "organization_name";
    public static final String USER_ID_KEY = "user_id";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_PHONE_KEY = "user_phone";
    public static final String USER_ACCOUNT_KEY = "user_account";
    public static final String USER_ROLE = "user_role";
    public static final String IS_LOGIN = "isLogin";
    // 解析url 获取flag值 标识当前页面的特殊显示方式
    public static final String INSTRUCKION_KEY = "flag";
    /**
     * 轮播图key值
     */
    public static final String BANNER_WEB_URL_KEY = "banner_web_url_key";
    //下载文件存储地址
    public static String FILE_PATH_DOWNLOAD = SDCardUtils.getSDCardPathByEnvironment() + File.separator + BaseCommonApp.flavorName + File.separator;
    public static String FILE_PATH_TEMP = SDCardUtils.getSDCardPathByEnvironment() + File.separator + BaseCommonApp.flavorName + File.separator + "temp" + File.separator;
    //public static String APK_DOWN_PATH = SDCardUtils.getSDCardPathByEnvironment() + File.separator + BaseCommonApp.flavorName + File.separator + "temp";

    //上传照片缓存处
    public static String FILE_PATH_PIC = SDCardUtils.getSDCardPathByEnvironment() + File.separator + BaseCommonApp.flavorName + File.separator + "camera" + File.separator;
    public static final int PAGE_SIZE = 5000;
}
