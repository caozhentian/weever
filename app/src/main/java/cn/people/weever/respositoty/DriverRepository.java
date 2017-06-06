package cn.people.weever.respositoty;

import cn.people.weever.MockData.MockLoginService;
import cn.people.weever.MockData.MockService;
import cn.people.weever.activity.login.LoginViewModel;
import cn.people.weever.net.DriverApiService;

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
       if(MockService.DEBUG_MOCK){
           MockLoginService mockLoginService = new MockLoginService() ;
           mockLoginService.getJsonData() ;
       }
    }


}
