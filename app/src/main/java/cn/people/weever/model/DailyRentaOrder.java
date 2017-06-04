package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

/**日租订单
 * Created by Administrator on 2017/6/3 0003.
 */

public class DailyRentaOrder extends BaseOrder {

    //  日租费用
    @SerializedName("dailyRentalCost")
    protected int mDailyRentalCost ;

    public int getDailyRentalCost() {
        return mDailyRentalCost;
    }

    public void setDailyRentalCost(int dailyRentalCost) {
        mDailyRentalCost = dailyRentalCost;
    }
}
