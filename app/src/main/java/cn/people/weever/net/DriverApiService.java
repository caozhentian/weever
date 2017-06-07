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
    @POST("driver/login")
    Call<BaseModel<Driver>> login(@Body RequestBody route);

    @FormUrlEncoded
    @POST("driver/autoLogin")
    Call<BaseModel<Driver>> autoLogin(@Field("token") String token);

    @FormUrlEncoded
    @POST("driver/logout")
    Call<BaseModel<Object>> loginOut(@Field("userName") String userName);
}
