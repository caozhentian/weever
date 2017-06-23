package cn.people.weever.net;

import java.util.List;

import cn.people.weever.model.Car;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/4/8.
 */

public  interface  CarApiService {
    //车辆编号
    public static final int TO_CAR                 =   100  ;
    @GET("car/getCar/")
    Call<BaseModel<List<Car>>> getCar();
}
