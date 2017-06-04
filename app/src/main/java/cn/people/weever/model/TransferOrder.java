package cn.people.weever.model;

/** 接送机订单
 * Created by Administrator on 2017/4/8.
 */

public class TransferOrder {

    //接送机费用
    private int mTransferCost           ;

    private String mFlightNumber         ;

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        mFlightNumber = flightNumber;
    }
}
