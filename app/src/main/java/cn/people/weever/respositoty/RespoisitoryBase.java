package cn.people.weever.respositoty;

import cn.people.weever.net.RetrofitFactory;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/4/8.
 */

public class RespoisitoryBase {
    protected Retrofit mRetrofit ;

    public RespoisitoryBase() {
        mRetrofit = RetrofitFactory.getBaseRetrofit();
    }
}
