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
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.event.OrderStatusChangeEvent;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.net.BaseModel;
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
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_ORDER_BASE);
        mOrderService = new OrderService();
        setViewByBaseOrder();
        mOrderService.getDetails(mBaseOrder);
    }

    @Override
    public void initView() {
        mTvTitle.setText("");
    }

    private void setViewByBaseOrder() {
        mTvTitle.setText(BaseOrder.getTypeStr(mBaseOrder.getType()));
        mTvNum.setText(mBaseOrder.getOrderId()) ;
        mTvName.setText(mBaseOrder.getSubscribePerson());
        mTvPlantStartDate.setText(mBaseOrder.getPlanboardingTripNode().getDateStr());
        mTvPlantStartAddress.setText(mBaseOrder.getPlanboardingTripNode().getAddress().getPlaceName());
        if (mBaseOrder.getActualBoardingTripNode() != null) {
            mTvActualStartDate.setText(mBaseOrder.getActualBoardingTripNode().getDateStr());
            mTvActualStartAddress.setText(mBaseOrder.getActualBoardingTripNode().getAddress().getPlaceName());
        }
        mTvExpireDistanceCose.setText(mBaseOrder.getActualMileageCost() + "");
        mTvActualAllCost.setText(mBaseOrder.getPostDiscount() + "");
        mTvExpireTimeCose.setText(mBaseOrder.getActualWaitTimeCost() + "");
        if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_APPOINTMENT) {
            mLlTake.setVisibility(View.VISIBLE);
        } else if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_ORDER) {
            mLlStart.setVisibility(View.VISIBLE);
        } else if (mBaseOrder.getStatus() == BaseOrder.ORDER_STAUS_PAY) {
            mLlSettlent.setVisibility(View.VISIBLE);
        }
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
        mOrderService.takeOrder(mBaseOrder);
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
        startActivity(HomeActivity.newIntent(this, mBaseOrder));
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
    }

    protected void dealSuccess(BaseModel baseModel){
        showToast("操作成功");
        setOtherDetailInfo() ;
        EventBus.getDefault().postSticky(new OrderStatusChangeEvent());
        finish();
    }

    protected void setOtherDetailInfo(){

    }
}
