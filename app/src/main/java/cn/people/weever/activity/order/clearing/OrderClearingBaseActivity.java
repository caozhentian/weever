package cn.people.weever.activity.order.clearing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.car.TrackQueryActivity;
import cn.people.weever.model.Amount;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

public  class OrderClearingBaseActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.tv_src)
    TextView mTvSrc;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;

    @BindView(R.id.tv_member)
    TextView mTvMember;
    @BindView(R.id.tv_pre_discount)
    TextView mTvPreDiscount;
    @BindView(R.id.tv_post_discount)
    TextView mTvPostDiscount;
    @BindView(R.id.tv_percent_discount)
    TextView mTvPercentDiscount;

    @BindView(R.id.tv_start_up_cost)
    TextView mTvStartUpCost;//TODO
    @BindView(R.id.tv_distance_cost)
    TextView mTvDistanceCost;
    @BindView(R.id.tv_ride_time_cost)
    TextView mTvRideTimeCost;
    @BindView(R.id.tv_wait_time_cost)
    TextView mTvWaitTimeCost;

    @BindView(R.id.spn_settlement_first)
    Spinner mSpnSettlementFirst;
    @BindView(R.id.edt_settlement_first_cost)
    EditText mEdtSettlementFirstCost;
    @BindView(R.id.spn_settlement_second)
    Spinner mSpnSettlementSecond;
    @BindView(R.id.edt_settlement_second_cost)
    EditText mEdtSettlementSecondCost;

    @BindView(R.id.btn_compute)
    Button mBtnCompute;
    @BindView(R.id.btn_trace)
    Button mBtnTrace;

    protected BaseOrder mBaseOrder ;

    protected OrderService mOrderService ;

    public static final Intent newIntent(Context context, BaseOrder baseOrder) {
        Intent intent = new Intent(context, OrderClearingBaseActivity.class);
        if(baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY){
            intent = new Intent(context, DayOrderClearingActivity.class);
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY_HALF){
            intent = new Intent(context, DayHalfOrderClearingActivity.class);
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR){
            intent = new Intent(context, AirportConverorOrderClearingActivity.class);
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_PICK_UP){
            intent = new Intent(context, PickupOrderClearingActivity.class);
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME){
            intent = new Intent(context, FixedTimeOrderClearingActivity.class);
        }
        intent.putExtra(BaseOrder.ORDER_ID, baseOrder);
        return intent;
    }

    @Override
    public void initData() {
        mOrderService = new OrderService() ;
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(BaseOrder.ORDER_ID);
        setViewByBaseOrder();
    }

    @Override
    public void initView() {
        mTvTitle.setText("");
    }

    protected void setViewByBaseOrder(){
        mTvSrc.setText(mBaseOrder.getPlanboardingTripNode().getAddress().getPlaceName());
        mTvDesc.setText(mBaseOrder.getPlanDropOffTripNode().getAddress().getPlaceName());
        mTvMember.setText(mBaseOrder.getCompany().getCompanyNum());
        mTvPreDiscount.setText(mBaseOrder.getPreDiscount() + "");
        mTvPostDiscount.setText(mBaseOrder.getPostDiscount() + "");
        mTvPercentDiscount.setText(mBaseOrder.getPercentDiscount() );
        mTvStartUpCost.setText(mBaseOrder.getStartingFare());
        mTvDistanceCost.setText(mBaseOrder.getActualMileageCost()+ "");
        mTvRideTimeCost.setText(mBaseOrder.getActualRideTimeCost()+ "");
        mTvWaitTimeCost.setText(mBaseOrder.getActualWaitTimeCost()+ "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_clearing);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    protected void queryOrderDetails(BaseOrder baseOrder) {
        mOrderService.getDetails(baseOrder);
    }

    @OnClick({R.id.btn_compute, R.id.btn_trace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_compute:
                toCompute() ;
                break;
            case R.id.btn_trace:
                getTrace() ;
                break;
        }
    }

    private void toCompute(){
        if(mBaseOrder == null){
            return ;
        }
        String  firstCost  =  mEdtSettlementFirstCost.getEditableText().toString()   ;
        String  secondCost =  mEdtSettlementSecondCost.getEditableText().toString()  ;
        if(TextUtils.isEmpty(firstCost) && TextUtils.isEmpty(secondCost)){
            showToast("请选择");
            return ;
        }
        OrderSubmitInfo orderSubmitInfo = new OrderSubmitInfo() ;
        orderSubmitInfo.setOrderId(mBaseOrder.getOrderId())    ;
        List<Amount> amountList = new ArrayList<>(2) ;
        if(!TextUtils.isEmpty(firstCost)){
            int  fCost =  Integer.parseInt(firstCost) ;
            int  fType =  getAmountType(mSpnSettlementFirst) ;
            Amount amount = new Amount(fType , fCost) ;
            amountList.add(amount) ;
        }
        if(!TextUtils.isEmpty(secondCost)){
            int  sCost =  Integer.parseInt(secondCost) ;
            int  sType =  getAmountType(mSpnSettlementSecond) ;
            Amount amount = new Amount(sType , sCost) ;
            amountList.add(amount) ;
        }
        mOrderService.submit(orderSubmitInfo);
    }

    private void getTrace() {
        if(mBaseOrder == null){
            return ;
        }
        long startTime = mBaseOrder.getActualBoardingTripNode().getTime();
        long endTime = mBaseOrder.getActualDropOffTripNode().getTime();
        startActivity(TrackQueryActivity.newIntent(this, startTime, endTime));
    }


    private int getAmountType(Spinner spinner){
        String typeStr = (String) spinner.getSelectedItem();
        int   acountType = Amount.AMOUNT_TYPE_CRASH ;
        switch (typeStr){
            case "A":
                acountType = Amount.AMOUNT_TYPE_CRASH ;
                break ;
            case "B":
                acountType = Amount.AMOUNT_TYPE_MONTHLY_TICKET ;
                break ;
            case "C"  :
                acountType = Amount.AMOUNT_TYPE_MONTHLY_ACCOUNTING ;
                break ;
            default:
                break ;
        }
        return acountType ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processSubmitOrderEvent(@Nullable BaseModel<Object> baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_SUBMIT_NET_REQUST){
           showToast(baseModel.getMessage());
        }
    }
}


