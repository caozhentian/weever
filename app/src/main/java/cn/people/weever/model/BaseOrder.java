package com.example.administrator.firstapplication.model;

/**订单 的基类
 * Created by weever on 2017/4/6.
 */

public class BaseOrder extends WeeverBean {

    public static String ORDER_ID      =  "orderId" ;

    public static String USE_CAR_TYPE  =  ""     ;


	protected  String mOrderId                   ;

    //订单状态
    private int status                            ;
    //订单类型
    private int type                              ;
	
    //预约人 subscribePerson
    private String mSubscribePerson              ;

    //公司
    private Company mCompany  ;
	
    //预约上车信息
    private TripNode mPlanboardingTripNode    ;
    //预约下车信息
    private TripNode mPlanDropOffTripNode     ;
    //预计总时间
    private String mPlanTotalTime              ;

	//实际上车信息
    private TripNode mActualBoardingTripNode    ;
	//实际下车信息
    private String mActualDropOffTripNode       ;
	/*
	实际乘车时间
	*/
    private String mActualRideTime             ;
    /*
	实际乘车时间费用
	*/
    private int mActualRideTimeCost    ;

	//实际等待时间
	private String mWaitTime              ; 
	/**
     实际等待时间费用
	*/
    private int mWaitTimeCost           ;

	//实际总里程
	private String mActualMileage                ;
	/*
	实际总里程费用
	*/
    private int mActualMileageCost            ;
	




}
