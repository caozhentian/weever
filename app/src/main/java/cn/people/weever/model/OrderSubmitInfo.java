package cn.people.weever.model;

import java.util.List;

/**
 * 司机提交订单的信息
 * Created by Administrator on 2017/6/2.
 */

public class OrderSubmitInfo {

    //订单ID
    private String orderId ;

    private List<Amount> mAmountList ;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Amount> getAmountList() {
        return mAmountList;
    }

    public void setAmountList(List<Amount> amountList) {
        mAmountList = amountList;
    }
}
