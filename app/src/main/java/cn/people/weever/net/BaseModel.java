package cn.people.weever.net;

/**
 * 整个数据返回的类模型
 * 
 * @author lanyan
 * 
 * @param <T>
 *            result字段的类型
 */
/**
 * @author Administrator
 *
 * @param <T>
 */
public class BaseModel<T> {
	public static final String SUCCESS = "success" ;
	public static final String FAIL     = "fail"    ;

	//fail:失败；success:成功
	protected String status ;
	//失败原因/成功描述！
	protected String message;
	protected T data;

	//接口的对应的操作码
	protected int mApiOperationCode ;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setApiOperationCode(int apiOperationCode) {
		mApiOperationCode = apiOperationCode;
	}

	public int getApiOperationCode() {
		return mApiOperationCode;
	}

	public boolean isSuccess(){
		if(status.equals(SUCCESS)){
			return true ;
		}
		else{
			return false ;
		}
	}


}
