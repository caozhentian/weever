package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

/**
 * 订单实时状态信息
 * Created by Administrator on 2017/6/20.
 */

public class RealTimeOrderInfo {

    @SerializedName("waitTime")
    private String mWaitTime   ;
    @SerializedName("waitCost")
    private String mWaitCost   ;

    public String getWaitTime() {
        return mWaitTime;
    }

    public void setWaitTime(String waitTime) {
        mWaitTime = waitTime;
    }

    public String getWaitCost() {
        return mWaitCost;
    }

    public void setWaitCost(String waitCost) {
        mWaitCost = waitCost;
    }


}
