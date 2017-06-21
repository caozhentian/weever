package cn.people.weever.activity.order.detail;

import cn.people.weever.model.TransferOrder;

public class TransferOrderDetailsActivity extends OrderDetailsBaseActivity {

    protected void setOtherDetailInfo() {
        if (mBaseOrder instanceof TransferOrder) {
            TransferOrder transferOrder = (TransferOrder) mBaseOrder;
            mTvRentalCose.setText(transferOrder.getTransferCost()+"");
        }
    }
}
