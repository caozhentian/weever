package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ztcao on 2017/4/6.
 */

public class Car extends WeeverBean {

    /*车辆编号
     */
    @SerializedName("num")
    protected  String mNum ;  //JSON序列化的字段名

    /*
    车辆类型分为5座型和7座型 ,具体字段 服务器段可定义
     */
    @SerializedName("type")
    protected  String mType ; //JSON序列化的字段名
	
	/*
	车辆表述 五座豪华舒适型
	*/
    @SerializedName("description")
    protected  String  mDescription ; //JSON序列化的字段名

    public String getNum() {
        return mNum;
    }

    public void setNum(String num) {
        mNum = num;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
