package cn.people.weever.net;

public class APIError {

	public APIError(int todo_code, Throwable throwable) {
		this.todo_code = todo_code;
		this.throwable = throwable;
	}

	private int todo_code;
	private Throwable throwable;

	public int getTodo_code() {
		return todo_code;
	}

	public void setTodo_code(int todo_code) {
		this.todo_code = todo_code;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
