package cn.people.weever.application;

import android.app.Application;
import android.util.DisplayMetrics;

import cn.people.weever.BuildConfig;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.db.DaoManager;
import cn.people.weever.model.Driver;

/**
 * Created by ztcao on 2016/12/20.
 */

public class WeeverApplication extends Application {

    private static WeeverApplication sWeeverApplication ;

    private static Driver sCurDriver        ;

    public static int screenWidth = 0;

    public static int screenHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        if(BuildConfig.DEBUG){
            //Stetho.initializeWithDefaults(this);
        }
        sWeeverApplication = this ;
        getScreenSize() ;
        StartExitAppManager.initApp(this);
        initdb();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

    /**after onCreate ,call this method
     * @return
     */
    public static WeeverApplication getInstance(){
        return sWeeverApplication ;
    }

    /**
     * 得到当前登录的用户
     * @return
     */
    public static Driver getCurUser(){
        if(sCurDriver != null)
            return sCurDriver ;
        sCurDriver  =  PreferencesUtil.getPreferences(sWeeverApplication , Driver.USER_KEY) ;
//        QueryBuilder<Driver> driverQueryBuilder = sDaoSession.getDriverDao().queryBuilder();
//        List<Driver> driverList = driverQueryBuilder.limit(1).list() ;
//        if(driverList != null && driverList.size() > 0){
//            sCurDriver =  driverList.get(0) ;
//        }
        return sCurDriver ;
    }

    public static void setCurUser(Driver curUser) {
        //内存信息 持久化信息
        WeeverApplication.sCurDriver = curUser;
        PreferencesUtil.setPreferences(sWeeverApplication,Driver.USER_KEY,curUser);
//        if(curUser == null) {
//            sDaoSession.getDriverDao().deleteAll();
//        }
//        else{
//            sDaoSession.insertOrReplace(curUser) ;
//        }
    }

    private void initdb(){
        DaoManager.getInstance() ;
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

}
