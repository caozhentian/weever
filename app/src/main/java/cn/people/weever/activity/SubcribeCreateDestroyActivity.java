package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ztcao on 2016/12/20. create / onDestroy 事件订阅的生命周期
 */
public abstract class SubcribeCreateDestroyActivity extends BaseActivity{

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
 