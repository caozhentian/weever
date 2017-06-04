package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import cn.people.weever.model.Company;
import cn.people.weever.model.TripNode;
import cn.people.weever.model.WeeverBean;

/**订单 的基类
 * Created by weever on 2017/4/6.
 */

public class BaseOrder extends WeeverBean {

    //订单状态 常量定义
    public static final int  ORDER_STAUS_ALL           = 0x00 ;  //全部状态
    public static final int  ORDER_STAUS_APPOINTMENT  = 0x01 ;  //预约
    public static final int  ORDER_STAUS_ORDER         = 0x02;   //接单
    public static final int  ORDER_STAUS_CANCEL        = 0x03 ;  //取消
    public static final int  ORDER_STAUS_PAY            = 0x04 ;  //待付款
    public static final int  ORDER_STAUS_FINISH         = 0x05 ; //已完成

    //订单类型 常量定义
    public static final int  ORDER_TYPE_DAY           = 0x01 ;             //日租
    public static final int  ORDER_STAUS_DAY_HALF    = 0x02 ;             //半日租
    public static final int  ORDER_STAUS_PICK_UP     = 0x03;              //接机
    public static final int  ORDER_STAUS_AIRPORT_CONVEYOR     = 0x04;   //送机Fixed time
    public static final int  ORDER_STAUS_AIRPORT_FIXED_TIME   = 0x05;  //固定时间租车

    public static final String ORDER_ID      =  "orderId" ;


    //订单ID
    @SerializedName("orderId") //JSON序列化的字段名
	protected  String mOrderId                   ;
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
    protected String mActualDropOffTripNode       ;


	/*
	实际乘车时间 6小时
	*/
    @SerializedName("actualRideTime")
    protected String  mActualRideTime             ;
    /*
	实际乘车时间费用
	*/
    @SerializedName("actualRideTimeCost")
    protected int mActualRideTimeCost    ;

	//实际等待时间 30分钟
    @SerializedName("waitTime")
	protected String mWaitTime              ; 
	/**
     实际等待时间费用
	*/
    @SerializedName("waitTimeCost")
    protected int mWaitTimeCost           ;

	//实际总里程
    @SerializedName("actualMileage")
	protected String mActualMileage                ;
	/*
	实际总里程费用
	*/
    @SerializedName("actualMileageCost")
    protected int mActualMileageCost            ;


}
