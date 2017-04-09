package cn.people.weever.net;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ztcao on 2017/2/4.
 */

public class BaseCallback<T> implements Callback<BaseModel<T>> {

    public static final String TAG = "BaseCallback" ;

    //区分是哪个网络接口的请求
    private int mApiOperationCode ;

    public BaseCallback() {
    }

    public BaseCallback(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }


    @Override
    public void onResponse(Call<BaseModel<T>> call, Response<BaseModel<T>> response) {
        if (response.isSuccessful() && response.errorBody() == null) {
            BaseModel<T> model = response.body();
            if (model == null) {
                Log.e(TAG, "数据解析出现异常");
                postEvent(new APIFail(mApiOperationCode ,response.code(), response.message()));
                return ;
            } else {
                Log.d(TAG, "成功----------------");
            }
            model.setApiOperationCode(mApiOperationCode);
            postEvent(model);
        } else { //an application-level failure such as a 404 or 500
            postEvent(new APIFail(mApiOperationCode ,response.code(), response.message()));
            Log.e(TAG, response.code() + "");
            Log.e(TAG, response.message() + "");
        }
    }

    @Override
    public void onFailure(Call call, Throwable throwable) { //网络异常
        Log.e(TAG, "onFailure            " + throwable.getMessage());
        throwable.printStackTrace();
        postEvent(new APIError(mApiOperationCode, throwable));
    }

    private void postEvent(Object model) {
        EventBus.getDefault().post(model);
    }
}
