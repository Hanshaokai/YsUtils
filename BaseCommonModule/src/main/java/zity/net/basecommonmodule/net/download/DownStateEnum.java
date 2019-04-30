package zity.net.basecommonmodule.net.download;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:57
 * describe：
 */


public enum DownStateEnum {
    START(0),
    DOWN(1),
    PAUSE(2),
    STOP(3),
    ERROR(4),
    FINISH(5);
    private int state;

    DownStateEnum(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
