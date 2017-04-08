package cn.people.weever.model;

/** 租车计划，类似于 商品信息
 * Created by weever on 2017/4/8.
 */

public class Plan extends WeeverBean {

    /*
   出发点
    */
    private String src ;

    private String dest ;
    //日租、半日租、接送机三种模式
    private String mode ;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
