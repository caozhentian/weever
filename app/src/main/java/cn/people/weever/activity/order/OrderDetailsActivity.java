package cn.people.weever.activity.order;

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

public class OrderDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;

    public static final Intent newIntent(Context context) {
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        return intent;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mTvTitle.setText("日租");
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
