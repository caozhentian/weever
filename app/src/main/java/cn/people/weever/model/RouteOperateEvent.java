package cn.people.weever.model;

/**
 * Created by Administrator on 2017/6/7.
 */

public class RouteOperateEvent {

    public static final int TO_ORDER_CHARGING_OPERATE_TYPE            =             400          ;
    public static final int TO_ORDER_WAITTING_OPERATE_TYPE            =             401          ;
    public static final int TO_ORDER_RESTART_OPERATE_TYPE             =             402          ;
    public static final int TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE      =             403          ;

    private int        mOperateType    ;

    private TripNode    mTripNode       ;

    public int getOperateType() {
        return mOperateType;
    }

    public void setOperateType(int operateType) {
        mOperateType = operateType;
    }

    public TripNode getTripNode() {
        return mTripNode;
    }

    public void setTripNode(TripNode tripNode) {
        mTripNode = tripNode;
    }
}
