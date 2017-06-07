package cn.people.weever.net;

import android.text.TextUtils;

import java.io.IOException;

import cn.people.weever.service.TokenService;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/7.
 */

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (TextUtils.isEmpty(TokenService.getToken())) {
            return chain.proceed(originalRequest);
        }
        Request authorised = originalRequest.newBuilder()
                .header("Token", TokenService.getToken())
                .build();
        return chain.proceed(authorised);
    }
}
