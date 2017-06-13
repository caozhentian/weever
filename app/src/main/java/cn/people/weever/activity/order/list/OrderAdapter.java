package cn.people.weever.activity.order.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.people.weever.R;
import cn.people.weever.adapter.BaseListAdapter;
import cn.people.weever.model.BaseOrder;

/**
 * Created by Administrator on 2017/5/29 0029.
 */

public class OrderAdapter extends BaseListAdapter<BaseOrder> {


    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_passenger)
    TextView mTvPassenger;
    @BindView(R.id.tv_start_end_time)
    TextView mTvStartEndTime;
    @BindView(R.id.txt_order_status)
    TextView mTxtOrderStatus;

    protected OrderAdapter(Context context, List<BaseOrder> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.item_order, null);
            holderView = new HolderView(v);
            v.setTag(holderView);
        } else {
            holderView = (HolderView) v.getTag();
        }
        BaseOrder baseOrder = mList.get(position) ;
        mTvType.setText(BaseOrder.getTypeStr(baseOrder.getType()));
        mTvPassenger.setText(baseOrder.getSubscribePerson());
        mTvStartEndTime.setText(baseOrder.getStartEndTimeStr());
        mTxtOrderStatus.setText(BaseOrder.getStatusStr(baseOrder.getStatus()));
        return v;
    }

    private class HolderView {
        public HolderView(View view) {
            ButterKnife.bind(this, view);
        }
    }
}