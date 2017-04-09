package cn.people.weever.respositoty;

import cn.people.weever.net.CarApiService;
import cn.people.weever.service.CarService;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CarRespository extends RespoisitoryBase {
    protected  CarApiService mCarApiService ;
    public CarRespository() {
        super();
        mCarApiService = mRetrofit.create(CarApiService.class) ;
    }
}
