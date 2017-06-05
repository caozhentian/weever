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

	//取值 fail:失败；success:成功
	protected String status ;

	//status值为success，如果需要 可进一步说明成功 子状态
	protected int subSuccessStatus   ;

	//status值为fail ，如果需要 可进一步说失败的子状态
	protected int subFailStatus       ;

	//失败原因或者成功描述信息！
	protected String message;
	//status值为success，成功数据，如果status值为fail ，可以返回null
	protected T data;

	//仅客户端本地使用， 接口的对应的操作码
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

	public int getSubSuccessStatus() {
		return subSuccessStatus;
	}

	public void setSubSuccessStatus(int subSuccessStatus) {
		this.subSuccessStatus = subSuccessStatus;
	}

	public int getSubFailStatus() {
		return subFailStatus;
	}

	public void setSubFailStatus(int subFailStatus) {
		this.subFailStatus = subFailStatus;
	}
}
