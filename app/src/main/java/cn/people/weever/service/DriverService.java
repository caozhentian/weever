package cn.people.weever.service;

import android.text.TextUtils;

import cn.people.weever.application.WeeverApplication;
import cn.people.weever.model.Driver;
import cn.people.weever.respositoty.DriverRepository;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DriverService {

    private DriverRepository mDriverRepository ;

    public DriverService() {
        mDriverRepository = new DriverRepository() ;
    }

    public void setDriverRepository(DriverRepository driverRepository) {
        mDriverRepository = driverRepository;
    }

    public void login(Driver driver){
        if(driver == null){
            throw new IllegalArgumentException("参数值为null") ;
        }
        if(TextUtils.isEmpty(driver.getUserName())){
            throw new IllegalArgumentException("请输入用户名") ;
        }
        if(TextUtils.isEmpty(driver.getPassword())){
            throw new IllegalArgumentException("请输入密码") ;
        }
        mDriverRepository.login(driver);
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
