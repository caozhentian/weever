package cn.people.weever.activity.order.clearing;

import android.widget.TextView;

import butterknife.BindView;
import cn.people.weever.R;
import cn.people.weever.model.DailyRentaOrder;
import cn.people.weever.model.TransferOrder;

/**
 * Created by Administrator on 2017/6/9.
 */

public class PickupOrderClearingActivity extends OrderClearingBaseActivity {

    @BindView(R.id.tv_transfer_cost)
    TextView mTvTransferCost;

    @Override
    public void setViewByBaseOrder() {
        super.setViewByBaseOrder();
        mTvTransferCost.setText(((TransferOrder)mBaseOrder).getTransferCost()+"") ;
    }
}
