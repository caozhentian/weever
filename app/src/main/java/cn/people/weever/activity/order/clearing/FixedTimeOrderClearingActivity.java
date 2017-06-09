package cn.people.weever.activity.order.clearing;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.FixTimeOrder;

/**
 * Created by Administrator on 2017/6/9.
 */

public class FixedTimeOrderClearingActivity extends OrderClearingBaseActivity {
    @Override
    protected FixTimeOrder queryOrderDetails(BaseOrder order) {
        return null;
    }
}
