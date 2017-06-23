package cn.people.weever.net;

import cn.people.weever.update.UpdateAPKModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/6/14.
 */

public interface UpdateApkApi {

    public static final int TO_UPDATE_APK_NET_REQUEST = 601;
    @GET("getUpdateVersion")
    Call<BaseModel<UpdateAPKModel>> getUpdateApkInfo() ;

}
