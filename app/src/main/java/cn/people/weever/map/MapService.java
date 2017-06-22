package cn.people.weever.map;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/6/22.
 */

public class MapService {
    public static void initMap(Context context){
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(context);
    }
}
