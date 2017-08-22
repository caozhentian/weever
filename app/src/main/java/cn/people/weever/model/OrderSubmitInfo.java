package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 司机提交订单的信息
 * Created by Administrator on 2017/6/2.
 */

public class OrderSubmitInfo {

    //订单ID
    @SerializedName("orderId")
    private String mOrderId ;
    @SerializedName("amountList")
    private List<Amount> mAmountList ;

	//会员信息
	@SerializedName("company")
	private Company mCompany ;
    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public List<Amount> getAmountList() {
        return mAmountList;
    }

    public void setAmountList(List<Amount> amountList) {
        mAmountList = amountList;
    }
}
