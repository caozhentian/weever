package cn.people.weever.activity.order.detail;

import cn.people.weever.model.HalfDayRentalOrder;

public class DayHalfOrderDetailsActivity extends OrderDetailsBaseActivity {

    @Override
    public void initView() {
        super.initView();
    }

    protected void setOtherDetailInfo() {
        if (mBaseOrder instanceof HalfDayRentalOrder) {
            HalfDayRentalOrder halfDayRentalOrder = (HalfDayRentalOrder) mBaseOrder;
            mTvRentalCose.setText(halfDayRentalOrder.getHalfDayRentalCost()+"");
        }
    }
}
