package cn.people.weever.model;

/**订单 的基类
 * Created by weever on 2017/4/6.
 */

public class BaseOrder extends WeeverBean {

    public static String ORDER_ID = "orderId" ;

    //乘车人
    private String mPassenger            ;

    //上车时间
    private String mPlanboardingTime    ;
    //下车地点
    private String mPlanDropOffArea     ;

    //预计总时间
    private String mPlanTotalTime          ;

    private String mActualBoardingTime    ;
    private String mActualBoardingArea    ;
    private String mActualDropOffArea     ;

    private String mTotalExpenses  ;

    private String mOvertimeCost      ;

    private String mMileageCost          ;

    public String getPassenger() {
        return mPassenger;
    }

    public void setPassenger(String passenger) {
        mPassenger = passenger;
    }

    public String getPlanboardingTime() {
        return mPlanboardingTime;
    }

    public void setPlanboardingTime(String planboardingTime) {
        mPlanboardingTime = planboardingTime;
    }

    public String getPlanDropOffArea() {
        return mPlanDropOffArea;
    }

    public void setPlanDropOffArea(String planDropOffArea) {
        mPlanDropOffArea = planDropOffArea;
    }

    public String getPlanTotalTime() {
        return mPlanTotalTime;
    }

    public void setPlanTotalTime(String planTotalTime) {
        mPlanTotalTime = planTotalTime;
    }

    public String getActualBoardingTime() {
        return mActualBoardingTime;
    }

    public void setActualBoardingTime(String actualBoardingTime) {
        mActualBoardingTime = actualBoardingTime;
    }

    public String getActualBoardingArea() {
        return mActualBoardingArea;
    }

    public void setActualBoardingArea(String actualBoardingArea) {
        mActualBoardingArea = actualBoardingArea;
    }

    public String getActualDropOffArea() {
        return mActualDropOffArea;
    }

    public void setActualDropOffArea(String actualDropOffArea) {
        mActualDropOffArea = actualDropOffArea;
    }

    public String getTotalExpenses() {
        return mTotalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        mTotalExpenses = totalExpenses;
    }

    public String getOvertimeCost() {
        return mOvertimeCost;
    }

    public void setOvertimeCost(String overtimeCost) {
        mOvertimeCost = overtimeCost;
    }

    public String getMileageCost() {
        return mMileageCost;
    }

    public void setMileageCost(String mileageCost) {
        mMileageCost = mileageCost;
    }
}
