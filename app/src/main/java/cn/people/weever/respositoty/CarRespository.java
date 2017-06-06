package cn.people.weever.respositoty;

import java.util.List;

import cn.people.weever.model.Car;
import cn.people.weever.net.BaseCallback;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.CarApiService;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CarRespository extends RespoisitoryBase {
    protected  CarApiService mCarApiService ;
    public CarRespository() {
        super();
        mCarApiService = mRetrofit.create(CarApiService.class) ;
    }

    public void getCar(){
        Call<BaseModel<List<Car>>> call   =  mCarApiService.getCar() ;
        call.enqueue(new BaseCallback<List<Car>>(CarApiService.TO_CAR));
    }
}
