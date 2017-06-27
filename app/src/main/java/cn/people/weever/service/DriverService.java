package cn.people.weever.service;

import android.text.TextUtils;

import cn.people.weever.MockData.MockLoginService;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.jpush.JPushService;
import cn.people.weever.map.TraceService;
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
//        if(MockLoginService.DEBUG_MOCK){
//            MockLoginService mockLoginService = new MockLoginService() ;
//            mockLoginService.getJsonData(driver) ;
//            return ;
//        }
        mDriverRepository.login(driver);
    }

    public void autoLogin(){
        if(MockLoginService.DEBUG_MOCK){
            MockLoginService mockLoginService = new MockLoginService() ;
            mockLoginService.getAutologinJsonData() ;
            return ;
        }
        mDriverRepository.autoLogin();
    }

    public void loginOut(){
//        if(MockLoginService.DEBUG_MOCK){
//            MockLoginService mockLoginService = new MockLoginService() ;
//            mockLoginService.getloginOutJsonData();
//            return ;
//        }
        mDriverRepository.loginOut();
    }

    public void save(Driver driver){
        if(driver == null){
            return ;
        }
        WeeverApplication.setCurUser(driver)                  ;
        TraceService.getInstance(WeeverApplication.getInstance()).setEntityName(driver.getCardNum())  ;
        JPushService.setAlias(WeeverApplication.getInstance() , driver.getUserName());
    }

    public Driver getCurDriver(){
        return WeeverApplication.getCurUser() ;
    }

}
