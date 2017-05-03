package cn.people.weever.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.people.weever.BuildConfig;
import cn.people.weever.model.DaoMaster;
import cn.people.weever.model.DaoSession;
import cn.people.weever.model.Driver;
import cn.people.weever.service.LocationService;

/**
 * Created by ztcao on 2016/12/20.
 */

public class WeeverApplication extends Application {

    private static WeeverApplication sWeeverApplication ;

    private static Driver sCurDriver;

    private static DaoSession sDaoSession ;
    private  boolean  isFirstLoc = true;
    public static String city_name = "";
    public static  int height;
    public static  int width;

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
        }
        sWeeverApplication = this ;
        initMap() ;
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
        //Driver user  =  PreferencesUtil.getPreferences(sWeeverApplication , Driver.USER_KEY) ;
        QueryBuilder<Driver> driverQueryBuilder = sDaoSession.getDriverDao().queryBuilder();
        List<Driver> driverList = driverQueryBuilder.limit(1).list() ;
        if(driverList != null && driverList.size() > 0){
            sCurDriver =  driverList.get(0) ;
        }
        return sCurDriver ;
    }

    public static void setCurUser(Driver curUser) {
        //内存信息 持久化信息
        WeeverApplication.sCurDriver = curUser;
        //PreferencesUtil.setPreferences(sWeeverApplication,Driver.USER_KEY,curUser);
        if(curUser == null) {
            sDaoSession.getDriverDao().deleteAll();
        }
        else{
            sDaoSession.insertOrReplace(curUser) ;
        }
    }

    private void initdb(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();
    }

    private void initMap(){
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        LocationService locationService = LocationService.getLocationService(this);
        SDKInitializer.initialize(this);
    }

}
