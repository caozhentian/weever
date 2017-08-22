package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2.
 */

public class Company implements Serializable{

    //会员号
    @SerializedName("companyNum")
    protected String mCompanyNum  ;

    //会员名称
    @SerializedName("companyName")
    protected String mCompanyName   ;

    //会员类型 普通会员 三星等
    @SerializedName("type")
    protected int mType   ;

    //90%
    @SerializedName("percent")
    protected String mPercent   ;

    public String getCompanyNum() {
        return mCompanyNum;
    }

    public void setCompanyNum(String companyNum) {
        mCompanyNum = companyNum;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }
}
