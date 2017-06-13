package cn.people.weever.model;

/**
 * Created by Administrator on 2017/6/7.
 */

public class RouteOperateEvent {

    //计费
    public static final int TO_ORDER_CHARGING_OPERATE_TYPE            =             400          ;
    //等待
    public static final int TO_ORDER_WAITTING_OPERATE_TYPE            =             401          ;
    //再出发
    public static final int TO_ORDER_RESTART_OPERATE_TYPE             =             402          ;
    //去结算
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
