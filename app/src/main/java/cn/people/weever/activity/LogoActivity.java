package cn.people.weever.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.people.weever.R;
import cn.people.weever.activity.login.LoginActivity;
import cn.people.weever.application.WeeverApplication;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logo);
        if(WeeverApplication.getCurUser() == null) { //需要登录
            startActivity(LoginActivity.newIntent(this));
        }
        else{ //自动登录
            startActivity(HomeActivity.newIntent(this));
        }
        finish();
    }
}
