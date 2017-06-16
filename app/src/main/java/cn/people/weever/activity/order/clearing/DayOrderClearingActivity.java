package cn.people.weever.activity.order.clearing;

import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import cn.people.weever.R;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.DailyRentaOrder;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;

/**
 * Created by Administrator on 2017/6/9.
 */

public class DayOrderClearingActivity extends OrderClearingBaseActivity {

    @BindView(R.id.tv_day_cost)
    TextView mTvDayCost;

    @Override
    public void setViewByBaseOrder() {
        super.setViewByBaseOrder();
        mTvDayCost.setText(((DailyRentaOrder)mBaseOrder).getDailyRentalCost()+"") ;
    }


    public void processOrderEvent(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TYPE_DAY_DETAILS_NET_REQUST){
            mBaseOrder = (BaseOrder) baseModel.getData();
            setViewByBaseOrder() ;
        }
    }

}
