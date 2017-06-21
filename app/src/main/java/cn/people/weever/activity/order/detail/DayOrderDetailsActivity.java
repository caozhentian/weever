package cn.people.weever.activity.order.detail;

import cn.people.weever.model.DailyRentaOrder;

public class DayOrderDetailsActivity extends OrderDetailsBaseActivity {

    @Override
    public void initView() {
        super.initView();
    }

    protected void setOtherDetailInfo(){
        if(mBaseOrder instanceof DailyRentaOrder) {
            DailyRentaOrder dailyRentaOrder = (DailyRentaOrder) mBaseOrder;
            mTvRentalCose.setText(dailyRentaOrder.getDailyRentalCost()+"" );
        }

    }
}
