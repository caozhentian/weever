package cn.people.weever.respositoty;

import cn.people.weever.net.BaseCallback;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.UpdateApkApi;
import cn.people.weever.update.UpdateAPKModel;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UpdateApkRespository extends RespoisitoryBase{

    private UpdateApkApi mUpdateApkApi ;

    public UpdateApkRespository() {
        super();
        mUpdateApkApi =  mRetrofit.create(UpdateApkApi.class);
    }

    public void getUpdateApkInfo() {
        Call<BaseModel<UpdateAPKModel>> call   =  mUpdateApkApi.getUpdateApkInfo();
        call.enqueue(new BaseCallback<UpdateAPKModel>(UpdateApkApi.TO_UPDATE_APK_NET_REQUEST));
    }
}
