package zity.net.basecommonmodule.net.listener;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:39
 * describe：
 */


public interface DownLoadProgressListener {
    /**
     * 下载进度
     */
    void update(long read, long count, boolean done);
}
