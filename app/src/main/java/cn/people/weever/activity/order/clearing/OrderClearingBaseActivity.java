package cn.people.weever.activity.order.clearing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.car.TrackQueryActivity;
import cn.people.weever.model.BaseOrder;

public abstract  class OrderClearingBaseActivity extends BaseActivity {


    @BindView(R.id.src)
    TextView src;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.spn_settlement_first)
    Spinner spnSettlementFirst;
    @BindView(R.id.spn_settlement_second)
    Spinner spnSettlementSecond;
    @BindView(R.id.btn_compute)
    Button btnCompute;
    @BindView(R.id.btn_trace)
    Button btnTrace;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;

    public static final Intent newIntent(Context context, BaseOrder baseOrder) {
        Intent intent = new Intent(context, OrderClearingBaseActivity.class);
        intent.putExtra(BaseOrder.ORDER_ID, baseOrder);
        return intent;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mTvTitle.setText("结算");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_clearing);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @OnClick({R.id.btn_compute, R.id.btn_trace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_compute:
                break;
            case R.id.btn_trace:
                startActivity(TrackQueryActivity.newIntent(this));
                break;
        }
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }


    abstract  protected <T extends BaseOrder> T queryOrderDetails(BaseOrder order)  ;

    protected void  submitOrder(){

    }
}
