package cn.people.weever.MockData;

import cn.people.weever.model.Driver;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockLoginService extends MockService {


    @Override
    public MockResponse getJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        Driver driver = new Driver() ;
        driver.setToken("dfdfdffda2434");
        mockResponse.getModel().setData(driver);
        MockBaseCallback<Driver> mockBaseCallback = new  MockBaseCallback<Driver>(1 , mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }
}
