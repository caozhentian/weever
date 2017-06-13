package cn.people.weever.activity.order.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.service.OrderService;

public class OrderDetailsBaseActivity extends BaseActivity {

    private static final String ARG_ORDER_BASE = "order_base";

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_top)
    LinearLayout mLayoutTop;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_plant_start_date)
    TextView mTvPlantStartDate;
    @BindView(R.id.tv_plant_start_address)
    TextView mTvPlantStartAddress;
    @BindView(R.id.tv_actual_start_date)
    TextView mTvActualStartDate;
    @BindView(R.id.tv_actual_start_address)
    TextView mTvActualStartAddress;
    @BindView(R.id.tv_actual_end_address)
    TextView mTvActualEndAddress;
    @BindView(R.id.tv_actual_all_cost)
    TextView mTvActualAllCost;
    @BindView(R.id.tv_expire_time_cose)
    TextView mTvExpireTimeCose;
    @BindView(R.id.tv_expire_distance_cose)
    TextView mTvExpireDistanceCose;
    @BindView(R.id.btn_take)
    Button mBtnTake;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_settlent)
    Button mBtnSettlent;

    private BaseOrder mBaseOrder;

    private OrderService mOrderService ;
    public static final Intent newIntent(Context context, BaseOrder baseOrder) {
        Intent intent = new Intent(context, OrderDetailsBaseActivity.class);
        intent.putExtra(ARG_ORDER_BASE, baseOrder);
        return intent;
    }

    @Override
    public void initData() {
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_ORDER_BASE);
    }

    @Override
    public void initView() {
        mTvTitle.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.btn_take, R.id.btn_start, R.id.btn_settlent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take:
                break;
            case R.id.btn_start:
                break;
            case R.id.btn_settlent:
                break;
        }
    }
}
