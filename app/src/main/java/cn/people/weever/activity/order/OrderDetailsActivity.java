package cn.people.weever.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.people.weever.R;

public class OrderDetailsActivity extends AppCompatActivity {

    public static final Intent newIntent(Context context){
        Intent   intent  = new Intent(context ,OrderDetailsActivity.class ) ;
        return  intent  ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
    }
}
