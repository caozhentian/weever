package cn.people.weever.model;

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
    private String id ;
    @Property(nameInDb = "USERNAME")
    private String mUserName  ;

    @Transient
    private String mPassword   ;

    @Property(nameInDb = "TOKEN")
    private String mToken   ;

    @Generated(hash = 1037267353)
    public Driver(String id, String mUserName, String mToken) {
        this.id = id;
        this.mUserName = mUserName;
        this.mToken = mToken;
    }

    @Generated(hash = 911393595)
    public Driver() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getMUserName() {
        return this.mUserName;
    }

    public void setMUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getMToken() {
        return this.mToken;
    }

    public void setMToken(String mToken) {
        this.mToken = mToken;
    }
}
