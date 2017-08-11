package cn.people.weever.net;

import cn.people.weever.model.Driver;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/4/8.
 */

public  interface DriverApiService {

    //用户登录事件
    public static final int TO_USER_LOGIN                       =   1  ;
    public static final int TO_USER_AUTO_LOGIN                 =   2  ;
    public static final int TO_USER_LOGIN_OUT                  =   3  ;
    public static final int TO_USER_CLOCK_IN                   =   4  ;
    public static final int TO_USER_PUNCH_OUT                  =   5  ;
    @POST("driver/login")
    Call<BaseModel<Driver>> login(@Body RequestBody route);

    @POST("driver/autoLogin")
    Call<BaseModel<Driver>> autoLogin();

    @FormUrlEncoded
    @POST("driver/logout")
    Call<BaseModel<Object>>  loginOut(@Field("driverUserName") String driverUserName);

    @FormUrlEncoded
    @POST("driver/clockIn")
    Call<BaseModel<Object>>   clockIn(@Field("workTimeType")int workTimeType ,@Field("cardNum")String cardNum)  ;

    @FormUrlEncoded
    @POST("driver/punchOut")
    Call<BaseModel<Object>>   punchOut(@Field("userName")String userName)         ;


}
