package cn.people.weever.service;

import cn.people.weever.MockData.MockCarService;
import cn.people.weever.model.Car;
import cn.people.weever.respositoty.CarRespository;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CarService extends ServiceBase<Car>{


    private CarRespository mCarRespository ;

    public CarService() {
        mCarRespository = new CarRespository()  ;
    }

    @Override
    public void query() {
        if(MockCarService.DEBUG_MOCK){
            MockCarService mockCarService = new MockCarService() ;
            mockCarService.getJsonData() ;
            return ;
        }
        mCarRespository.getCar() ;
    }
}
