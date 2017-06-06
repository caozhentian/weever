package cn.people.weever.net;

import com.google.gson.Gson;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/6.
 */

public class RequestBodyCreator {

    public static final RequestBody CreateRequestBodyJSON(Object src){
        Gson gson = new Gson() ;
        String strEntity = gson.toJson(src) ;
        RequestBody bodyJSON = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        return bodyJSON ;
    }
}
