package cn.people.weever.activity.order.clearing;

import android.support.annotation.Nullable;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.people.weever.R;
import cn.people.weever.model.HalfDayRentalOrder;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DayHalfOrderClearingActivity extends OrderClearingBaseActivity {

    @BindView(R.id.tv_half_day_cost)
    TextView mTvHalfDayCost;

    @Override
    public void setViewByBaseOrder() {
        super.setViewByBaseOrder();
        mTvHalfDayCost.setText(((HalfDayRentalOrder)mBaseOrder).getHalfDayRentalCost() + "") ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processOrderEvent(@Nullable BaseModel<HalfDayRentalOrder> baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TYPE_DAYHALF_DETAILS_NET_REQUST){
            mBaseOrder =  baseModel.getData() ;
            setViewByBaseOrder() ;
        }
    }
}
