package cn.people.weever.Event;

/**
 * Created by Administrator on 2017/6/6.
 */

public class NetRequestPreEvent {
    private int mApiOperationCode ;

    public NetRequestPreEvent(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }

    public int getApiOperationCode() {
        return mApiOperationCode;
    }

    public void setApiOperationCode(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }
}
