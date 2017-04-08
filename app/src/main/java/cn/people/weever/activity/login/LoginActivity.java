package cn.people.weever.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtUserName)
    EditText mEdtUserName;
    @BindView(R.id.edtPassword)
    EditText mEdtPassword;
    @BindView(R.id.spnCarNum)
    Spinner mSpnCarNum;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked() {

    }
}
