package cn.people.weever.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.model.BaseOrder;

public class OrderClearingActivity extends BaseActivity {

    public static final Intent newIntent(Context context , BaseOrder baseOrder){
        Intent intent = new Intent() ;
        intent.putExtra(BaseOrder.ORDER_ID ,baseOrder ) ;
        return intent ;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_clearing);
        initView();
        initData();
    }
}
