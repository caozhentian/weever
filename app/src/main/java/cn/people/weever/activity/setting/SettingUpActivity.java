package cn.people.weever.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.people.weever.R;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.activity.login.LoginActivity;
import cn.people.weever.application.ActivityExitManage;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.ManifestUtil;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.model.VersionUpdating;

/**
 *  设置
 * Created by Administrator on 2017/1/10 0010.
 */

public class SettingUpActivity extends SubcribeCreateDestroyActivity implements View.OnClickListener{

    private ImageView  img_back ;
    private  TextView  tv_title,tv_versio_update;
    private RelativeLayout  rl_version_upgrade,rl_about_we;
    private  TextView   tv_log_out;
    //private LogOutService logOutService;
    private LinearLayout  ll_setting;
    private VersionUpdating versionUpdating;

    public static final Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext , SettingUpActivity.class);
        return intent ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        initView();
        initData();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_version_upgrade = (RelativeLayout) findViewById(R.id.rl_version_upgrade);
        rl_about_we = (RelativeLayout) findViewById(R.id.rl_about_we);
        tv_log_out = (TextView)findViewById(R.id.tv_log_out);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        tv_versio_update = (TextView) findViewById(R.id.tv_versio_update);

        rl_version_upgrade.setOnClickListener(this);
        rl_about_we.setOnClickListener(this);
        tv_log_out.setOnClickListener(this);
        tv_title.setText("设置");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.img_back: // 返回
                this.finish();
                break;
            case R.id.rl_version_upgrade: // 版本更新
                intent.putExtra("versionUpdating",versionUpdating);
                intent.setClass(SettingUpActivity.this,VersionUpgradeActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_we: // 关于我们
                intent.setClass(SettingUpActivity.this,AboutWeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_log_out: //  退出
                logout();
                break;
        }
    }


    // t退出弹出框
    private void logout(){
        OKCancelDlg.createCancelOKDlg(this, "确定退出登录吗?", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                WeeverApplication.exitLogin();
                ActivityExitManage.finishAll();
                startActivity(LoginActivity.newIntent(SettingUpActivity.this));
            }
        });

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 获取软件版本号
     * @return
     */
    public static int getVerCode() {
        return ManifestUtil.getVersionCode(WeeverApplication.getInstance()) ;
    }
    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        return ManifestUtil.getVersionName(WeeverApplication.getInstance()) ;
    }
}
