package cn.people.weever.common.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/3 0003.
 */

public class NumberFormat {
    public final  static String getdouble(double f , int DecimalDigit){
        BigDecimal   b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(DecimalDigit ,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(f1);
    }
}
