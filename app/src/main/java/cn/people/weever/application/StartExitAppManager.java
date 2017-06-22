package cn.people.weever.application;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.jpush.android.api.JPushInterface;
import cn.people.weever.BuildConfig;
import cn.people.weever.config.FileConfig;
import cn.people.weever.jpush.JPushService;
import cn.people.weever.model.Driver;
import cn.people.weever.service.LocationService;
import cn.people.weever.service.TraceService;

/**
 * Created by Administrator on 2017/6/7.
 */

public class StartExitAppManager {

    public static final void initApp(Context context){
        initLog() ;
        FileConfig.initDirs() ;
        initJPushService(context) ;
        initBaiduService(context) ;
    }

    public static void  initLog(){
        if(BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
    }

    public static void initBaiduService(Context context){
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(context);
        LocationService.getLocationService(context).start();
        TraceService.getInstance(context);
    }

    public static void initJPushService(Context context){

        if(BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        }
        JPushInterface.init(context);     		// 初始化 JPush
        if(WeeverApplication.getCurUser() != null){
            Driver driver = WeeverApplication.getCurUser() ;
            JPushService.setAlias(context ,driver.getUserName() );
        }
    }

    public static final void exitLogin(){
        WeeverApplication.exitLogin();
    }

    public static final void exitApp(){

    }
}
