package cn.people.weever.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.dialog.CustomProgressDialog;
import cn.people.weever.model.VersionUpdating;

/**
 *  设置
 * Created by Administrator on 2017/1/10 0010.
 */

public class SettingUpActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back;
    private TextView  tv_title,tv_versio_update;
    private RelativeLayout  rl_version_upgrade,rl_about_we,rl_touch_we;
    private  TextView   tv_log_out;
    //private LogOutService logOutService;
    private CustomProgressDialog customProgressDialog;
    private PopupWindow synthesize_popwindow;
    private LinearLayout  ll_setting;
    private VersionUpdating versionUpdating;
   // private VersionUpdatingService versionUpdatingService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_version_upgrade = (RelativeLayout) findViewById(R.id.rl_version_upgrade);
        rl_about_we = (RelativeLayout) findViewById(R.id.rl_about_we);
        tv_log_out = (TextView)findViewById(R.id.tv_log_out);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        tv_versio_update = (TextView) findViewById(R.id.tv_versio_update);

        iv_back.setOnClickListener(this);
        rl_version_upgrade.setOnClickListener(this);
        rl_about_we.setOnClickListener(this);
        rl_touch_we.setOnClickListener(this);
        tv_log_out.setOnClickListener(this);
        tv_title.setText("设置");
        customProgressDialog = new CustomProgressDialog(this,R.style.progress_dialog);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.iv_back: // 返回
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

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 获取软件版本号
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo("com.threeti.yihaoshijie", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg",e.getMessage());
        }
        return verCode;
    }
    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.threeti.yihaoshijie", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg",e.getMessage());
        }
        return verName;
    }
}
