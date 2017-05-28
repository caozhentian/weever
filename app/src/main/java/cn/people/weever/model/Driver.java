package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/** 司机
 * Created by weever on 2017/4/6.
 */


@Entity
public class Driver extends WeeverBean {

    public static final String USER_KEY = "USER_KEY";

    @Id
    @Property(nameInDb = "ID")
    private String mId ;

    //用户名
    @Property(nameInDb = "USERNAME")
    @SerializedName("userName") //JSON序列化的字段名
    private String mUserName  ;

    //密码
    @Transient
    @SerializedName("password") //JSON序列化的字段名
    private String mPassword       ;

    //车辆编号
    @SerializedName("cardNum") //JSON序列化的字段名
    @Property(nameInDb = "CARD_NUM")
    private String mCardNum         ;

    //班次 具体字段值服务器端可定义
    @Property(nameInDb = "WORKTIME_TYPE")
    @SerializedName("workTimeType") //JSON序列化的字段名
    private String  mWorkTimeType  ;

    //token 服务器返回
    @Property(nameInDb = "TOKEN")
    private String mToken   ;

    public Driver(String id, String mUserName, String mToken) {
        this.mId = id;
        this.mUserName = mUserName;
        this.mToken = mToken;
    }

    public Driver() {
    }

    @Generated(hash = 1174434292)
    public Driver(String mId, String mUserName, String mCardNum,
            String mWorkTimeType, String mToken) {
        this.mId = mId;
        this.mUserName = mUserName;
        this.mCardNum = mCardNum;
        this.mWorkTimeType = mWorkTimeType;
        this.mToken = mToken;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getCardNum() {
        return mCardNum;
    }

    public void setCardNum(String cardNum) {
        mCardNum = cardNum;
    }

    public String getWorkTimeType() {
        return mWorkTimeType;
    }

    public void setWorkTimeType(String workTimeType) {
        mWorkTimeType = workTimeType;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getMId() {
        return this.mId;
    }

    public void setMId(String mId) {
        this.mId = mId;
    }

    public String getMUserName() {
        return this.mUserName;
    }

    public void setMUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getMCardNum() {
        return this.mCardNum;
    }

    public void setMCardNum(String mCardNum) {
        this.mCardNum = mCardNum;
    }

    public String getMWorkTimeType() {
        return this.mWorkTimeType;
    }

    public void setMWorkTimeType(String mWorkTimeType) {
        this.mWorkTimeType = mWorkTimeType;
    }

    public String getMToken() {
        return this.mToken;
    }

    public void setMToken(String mToken) {
        this.mToken = mToken;
    }
}
