package cn.people.weever.application;

import android.content.Context;
import android.text.TextUtils;

import cn.jpush.android.api.JPushInterface;
import cn.people.weever.BuildConfig;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.config.FileConfig;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.jpush.JPushService;
import cn.people.weever.log.LogUtil;
import cn.people.weever.map.LocationService;
import cn.people.weever.map.MapService;
import cn.people.weever.map.TraceService;
import cn.people.weever.model.Driver;

/**
 * Created by Administrator on 2017/6/7.
 */

public class StartExitAppManager {

    public static final void initApp(Context context){
        LogUtil.initLog(); ;
        FileConfig.initDirs() ;
        initJPushService(context) ;
        initBaiduService(context) ;
    }

    public static void initBaiduService(Context context){

        MapService.initMap(context);
        LocationService.getLocationService(context);


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
        WeeverApplication.getInstance().setCurUser(null);
        JPushService.setAlias(WeeverApplication.getInstance(), "");
        ActivityExitManage.finishAll();
    }

    public static final void exitApp(final Context context){
        OKCancelDlg.createCancelOKDlg(context, "确认退出应用吗?", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                ActivityExitManage.finishAll();
                TraceService.getInstance(WeeverApplication.getInstance()).stopTrace();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }
}
