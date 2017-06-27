package cn.people.weever.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import cn.people.weever.R;
import cn.people.weever.activity.login.LoginActivity;
import cn.people.weever.application.StartExitAppManager;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.service.DriverService;

public class LogoActivity extends SubcribeCreateDestroyActivity {

    private DriverService mDriverService ;
    @Override
    public void initData() {
        mDriverService = new DriverService() ;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logo);
        initView();
        initData();
        if(WeeverApplication.getCurUser() == null) { //需要登录
            Logger.d("登录") ;
            startActivity(LoginActivity.newIntent(this));
            finish();
        }
        else{ //自动登录
            //startActivity(HomeActivity.newIntent(this));
            Logger.d("自动登录") ;
            mDriverService.autoLogin();
        }
    }

    public void dealSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_AUTO_LOGIN){
            startActivity(HomeActivity.newIntent(this));
            finish() ;
        }
    }

    @Override
    public void onBackPressed() {
        StartExitAppManager.exitApp(LogoActivity.this);
    }
}
