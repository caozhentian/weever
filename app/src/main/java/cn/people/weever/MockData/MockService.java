package cn.people.weever.MockData;


import cn.people.weever.net.BaseModel;

public abstract class MockService {

	public static final boolean DEBUG_MOCK = false ;

	public  abstract MockResponse getJsonData();
	
	public MockResponse getSuccessResponse() {
		MockResponse mockResponse = new MockResponse();
		mockResponse.setSuccess(true);
		mockResponse.setCode(200);
		mockResponse.setMessage("success");
		BaseModel baseModel = new BaseModel() ;
		baseModel.setStatus(BaseModel.SUCCESS);
		baseModel.setMessage("操作成功");
		baseModel.setData(baseModel);
		mockResponse.setModel(baseModel);
		return mockResponse;
	}

	public MockResponse getFailResponse(int errorType, String errorMessage) {
		MockResponse mockResponse = new MockResponse();
		mockResponse.setSuccess(false);
		mockResponse.setCode(501);
		mockResponse.setMessage("failse");
		return mockResponse;
	}
}
