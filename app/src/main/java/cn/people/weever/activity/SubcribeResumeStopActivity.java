package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.people.weever.event.NetRequestPostEvent;
import cn.people.weever.event.NetRequestPreEvent;
import cn.people.weever.R;
import cn.people.weever.dialog.CustomProgressDialog;
import cn.people.weever.net.APIError;
import cn.people.weever.net.APIFail;

/**
 * Created by ztcao on 2016/12/20. onResume / onPause 事件订阅的生命周期
 */
public abstract class SubcribeResumeStopActivity extends BaseActivity{

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//EventBus.getDefault().register(this);
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
		mCustomProgressDialog = new CustomProgressDialog(this , R.style.progress_dialog) ;
		mCustomProgressDialog.show();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processNetRequestPostEvent(@Nullable NetRequestPostEvent netRequestPostEvent){
		mCustomProgressDialog = new CustomProgressDialog(this , R.style.progress_dialog) ;
		mCustomProgressDialog.cancel();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processErrorEvent(@NonNull APIError apiErrorError) {
		Toast.makeText(this,"无网络连接",Toast.LENGTH_SHORT).show();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void processFailEvent(@NonNull APIFail apiFail) {
		Toast.makeText(this,apiFail.getMessage(),Toast.LENGTH_SHORT).show();
	}

}
 