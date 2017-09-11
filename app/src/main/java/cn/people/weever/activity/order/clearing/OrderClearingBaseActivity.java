package cn.people.weever.activity.order.clearing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.activity.car.TrackQueryActivity;
import cn.people.weever.model.Amount;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.Company;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

public  class OrderClearingBaseActivity extends SubcribeCreateDestroyActivity {

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
    @BindView(R.id.tv_member_name)
    TextView tv_member_name;
    @BindView(R.id.tv_pre_discount)
    TextView mTvPreDiscount;
    @BindView(R.id.tv_post_discount)
    TextView mTvPostDiscount;
    @BindView(R.id.tv_percent_discount)
    TextView mTvPercentDiscount;


    @BindView(R.id.tv_start_up_cost)
    TextView mTvStartUpCost;//TODO
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.tv_distance_cost)
    TextView mTvDistanceCost;
    @BindView(R.id.tv_ride_time)
    TextView tv_ride_time;
    @BindView(R.id.tv_ride_time_cost)
    TextView mTvRideTimeCost;
    @BindView(R.id.tv_wait_time)
    TextView tv_wait_time;
    @BindView(R.id.tv_wait_time_cost)
    TextView mTvWaitTimeCost;

    @BindView(R.id.spn_member)
    Spinner mSpnMember;
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

    private List<Company> mCompanies ;

    private Company  mCompany  ;

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
        if(mBaseOrder != null) {
            setViewByBaseOrder();
            queryOrderDetails(mBaseOrder);
        }
        mOrderService.getMembers();
    }

    @Override
    public void initView() {
        mTvTitle.setText("결산"); //结算
    }

    protected void setViewByBaseOrder(){
        mTvSrc.setText(mBaseOrder.getPlanboardingTripNode().getAddress().getPlaceName());
        if(mBaseOrder.getPlanDropOffTripNode() != null &&
                mBaseOrder.getPlanDropOffTripNode().getAddress() != null ) {
            mTvDesc.setText(mBaseOrder.getPlanDropOffTripNode().getAddress().getPlaceName());
        }
        mTvMember.setText(mBaseOrder.getCompany().getCompanyNum());
        tv_member_name.setText(mBaseOrder.getCompany().getCompanyName());
        mTvPreDiscount.setText(mBaseOrder.getPreDiscount() + "  RMB");
        mTvPostDiscount.setText(mBaseOrder.getPostDiscount() + "  RMB");
        mTvPercentDiscount.setText(mBaseOrder.getPercentDiscount() );
        mTvStartUpCost.setText(mBaseOrder.getStartingFare()+" RMB");

        tv_distance.setText("(" + mBaseOrder.getActualMileage()+ "公里)");
        mTvDistanceCost.setText("" + mBaseOrder.getActualMileage()+ " 公里");
        tv_distance.setVisibility(View.INVISIBLE);
        mTvRideTimeCost.setText(""+ mBaseOrder.getActualRideTime()+ " Minute");
        tv_ride_time.setText("("+ mBaseOrder.getActualRideTime()+ "Minute)");
        tv_ride_time.setVisibility(View.INVISIBLE);
        tv_wait_time.setText("("+ mBaseOrder.getActualWaitTime()+ "Minute)");
        tv_wait_time.setVisibility(View.INVISIBLE);
        mTvWaitTimeCost.setText(""+ mBaseOrder.getActualWaitTime()+ "");
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

    private void updateMemerInfo(){
        mTvMember.setText(mCompany.getCompanyNum());
        tv_member_name.setText(mCompany.getCompanyName());
        //mTvPreDiscount.setText(mBaseOrder.getPreDiscount() + "  RMB");
        float per = Float.parseFloat(mCompany.getPercent().replace("%" , "")) * 0.01f ;
        mTvPostDiscount.setText(Math.ceil(mBaseOrder.getPreDiscount() * per) + "  RMB");
        mTvPercentDiscount.setText(mCompany.getPercent() );
    }

    private void toCompute(){
        if(mBaseOrder == null){
            return ;
        }
        String  firstCost  =  mEdtSettlementFirstCost.getEditableText().toString()   ;
        String  secondCost =  mEdtSettlementSecondCost.getEditableText().toString()  ;
        if(TextUtils.isEmpty(firstCost) && TextUtils.isEmpty(secondCost)){
            showToast("请输入结算金额");
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
        orderSubmitInfo.setAmountList(amountList) ;
        orderSubmitInfo.setCompany(mCompany)    ;
        mOrderService.submit(orderSubmitInfo);
    }

    private void getTrace() {
        if(mBaseOrder == null){
            return ;
        }
       long startTime = 0 ;
       if(mBaseOrder.getActualBoardingTripNode() != null){
           startTime = mBaseOrder.getActualBoardingTripNode().getTime();
       }
        long endTime = 0 ;
       if(mBaseOrder.getActualDropOffTripNode() != null) {
            endTime = mBaseOrder.getActualDropOffTripNode().getTime();
       }
        startActivity(TrackQueryActivity.newIntent(this, startTime, endTime,mBaseOrder.getCardNum()));
    }


    private int getAmountType(Spinner spinner){
        String typeStr = (String) spinner.getSelectedItem();
        int   acountType = Amount.AMOUNT_TYPE_CRASH ;
        switch (typeStr.substring(0 ,2)){
            case "现金":
                acountType = Amount.AMOUNT_TYPE_CRASH ;
                break ;
            case "小票":
                acountType = Amount.AMOUNT_TYPE_MONTHLY_TICKET ;
                break ;
            case "记账"  :
                acountType = Amount.AMOUNT_TYPE_MONTHLY_ACCOUNTING ;
                break ;
            default:
                break ;
        }
        return acountType ;
    }

    public void dealSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_SUBMIT_NET_REQUST){
            dealSubmitSuccess(baseModel)                         ;
        }
        else if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_TYPE_MEMBERS_NET_REQUST){
            processCompanyEvent(baseModel) ;
        }
        else {
            processOrderEvent(baseModel) ;
        }
    }

    public void dealSubmitSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_SUBMIT_NET_REQUST){
           showToast(baseModel.getMessage()) ;
           finish()                          ;
        }
    }

    public void processOrderEvent(@Nullable BaseModel baseModel){

    }

    public void processCompanyEvent(@Nullable BaseModel baseModel){
        mCompanies = (List<Company>) baseModel.getData();
        mCompany   = mCompanies.get(0) ;
        updateMemerInfo() ;
        List<String>  names = new LinkedList<>() ;
        for( Company company :mCompanies){
            names.add(company.getCompanyName()) ;
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        mSpnMember .setAdapter(adapter);
        mSpnMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mCompany = mCompanies.get(pos) ;
                updateMemerInfo() ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

}


