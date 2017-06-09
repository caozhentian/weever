package cn.people.weever.activity.order.clearing;

import cn.people.weever.model.BaseOrder;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DayHalfOrderClearingActivity extends OrderClearingBaseActivity {
    @Override
    protected <T extends BaseOrder> T queryOrderDetails(BaseOrder order) {
        return null;
    }
}
