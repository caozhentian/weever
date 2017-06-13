package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import cn.people.weever.common.util.DatetimeUtil;

/**订单 的基类
 * Created by weever on 2017/4/6.
 */

public class BaseOrder extends WeeverBean {

    //订单状态 常量定义
    public static final int  ORDER_STAUS_ALL           = 0x00 ;  //全部状态
    public static final int  ORDER_STAUS_APPOINTMENT  = 0x01 ;  //预约中
    public static final int  ORDER_STAUS_ORDER         = 0x02;   //待执行订单
    public static final int  ORDER_STAUS_PAY            = 0x03 ;  //待付款
    public static final int  ORDER_STAUS_CANCEL        = 0x04 ;  //取消
    public static final int  ORDER_STAUS_FINISH         = 0x05 ; //已完成

    //订单类型 常量定义
    public static final int  ORDER_TYPE_DAY           = 0x01 ;             //日租
    public static final int  ORDER_TYPE_DAY_HALF    = 0x02 ;             //半日租
    public static final int  ORDER_TYPE_PICK_UP     = 0x03;              //接机
    public static final int  ORDER_TYPE_AIRPORT_CONVEYOR     = 0x04;   //送机Fixed time
    public static final int  ORDER_TYPE_AIRPORT_FIXED_TIME   = 0x05;  //固定时间租车

    public static final String ORDER_ID      =  "orderId" ;


    //订单ID
    @SerializedName("orderId") //JSON序列化的字段名
	protected String mOrderId                   ;
    //订单状态 具体取值见订单状态的定义
    @SerializedName("status")
    protected int mStatus;
    //订单类型 1：日租 2 半日租 3 接机 4 送机 5 固定时间租车
    @SerializedName("type")
    protected int mType                               ;
    //预约人 subscribePerson
    @SerializedName("subscribePerson")
    protected String mSubscribePerson              ;

    //公司
    @SerializedName("company")
    protected Company mCompany  ;
	
    //预约上车信息
    @SerializedName("planboardingTripNode")
    protected TripNode mPlanboardingTripNode    ;
    //预约下车信息
    @SerializedName("planDropOffTripNode")
    protected TripNode mPlanDropOffTripNode     ;
    //预计总时间 5小时
    @SerializedName("planTotalTime")
    protected String mPlanTotalTime              ;

	//实际上车信息
    @SerializedName("actualBoardingTripNode")
    protected TripNode mActualBoardingTripNode    ;
	//实际下车信息
    @SerializedName("actualDropOffTripNode")
    protected TripNode mActualDropOffTripNode       ;

    @SerializedName("preDiscount")
    private   int mPreDiscount         ;
    @SerializedName("postDiscount")
    private   int mPostDiscount        ;
    @SerializedName("percentDiscount")
    private   String mPercentDiscount  ;

    //起步价
    @SerializedName("startingFare")
    private String mStartingFare ;
	/*
	实际乘车时间 6小时
	*/
    @SerializedName("actualRideTime")
    protected String mActualRideTime             ;
    /*
	实际乘车时间费用
	*/
    @SerializedName("actualRideTimeCost")
    protected int mActualRideTimeCost    ;

	//实际等待时间 30分钟
    @SerializedName("waitTime")
	protected String mActualWaitTime              ;
	/**
     实际等待时间费用
	*/
    @SerializedName("waitTimeCost")
    protected int mActualWaitTimeCost           ;

	//实际总里程
    @SerializedName("actualMileage")
	protected String mActualMileage                ;
	/*
	实际总里程费用
	*/
    @SerializedName("actualMileageCost")
    protected int mActualMileageCost            ;

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getSubscribePerson() {
        return mSubscribePerson;
    }

    public void setSubscribePerson(String subscribePerson) {
        mSubscribePerson = subscribePerson;
    }

    public Company getCompany() {
        return mCompany;
    }

    public void setCompany(Company company) {
        mCompany = company;
    }

    public TripNode getPlanboardingTripNode() {
        return mPlanboardingTripNode;
    }

    public void setPlanboardingTripNode(TripNode planboardingTripNode) {
        mPlanboardingTripNode = planboardingTripNode;
    }

    public TripNode getPlanDropOffTripNode() {
        return mPlanDropOffTripNode;
    }

    public void setPlanDropOffTripNode(TripNode planDropOffTripNode) {
        mPlanDropOffTripNode = planDropOffTripNode;
    }

    public String getPlanTotalTime() {
        return mPlanTotalTime;
    }

    public void setPlanTotalTime(String planTotalTime) {
        mPlanTotalTime = planTotalTime;
    }

    public TripNode getActualBoardingTripNode() {
        return mActualBoardingTripNode;
    }

    public void setActualBoardingTripNode(TripNode actualBoardingTripNode) {
        mActualBoardingTripNode = actualBoardingTripNode;
    }

    public TripNode getActualDropOffTripNode() {
        return mActualDropOffTripNode;
    }

    public void setActualDropOffTripNode(TripNode actualDropOffTripNode) {
        mActualDropOffTripNode = actualDropOffTripNode;
    }

    public String getActualRideTime() {
        return mActualRideTime;
    }

    public void setActualRideTime(String actualRideTime) {
        mActualRideTime = actualRideTime;
    }

    public int getActualRideTimeCost() {
        return mActualRideTimeCost;
    }

    public void setActualRideTimeCost(int actualRideTimeCost) {
        mActualRideTimeCost = actualRideTimeCost;
    }

    public String getActualWaitTime() {
        return mActualWaitTime;
    }

    public void setActualWaitTime(String actualWaitTime) {
        mActualWaitTime = actualWaitTime;
    }

    public int getActualWaitTimeCost() {
        return mActualWaitTimeCost;
    }

    public void setActualWaitTimeCost(int actualWaitTimeCost) {
        mActualWaitTimeCost = actualWaitTimeCost;
    }

    public String getActualMileage() {
        return mActualMileage;
    }

    public void setActualMileage(String actualMileage) {
        mActualMileage = actualMileage;
    }

    public int getActualMileageCost() {
        return mActualMileageCost;
    }

    public void setActualMileageCost(int actualMileageCost) {
        mActualMileageCost = actualMileageCost;
    }

    public int getPreDiscount() {
        return mPreDiscount;
    }

    public void setPreDiscount(int preDiscount) {
        mPreDiscount = preDiscount;
    }

    public int getPostDiscount() {
        return mPostDiscount;
    }

    public void setPostDiscount(int postDiscount) {
        mPostDiscount = postDiscount;
    }

    public String getPercentDiscount() {
        return mPercentDiscount;
    }

    public void setPercentDiscount(String percentDiscount) {
        mPercentDiscount = percentDiscount;
    }

    public String getStartingFare() {
        return mStartingFare;
    }

    public void setStartingFare(String startingFare) {
        mStartingFare = startingFare;
    }


    public String getStartEndTimeStr(){

        if(mPlanboardingTripNode != null && mPlanDropOffTripNode != null){
            String startDate = DatetimeUtil.getDate(mPlanboardingTripNode.getTime( ) * 1000 ,  DatetimeUtil.DEFAULT_FORMAT1) ;
            String endDate   = DatetimeUtil.getDate(mPlanDropOffTripNode.getTime( ) * 1000  ,  DatetimeUtil.DEFAULT_FORMAT1) ;
            return startDate + " - " + endDate ;
        }
        return "" ;
    }

    public static final String getTypeStr(int type){
       switch (type){
           case ORDER_TYPE_DAY:
               return "日租" ;
           case ORDER_TYPE_DAY_HALF:
               return "半日租" ;
           case ORDER_TYPE_PICK_UP:
               return "接送机" ;
           case ORDER_TYPE_AIRPORT_CONVEYOR:
               return "接送机" ;
           case ORDER_TYPE_AIRPORT_FIXED_TIME:
               return "固定时间" ;
           default:
               return "" ;
       }
    }

    public static final String getStatusStr(int status){
        switch (status){
            case ORDER_STAUS_APPOINTMENT:
                return "预约中" ;
            case ORDER_STAUS_ORDER:
                return "待执行" ;
            case ORDER_STAUS_PAY:
                return "待付款" ;
            default:
                return "" ;
        }
    }

}
