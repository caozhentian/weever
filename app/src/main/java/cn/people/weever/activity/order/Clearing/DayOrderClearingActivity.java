package cn.people.weever.activity.order.Clearing;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.DailyRentaOrder;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DayOrderClearingActivity extends OrderClearingBaseActivity {
    @Override
    protected DailyRentaOrder queryOrderDetails(BaseOrder order) {
        return null;
    }
}
