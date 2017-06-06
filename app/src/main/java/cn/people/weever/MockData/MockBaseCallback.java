package cn.people.weever.MockData;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import cn.people.weever.event.NetRequestPostEvent;
import cn.people.weever.event.NetRequestPreEvent;
import cn.people.weever.net.APIError;
import cn.people.weever.net.APIFail;
import cn.people.weever.net.BaseModel;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockBaseCallback<T> {
    public static final String TAG = "BaseCallback" ;

    //区分是哪个网络接口的请求
    private int mApiOperationCode ;

    private MockResponse<T> mResponse ;

    public MockBaseCallback(int apiOperationCode  , MockResponse<T> response) {
        mApiOperationCode = apiOperationCode;
        mResponse = response ;
        //通知网络请求开始 UI层收到可展现进度条
        postEvent(new NetRequestPreEvent(apiOperationCode));
    }


    public void onResponse( ) {
        MockResponse<T>  response = mResponse ;
        if (response.isSuccess() ) {
            BaseModel<T> model = response.body();
            if (model == null) {
                Log.e(TAG, "数据解析出现异常");
                postEvent(new APIFail(mApiOperationCode ,response.code(), response.message()));
                //postEvent(new NetRequestPostEvent(mApiOperationCode));//网络请求结束
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
        postEvent(new NetRequestPostEvent(mApiOperationCode)); //网络请求结束
    }

    public void onFailure(Call call, Throwable throwable) { //网络异常
        Log.e(TAG, "onFailure            " + throwable.getMessage());
        throwable.printStackTrace();
        postEvent(new APIError(mApiOperationCode, throwable));
        postEvent(new NetRequestPostEvent(mApiOperationCode)); //网络请求结束
    }

    private void postEvent(Object model) {
        EventBus.getDefault().post(model);
    }
}
