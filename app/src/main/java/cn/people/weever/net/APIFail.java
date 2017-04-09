package cn.people.weever.net;

public class APIFail {

	
	public APIFail(int code,String message){
		this.code =  code;
		this.message = message;
	}
	
	public APIFail(int todo_code,int code,String message){
		this.code =  code;
		this.message = message;
		this.todo_code = todo_code;
	}
	private int code;
	private int todo_code;
	private String message;

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTodo_code() {
		return todo_code;
	}
	public void setTodo_code(int todo_code) {
		this.todo_code = todo_code;
	}
	
}
