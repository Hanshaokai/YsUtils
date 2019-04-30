package zity.net.basecommonmodule.commonbase;

/**
 * author：hanshaokai
 * date： 2018/5/30 14:33
 * describe：
 */


public class BaseResponse {
    /**
     * 服务器是否运行正常
     */
    protected Boolean success;
    /**
     * 最新的api版本
     */
    protected int api_version;
    /**
     * 标识
     */
    protected int code;
    /**
     * 分页的总共条数
     */
    protected int total;
    /**
     * 反馈给用户信息
     */
    protected String user_msg;
    /**
     * 服务器信息
     */
    protected String server_msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getApi_version() {
        return api_version;
    }

    public void setApi_version(int api_version) {
        this.api_version = api_version;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getServer_msg() {
        return server_msg;
    }

    public void setServer_msg(String server_msg) {
        this.server_msg = server_msg;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", api_version=" + api_version +
                ", code=" + code +
                ", total=" + total +
                ", user_msg='" + user_msg + '\'' +
                ", server_msg='" + server_msg + '\'' +
                '}';
    }
}
