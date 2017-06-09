package cn.people.weever.activity.order.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.model.BaseOrder;

public class OrderDetailsBaseActivity extends BaseActivity {

    private static final String ARG_ORDER_BASE = "order_base" ;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;

    private BaseOrder mBaseOrder ;

    public static final Intent newIntent(Context context , BaseOrder baseOrder) {
        Intent intent = new Intent(context, OrderDetailsBaseActivity.class)  ;
        intent.putExtra(ARG_ORDER_BASE , baseOrder)                     ;
        return intent ;
    }

    @Override
    public void initData() {
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_ORDER_BASE) ;
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
}
