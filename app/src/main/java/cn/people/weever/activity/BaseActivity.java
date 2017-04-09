package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by ztcao on 2016/12/20. Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity{

	public abstract void initData();
	public abstract void initView();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	public void showToast(int res){
		Toast.makeText(this, getResources().getText(res), Toast.LENGTH_SHORT).show();
	}

}
 