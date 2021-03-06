package cn.people.weever.activity.order.clearing;

import android.support.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;

/**
 * Created by Administrator on 2017/6/9.
 */

public class FixedTimeOrderClearingActivity extends OrderClearingBaseActivity {

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processOrderEvent(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TYPE_FixTime_DETAILS_NET_REQUST){
            mBaseOrder = (BaseOrder) baseModel.getData();
            setViewByBaseOrder() ;
        }
    }
}
