package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

/** 接送机订单
 * Created by Administrator on 2017/4/8.
 */

public class TransferOrder extends BaseOrder{

    //接送机费用
    @SerializedName("transferCost")
    private int mTransferCost           ;
    @SerializedName("flightNumber")
    private String mFlightNumber         ;

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        mFlightNumber = flightNumber;
    }

    public int getTransferCost() {
        return mTransferCost;
    }

    public void setTransferCost(int transferCost) {
        mTransferCost = transferCost;
    }
}
