package cn.people.weever.activity.setting;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.model.VersionUpdating;



/**
 *  版本更新
 * Created by Administrator on 2017/1/10 0010.
 */

public class VersionUpgradeActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back;
    private TextView  tv_title,tv_version_code,tv_package_size,tv_update;
    private VersionUpdating versionUpdating;
    private RelativeLayout  rl_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_verdion_upgrade);
        initData();
        initView();
    }

    @Override
    public void initData() {
        if (null !=getIntent().getSerializableExtra("versionUpdating")){
            versionUpdating = (VersionUpdating)getIntent().getSerializableExtra("versionUpdating");
        }

    }

    @Override
    public void initView() {
        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_package_size = (TextView) findViewById(R.id.tv_package_size);
        tv_update = (TextView) findViewById(R.id.tv_update);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        iv_back.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_title.setText("设置");
        tv_version_code.setText("版本号：V"+versionUpdating.getAndro_num()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back: // 返回
                this.finish();
                break;
            case R.id.tv_update: //  更新
                if (versionUpdating.getAndro_num() >= getVerCode(VersionUpgradeActivity.this)){
                    update();
                }else if (versionUpdating.getAndro_num() == getVerCode(VersionUpgradeActivity.this)){
                    showToast("已是最新版本，无需更新");
                }

                break;
        }
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
    // 版本更新
    private void update(){

    }
}
