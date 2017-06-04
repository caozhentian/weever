package cn.people.weever.model;

/**
 * Created by Administrator on 2017/6/2.
 */

public class Amount {

    //现金
    public static final int AMOUNT_TYPE_CRASH = 0x01 ;
    //月票
    public static final int AMOUNT_TYPE_MONTHLY_TICKET = 0x02;
    //记账
    public static final int AMOUNT_TYPE_MONTHLY_ACCOUNTING = 0x03;
    //类型  现金 月票 记账
    private int mType   ;

    //金额
    private int mValue  ;

    public static int getAmountTypeCrash() {
        return AMOUNT_TYPE_CRASH;
    }

    public static int getAmountTypeMonthlyTicket() {
        return AMOUNT_TYPE_MONTHLY_TICKET;
    }

    public static int getAmountTypeMonthlyAccounting() {
        return AMOUNT_TYPE_MONTHLY_ACCOUNTING;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }
}
