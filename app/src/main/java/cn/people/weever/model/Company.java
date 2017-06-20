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

    @SerializedName("companyName")
    protected String mCompanyName   ;

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
