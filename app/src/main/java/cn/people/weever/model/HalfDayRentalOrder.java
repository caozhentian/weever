package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

/**半日租订单
 * Created by Administrator on 2017/6/3 0003.
 */

public class HalfDayRentalOrder extends BaseOrder {

    //  半日租费用
    @SerializedName("halfDayRentalCost")
    private int mHalfDayRentalCost ;

    public int getHalfDayRentalCost() {
        return mHalfDayRentalCost;
    }

    public void setHalfDayRentalCost(int halfDayRentalCost) {
        mHalfDayRentalCost = halfDayRentalCost;
    }
}
