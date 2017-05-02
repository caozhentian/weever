package cn.people.weever.service;

import android.text.TextUtils;

import cn.people.weever.activity.login.LoginViewModel;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.model.Driver;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DriverService {

    public void login(LoginViewModel loginViewModel){
        if(loginViewModel == null){
            throw new IllegalArgumentException("参数值为null") ;
        }
        if(TextUtils.isEmpty(loginViewModel.getUserName())){
            throw new IllegalArgumentException("用户名不能为空") ;
        }
        if(TextUtils.isEmpty(loginViewModel.getPassword())){
            throw new IllegalArgumentException("密码不能为空") ;
        }
    }

    public void save(Driver driver){
        if(driver == null){
            return ;
        }
        WeeverApplication.setCurUser(driver);
    }

    public Driver getCurDriver(){
        return WeeverApplication.getCurUser() ;
    }

    public void loginOut(){
        WeeverApplication.setCurUser(null)    ;
    }
}
