package cn.people.weever.service;

import cn.people.weever.application.WeeverApplication;
import cn.people.weever.model.Driver;

/**
 * Created by Administrator on 2017/6/7.
 */

public class TokenService {

    public static final String getToken(){
        String token = "";
        Driver driver = WeeverApplication.getCurUser() ;
        if(driver != null){
            token = driver.getToken();
        }
        return token ;
    }
}
