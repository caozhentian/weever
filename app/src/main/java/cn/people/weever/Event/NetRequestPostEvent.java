package cn.people.weever.Event;

/**
 * Created by Administrator on 2017/6/6.
 */

public class NetRequestPostEvent {
    private int mApiOperationCode ;

    public NetRequestPostEvent(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }

    public int getApiOperationCode() {
        return mApiOperationCode;
    }

    public void setApiOperationCode(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }
}
