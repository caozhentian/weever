package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import java.net.SocketTimeoutException;

import cn.people.weever.R;
import cn.people.weever.activity.login.LoginActivity;
import cn.people.weever.application.ActivityExitManage;
import cn.people.weever.dialog.CustomProgressDialog;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.event.NetRequestPostEvent;
import cn.people.weever.event.NetRequestPreEvent;
import cn.people.weever.net.APIError;
import cn.people.weever.net.APIFail;
import cn.people.weever.net.BaseModel;

/**
 * Created by ztcao on 2016/12/20. Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity{

	protected CustomProgressDialog mCustomProgressDialog;
	public abstract void initData();
	public abstract void initView();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ActivityExitManage.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityExitManage.removeActivity(this);
	}

	public void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	public void showToast(int res){
		Toast.makeText(this, getResources().getText(res), Toast.LENGTH_SHORT).show();
	}

	public void processNetRequestPreEvent(@Nullable NetRequestPreEvent netRequestPreEvent){
		mCustomProgressDialog = new CustomProgressDialog(this , R.style.progress_dialog) ;
		mCustomProgressDialog.show();
	}

	public void processNetRequestPostEvent(@Nullable NetRequestPostEvent netRequestPostEvent){
		mCustomProgressDialog.cancel();
	}


	public void processSuccessEvent(@NonNull BaseModel baseModel) {
		if(baseModel.isSuccess()){
			showToast(baseModel.getMessage());
			dealSuccess(baseModel) ;
		}
		else{
			showToast(baseModel.getMessage());
			return ;
		}

	}

	public void processErrorEvent(@NonNull APIError apiErrorError) {
		if(apiErrorError.getThrowable() instanceof SocketTimeoutException){
			Toast.makeText(this,"网络连接超时。请检查网络",Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
		}
	}

	public void processFailEvent(@NonNull APIFail apiFail) {
		if(apiFail.getCode() == BaseModel.SUB_FAIL_STATUS_TOKEN_EXPIRE){
			showTokenExpireDialog() ;
		}
		else {
			Toast.makeText(this, apiFail.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 *
	 * @param baseModel
	 */
	protected void dealSuccess(BaseModel baseModel){

	}

	public void showTokenExpireDialog(){
		OKCancelDlg.createCancelOKDlg(this, "登录超时", "重新登录", "退出应用", new ICancelOK() {
			@Override
			public void cancel() {
				ActivityExitManage.finishAll();
			}

			@Override
			public void ok() {
				ActivityExitManage.finishAll();
                startActivity(LoginActivity.newIntent(BaseActivity.this));
			}
		});
	}
}
 