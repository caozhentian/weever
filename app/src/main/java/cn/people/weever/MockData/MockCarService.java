package cn.people.weever.MockData;

import java.util.LinkedList;
import java.util.List;

import cn.people.weever.model.Car;
import cn.people.weever.net.CarApiService;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockCarService extends MockService {
    @Override
    public MockResponse getJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        List<Car> carList = new LinkedList<>() ;
        Car car = new Car() ;
        car.setDescription("heh");
        car.setNum("陕A00000");
        carList.add(car);
        car = new Car() ;
        car.setDescription("heh2");
        car.setNum("陕A11111");
        carList.add(car);
        car = new Car() ;
        car.setDescription("heh3");
        car.setNum("A0003");
        carList.add(car);
        mockResponse.getModel().setData(carList);
        MockBaseCallback<List<Car>> mockBaseCallback = new  MockBaseCallback<List<Car>>(CarApiService.TO_CAR, mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }
}
