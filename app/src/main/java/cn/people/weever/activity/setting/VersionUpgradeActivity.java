package cn.people.weever.activity.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.people.weever.R;
import cn.people.weever.activity.SubcribeCreateDestroyActivity;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.ManifestUtil;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.UpdateApkApi;
import cn.people.weever.service.UpdateService;
import cn.people.weever.update.UpdateAPKModel;
import cn.people.weever.update.UpdateManager;


/**
 *  版本更新
 * Created by Administrator on 2017/1/10 0010.
 */

public class VersionUpgradeActivity extends SubcribeCreateDestroyActivity implements View.OnClickListener{

    private ImageView iv_back;
    private TextView  tv_title,tv_version_code,tv_package_size,tv_update;
    private UpdateAPKModel versionUpdating;
    private RelativeLayout  rl_update;
    private UpdateService mUpdateService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_verdion_upgrade);
        initData();
        initView();
    }

    @Override
    public void initData() {
        mUpdateService = new UpdateService(this) ;
    }

    @Override
    public void initView() {
        iv_back = (ImageView)findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_package_size = (TextView) findViewById(R.id.tv_package_size);
        tv_update = (TextView) findViewById(R.id.tv_update);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        iv_back.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_title.setText("版本");
        tv_version_code.setText("版本号：V"+ ManifestUtil.getVersionName(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back: // 返回
                this.finish();
                break;
            case R.id.tv_update: //  更新
                mUpdateService.getUpdateApkInfo();
                break;
        }
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
    // 版本更新
    private void update(){
        UpdateManager  updateManager = new UpdateManager(this , versionUpdating) ;
        updateManager.update();
    }

    protected<T> void dealSuccess(BaseModel baseModel){

        if(baseModel.getApiOperationCode() == UpdateApkApi.TO_UPDATE_APK_NET_REQUEST){
            UpdateAPKModel updateAPKModel = (UpdateAPKModel) baseModel.getData();
            if (versionUpdating.getVersionCode()> getVerCode()){
                    update();
            }else if (versionUpdating.getVersionCode() <= getVerCode()){
                    showToast("已是最新版本，无需更新");
            }
        }

    }
}
