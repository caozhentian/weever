package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.people.weever.event.NetRequestPostEvent;
import cn.people.weever.event.NetRequestPreEvent;
import cn.people.weever.net.APIError;
import cn.people.weever.net.APIFail;
import cn.people.weever.net.BaseModel;

/**
 * Created by ztcao on 2016/12/20. onResume / onPause 事件订阅的生命周期
 */
public abstract class SubcribeCreateDestroyActivity extends BaseActivity{

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processNetRequestPreEvent(@Nullable NetRequestPreEvent netRequestPreEvent){
		super.processNetRequestPreEvent(netRequestPreEvent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processNetRequestPostEvent(@Nullable NetRequestPostEvent netRequestPostEvent){
		super.processNetRequestPostEvent(netRequestPostEvent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processErrorEvent(@NonNull APIError apiErrorError) {
		super.processErrorEvent(apiErrorError);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processFailEvent(@NonNull APIFail apiFail) {
		super.processFailEvent(apiFail);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processSuccessEvent(@NonNull BaseModel baseModel) {
		super.processSuccessEvent(baseModel);
	}

	//网络请求前的预处理设置
	protected void preNetQuest( int ApiOperationCode){
		mApiOperationCode = ApiOperationCode ;
	}

	//网络请求后的预处理
	protected void postNetQuest(){

	}

}
 