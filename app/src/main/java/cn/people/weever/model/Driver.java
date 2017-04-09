package cn.people.weever.model;

/** 司机
 * Created by weever on 2017/4/6.
 */


public class Driver extends WeeverBean {

    public static final String USER_KEY = "USER_KEY";

    private String mUserName ;

    private String mPassword   ;

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
}
