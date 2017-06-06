package cn.people.weever.net;

import cn.people.weever.model.Driver;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/4/8.
 */

public  interface DriverApiService {

    //用户登录事件
    public static final int TO_USER_LOGIN                 =   1  ;
    @GET("//")
    Call<BaseModel<Driver>> login(@Body RequestBody route);
}
