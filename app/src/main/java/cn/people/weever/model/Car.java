package cn.people.weever.model;

/**
 * Created by ztcao on 2017/4/6.
 */

public class Car extends WeeverBean {

    /*车辆编号

     */
    protected  String num ;

    /*
    车辆类型分为5座型和7座型
     */
    protected  String type ;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
