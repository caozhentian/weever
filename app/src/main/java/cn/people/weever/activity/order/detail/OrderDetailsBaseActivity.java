package cn.people.weever.activity.order.detail;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.HomeActivity;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.activity.order.clearing.AirportConverorOrderClearingActivity;
import cn.people.weever.activity.order.clearing.DayHalfOrderClearingActivity;
import cn.people.weever.activity.order.clearing.DayOrderClearingActivity;
import cn.people.weever.activity.order.clearing.FixedTimeOrderClearingActivity;
import cn.people.weever.activity.order.clearing.PickupOrderClearingActivity;
import cn.people.weever.activity.order.list.MyOrdersActivity;
import cn.people.weever.common.util.NavUtils;
import cn.people.weever.common.util.ToastUtil;
import cn.people.weever.config.OrderStatus;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.IOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.dialog.OKlDlg;
import cn.people.weever.event.OrderStatusChangeEvent;
import cn.people.weever.model.Address;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

public class OrderDetailsBaseActivity extends SubcribeCreateDestroyActivity {

    private static final String ARG_ORDER_BASE = "order_base";

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_top)
    LinearLayout mLayoutTop;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_plant_start_date)
    TextView mTvPlantStartDate;
    @BindView(R.id.tv_plant_start_address)
    TextView mTvPlantStartAddress;
    @BindView(R.id.tv_plant_end_date)
    TextView mTvPlantEndDate;
    @BindView(R.id.tv_plant_end_address)
    TextView mTvPlantEndAddress;
    @BindView(R.id.tv_actual_start_date)
    TextView mTvActualStartDate;
    @BindView(R.id.tv_actual_start_address)
    TextView mTvActualStartAddress;
    @BindView(R.id.tv_actual_end_date)
    TextView mTvActualEndDate;
    @BindView(R.id.tv_actual_end_address)
    TextView mTvActualEndAddress;
    @BindView(R.id.tv_actual_all_cost)
    TextView mTvActualAllCost;
    @BindView(R.id.tv_distance_cost)
    TextView tv_distance_cost;
    @BindView(R.id.tv_rental_cose)
    TextView mTvRentalCose;
    @BindView(R.id.tv_expire_time_cose)
    TextView mTvExpireTimeCose;
    @BindView(R.id.tv_expire_distance_cose)
    TextView mTvExpireDistanceCose;
    @BindView(R.id.btn_take)

    Button mBtnTake;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_settlent)
    Button mBtnSettlent;
    @BindView(R.id.ll_take)
    LinearLayout mLlTake;
    @BindView(R.id.ll_start)
    LinearLayout mLlStart;
    @BindView(R.id.ll_settlent)
    LinearLayout mLlSettlent;

    protected BaseOrder mBaseOrder;

    private OrderService mOrderService;
    private boolean take;
    private boolean isFindQuerySuccess;

    public static final Intent newIntent(Context context, BaseOrder baseOrder) {
        Intent intent = new Intent(context, DayOrderDetailsActivity.class);
        if (baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY) {
            intent = new Intent(context, DayOrderDetailsActivity.class);
        } else if (baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY_HALF) {
            intent = new Intent(context, DayHalfOrderDetailsActivity.class);
        } else if (baseOrder.getType() == BaseOrder.ORDER_TYPE_PICK_UP) {
            intent = new Intent(context, PickupOrderDetailsActivity.class);
        } else if (baseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR) {
            intent = new Intent(context, AirportConverorOrderDetailsActivity.class);
        } else if (baseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME) {
            intent = new Intent(context, FixTimeOrderDetailsActivity.class);
        }
        intent.putExtra(ARG_ORDER_BASE, baseOrder);
        return intent;
    }

    @Override
    public void initData() {
        NavUtils.initNavi(this);
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_ORDER_BASE);
        if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            //registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            //点亮屏幕
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //键盘锁管理器对象

            KeyguardManager km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

            //这里参数”unLock”作为调试时LogCat中的Tag

            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");

            kl.disableKeyguard();  //解锁


            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if(isScreenOn==false)
            {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                wl.acquire(10000);
                PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
                wl_cpu.acquire(10000);
            }
        }
        mOrderService = new OrderService();
        mOrderService.getDetails(mBaseOrder);
    }

    @Override
    public void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (isFindQuerySuccess && !take && mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
//            showToast("请首先完成 接单操作,否则后续其它操作异常");
//            startActivity(newIntent(this , mBaseOrder));
//        }
    }

    protected void setViewByBaseOrder() {
        mTvTitle.setText(BaseOrder.getTypeStr(mBaseOrder.getType()));
        mTvNum.setText(mBaseOrder.getOrderSN()) ;
        mTvName.setText(mBaseOrder.getSubscribePerson());
        mTvPlantStartDate.setText(mBaseOrder.getPlanboardingTripNode().getDateStr());
        mTvPlantStartAddress.setText(mBaseOrder.getPlanboardingTripNode().getAddress().getPlaceName());
        mTvPlantEndDate.setText(mBaseOrder.getPlanDropOffTripNode().getDateStr());
        mTvPlantEndAddress.setText(mBaseOrder.getPlanDropOffTripNode().getAddress().getPlaceName());

        tv_distance_cost.setText(mBaseOrder.getActualMileage() + " 公里");
        mTvRentalCose.setText(mBaseOrder.getStartingFare() + " RMB");
        mTvExpireDistanceCose.setText(mBaseOrder.getActualWaitTime() + "");
        mTvActualAllCost.setText(mBaseOrder.getPostDiscount() + " RMB");
        mTvExpireTimeCose.setText(mBaseOrder.getActualRideTime() + " 分钟");
        if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            mLlTake.setVisibility(View.VISIBLE);
            //设置相关的按钮不能用
            forbiddenUser() ;
        } else if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_ORDER) {
            mLlStart.setVisibility(View.VISIBLE);
        } else if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_PAY) {
            mLlSettlent.setVisibility(View.VISIBLE);
        }
        else if(mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_FINISH){

        }
        if(mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_PAY ||
                mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_FINISH){
            if (mBaseOrder.getActualBoardingTripNode() != null) {
                mTvActualStartDate.setText(mBaseOrder.getActualBoardingTripNode().getDateStr());
                mTvActualStartAddress.setText(mBaseOrder.getActualBoardingTripNode().getAddress().getPlaceName());
            }
            if(mBaseOrder.getActualDropOffTripNode() != null){
                mTvActualEndDate.setText(mBaseOrder.getActualDropOffTripNode().getDateStr());
                mTvActualEndAddress.setText(mBaseOrder.getActualDropOffTripNode().getAddress().getPlaceName());
            }
        }
        setOtherDetailInfo() ;
    }

    //接单界面 禁止用户操作
    private void forbiddenUser() {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_MENU) { return true; } return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_BACK:
//                if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
//                    showToast("请首先完成 接单操作,否则后续其它操作异常");
//                    return true;
//                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            showToast("请首先完成 接单操作,否则后续其它操作异常");
            return ;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            //unregisterReceiver(mHomeKeyEventReceiver);
        }
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        if (isFindQuerySuccess && mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            showToast("请首先完成 接单操作,否则后续其它操作异常");
            startActivity(newIntent(OrderDetailsBaseActivity.this, mBaseOrder));
        }
        else{
            //startActivity(MyOrdersActivity.newIntent(this));
            finish();
        }
    }

    @OnClick({R.id.btn_take,R.id.btn_cancel , R.id.btn_start, R.id.btn_settlent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take:
                take();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_start:
                start();
                break;
            case R.id.btn_settlent:
                settlent();
                break;
        }
    }

    private void take() {
        OKCancelDlg.createCancelOKDlg(this, "确定接单吗?", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                mOrderService.takeOrder(mBaseOrder);
            }
        });

    }

    private void cancel() {
        OKCancelDlg.createCancelOKDlg(this, "确定拒绝吗?", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                mOrderService.cancelOrder(mBaseOrder);
            }
        });

    }

    private void start() {
        if( OrderStatus.ORDER_STATSU_RUNNING){
            ToastUtil.showToast("订单正在执行中，不能操作");
            return ;
        }
        OKCancelDlg.createCancelOKDlg(this, "点击出发，自动开始计费，确定出发吗？", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                startActivity(HomeActivity.newIntent(OrderDetailsBaseActivity.this, mBaseOrder));
                finish();
            }
        });


    }
    private void routeplanToNavi() {

        //根据订单初始化
        Address srcAddress  = mBaseOrder.getPlanboardingTripNode().getAddress() ;
        String  src = srcAddress.getPlaceName() ;
        Address destAddress = mBaseOrder.getPlanDropOffTripNode().getAddress()  ;
        String  dest = destAddress.getPlaceName() ;

        LatLng srcLating   =  new LatLng(srcAddress.getLatitude(), srcAddress.getLongitude());
        LatLng destLating  =  new LatLng(destAddress.getLatitude(), destAddress.getLongitude());

        NavUtils.routeplanToNavi(this ,srcLating , src , destLating , dest , mBaseOrder);
    }
    private void settlent() {
        if (mBaseOrder.getType() == BaseOrder.ORDER_TYPE_DAY) {
            startActivity(DayOrderClearingActivity.newIntent(this, mBaseOrder));
        } else if (mBaseOrder.getType() == BaseOrder.ORDER_TYPE_DAY_HALF) {
            startActivity(DayHalfOrderClearingActivity.newIntent(this, mBaseOrder));
        } else if (mBaseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME) {
            startActivity(FixedTimeOrderClearingActivity.newIntent(this, mBaseOrder));
        } else if (mBaseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR) {
            startActivity(AirportConverorOrderClearingActivity.newIntent(this, mBaseOrder));
        } else if (mBaseOrder.getType() == BaseOrder.ORDER_TYPE_PICK_UP) {
            startActivity(PickupOrderClearingActivity.newIntent(this, mBaseOrder));
        }
        finish();
    }

    protected void dealSuccess(BaseModel baseModel){
        showToast("操作成功");
        if(baseModel.getData() instanceof  BaseOrder) {
            mBaseOrder = (BaseOrder) baseModel.getData();
        }
        isFindQuerySuccess = true ;
        setViewByBaseOrder();
//        EventBus.getDefault().postSticky(new OrderStatusChangeEvent());
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TAKE_NET_REQUST){
            take = true ;
            EventBus.getDefault().postSticky(new OrderStatusChangeEvent());
        }
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TAKE_NET_REQUST
                || baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_CANCEL_NET_REQUST){
            finish();
        }


    }

    protected void setOtherDetailInfo(){

    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ( action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) { // 监听home键和菜单键
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if(reason.equals(SYSTEM_HOME_KEY) || reason.equals(SYSTEM_HOME_KEY_LONG)) {
                    showToast("请首先完成 接单操作,否则后续其它操作异常");
                    startActivity(newIntent(OrderDetailsBaseActivity.this, mBaseOrder));
                }
            }
        }
    };

    public void accept(){
        OKlDlg.createOKDlg(this, "请尽快完成接单操作,否则无法进行后续的其它操作", new IOK() {
            @Override
            public void ok() {
                finish();
            }
        });
    }
}
