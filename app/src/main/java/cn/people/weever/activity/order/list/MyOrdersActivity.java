package cn.people.weever.activity.order.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.order.detail.OrderDetailsBaseActivity;
import cn.people.weever.event.OrderStatusChangeEvent;
import cn.people.weever.model.BaseOrder;


public class MyOrdersActivity extends BaseActivity {

    private ImageView    img_back ;
    private TextView     tv_title ;

    public static final Intent newIntent(Context packageContext){
        Intent intent    =  new  Intent(packageContext , MyOrdersActivity.class) ;
        return intent   ;
    }

    public static final Intent newIntent(Context packageContext  , BaseOrder baseOrder){
        Intent intent    =  new  Intent(packageContext , MyOrdersActivity.class) ;
        intent.putExtra("key" , baseOrder);
        return intent   ;
    }
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void initData() {

        BaseOrder baseOrder = (BaseOrder) getIntent().getSerializableExtra("key");
        if(baseOrder != null){
            startActivity(OrderDetailsBaseActivity.newIntent(this , baseOrder));
        }
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的订单");
        img_back  = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //String[] aa = {"全部" , "预约", "接单" ,"付款" ,"拒绝","完成"} ;
        String[] aa = { "预约中", "待执行" , "执行中" ,"待付款", "已完成"} ;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Arrays.asList(aa));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        initView()  ;
        initData()  ;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(OrderStatusChangeEvent event) {
        mViewPager.setCurrentItem(1);
    }
}
