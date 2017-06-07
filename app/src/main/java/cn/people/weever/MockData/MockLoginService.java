package cn.people.weever.MockData;

import cn.people.weever.model.Driver;
import cn.people.weever.net.DriverApiService;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockLoginService extends MockService {


    @Override
    public MockResponse getJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        Driver driver = new Driver() ;
        driver.setUserName("Demo");
        driver.setToken("dfdfdffda2434");
        mockResponse.getModel().setData(driver);
        MockBaseCallback<Driver> mockBaseCallback = new  MockBaseCallback<Driver>(DriverApiService.TO_USER_LOGIN, mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }

    public MockResponse getAutologinJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        Driver driver = new Driver() ;
        driver.setUserName("Demo");
        driver.setToken("dfdfdffda2434");
        mockResponse.getModel().setData(driver);
        MockBaseCallback<Driver> mockBaseCallback = new  MockBaseCallback<Driver>(DriverApiService.TO_USER_AUTO_LOGIN, mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }

    public MockResponse getloginOutJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        Driver driver = new Driver() ;
        driver.setUserName("Demo");
        driver.setToken("dfdfdffda2434");
        mockResponse.getModel().setData(driver);
        MockBaseCallback<Driver> mockBaseCallback = new  MockBaseCallback<Driver>(DriverApiService.TO_USER_LOGIN_OUT, mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }
}
