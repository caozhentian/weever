package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Property;

/** 司机
 * Created by weever on 2017/4/6.
 */


//@Entity
public class Driver extends WeeverBean {

    public static final String USER_KEY = "USER_KEY";

    //@Id
    //@Property(nameInDb = "ID")
    private String mId ;

    @SerializedName("name") //JSON序列化的字段名
    private String mName  ;

    //用户名
    //@Property(nameInDb = "USERNAME")
    @SerializedName("userName") //JSON序列化的字段名
    private String mUserName  ;

    //密码
    //@Transient
    @SerializedName("password") //JSON序列化的字段名
    private String mPassword       ;

    //车辆编号
    @SerializedName("carNum") //JSON序列化的字段名
    @Property(nameInDb = "CARD_NUM")
    private String mCardNum         ;

    //班次 具体字段值服务器端可定义
    //@Property(nameInDb = "WORKTIME_TYPE")
    @SerializedName("workTimeType") //JSON序列化的字段名
    private int  mWorkTimeType  = 1 ;

    //token ,服务器返回值
    //@Property(nameInDb = "TOKEN")
    @SerializedName("token") //JSON序列化的字段名
    private String mToken   ;

    public Driver(String id, String mUserName, String mToken) {
        this.mId = id;
        this.mUserName = mUserName;
        this.mToken = mToken;
    }

    public Driver() {
    }

    public Driver(String mId, String mUserName, String mCardNum,
            int mWorkTimeType, String mToken) {
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

    public int getWorkTimeType() {
        return mWorkTimeType;
    }

    public void setWorkTimeType(int workTimeType) {
        mWorkTimeType = workTimeType;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }


    @Override
    public String toString() {
        return "Driver{" +
                "mId='" + mId + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mPassword='" +  '\'' +
                ", mCardNum='" + mCardNum + '\'' +
                ", mWorkTimeType=" + mWorkTimeType +
                ", mToken='" + mToken + '\'' +
                '}';
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
