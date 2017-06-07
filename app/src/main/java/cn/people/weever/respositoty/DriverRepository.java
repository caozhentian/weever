package cn.people.weever.respositoty;

import cn.people.weever.MockData.MockLoginService;
import cn.people.weever.MockData.MockService;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.model.Driver;
import cn.people.weever.net.BaseCallback;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.net.RequestBodyCreator;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DriverRepository extends RespoisitoryBase {
    protected DriverApiService mDriverApiService ;

    public DriverRepository() {
        super() ;
        mDriverApiService  = mRetrofit.create(DriverApiService.class);
    }

    public void login(Driver driver){
       if(MockService.DEBUG_MOCK){
           MockLoginService mockLoginService = new MockLoginService() ;
           mockLoginService.getJsonData() ;
           return ;
       }
        Call<BaseModel<Driver>> call   =  mDriverApiService.login(RequestBodyCreator.CreateRequestBodyJSON(driver)) ;
        call.enqueue(new BaseCallback<Driver>(DriverApiService.TO_USER_LOGIN));
    }

    public void autoLogin(){
        Driver driver = WeeverApplication.getCurUser() ;
        if(driver == null){
            return ;
        }
        Call<BaseModel<Driver>> call   =  mDriverApiService.autoLogin(driver.getToken()) ;
        call.enqueue(new BaseCallback<Driver>(DriverApiService.TO_USER_AUTO_LOGIN))      ;
    }

    public void loginOut(){
        Driver driver = WeeverApplication.getCurUser() ;
        if(driver == null){
            return ;
        }
        Call<BaseModel<Object>> call   =  mDriverApiService.loginOut(driver.getUserName()) ;
        call.enqueue(new BaseCallback<Object>(DriverApiService.TO_USER_LOGIN_OUT)) ;
    }

}
