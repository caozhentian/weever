package cn.people.weever.MockData;


import cn.people.weever.net.BaseModel;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockResponse<T> {

    private BaseModel<T> model ;

    private boolean success ;

    public int code  ;

    public String message ;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BaseModel<T> body(){
        return model ;
    }


    public int code() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseModel<T> getModel() {
        return model;
    }

    public void setModel(BaseModel<T> model) {
        this.model = model;
    }
}
