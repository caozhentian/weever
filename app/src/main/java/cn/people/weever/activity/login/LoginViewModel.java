package cn.people.weever.activity.login;

/**
 * Created by Administrator on 2017/4/8.
 */

public class LoginViewModel {

    private String mUserName ;

    private String mPassword   ;

    private String mCardNum      ;

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
}
