package cn.people.weever.net;

import cn.people.weever.update.UpdateAPKModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/6/14.
 */

public interface UpdateApkApi {
    @GET("")
    Call<BaseModel<UpdateAPKModel>> getUpdateApkInfo() ;

}
