package cn.people.weever.respositoty;

import cn.people.weever.activity.login.LoginViewModel;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.service.CarService;
import cn.people.weever.service.DriverService;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DriverRepository extends RespoisitoryBase {
    protected DriverApiService mDriverApiService ;
    public DriverRepository() {
        super() ;
        mDriverApiService  = mRetrofit.create(DriverApiService.class);
    }

    public void login(LoginViewModel loginViewModel){

    }
}
