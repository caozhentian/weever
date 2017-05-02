package cn.people.weever.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.people.weever.R;
import cn.people.weever.activity.login.LoginActivity;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logo);
        startActivity(LoginActivity.newIntent(this));
        finish();
    }
}
