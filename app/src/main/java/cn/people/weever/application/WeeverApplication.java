package cn.people.weever.application;

import android.app.Application;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.model.BaseRequest;
import com.facebook.stetho.Stetho;

import java.util.concurrent.atomic.AtomicInteger;

import cn.people.weever.BuildConfig;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.db.DaoManager;
import cn.people.weever.map.LocationService;
import cn.people.weever.map.TraceService;
import cn.people.weever.model.Driver;
import cn.people.weever.model.TripNode;

/**
 * Created by ztcao on 2016/12/20.
 */

public class WeeverApplication extends Application {

    public static final String CUR_TRIP_NODE = "CurTripNode" ;

    private static WeeverApplication sWeeverApplication ;

    private TripNode mCurTripNode          ;

    private static Driver sCurDriver        ;

    //private static DaoSession sDaoSession ;

    public static int screenWidth = 0;

    public static int screenHeight = 0;

    private static  AtomicInteger mSequenceGenerator = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
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

    private void initMap(){
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        LocationService locationService = LocationService.getLocationService(this);
        SDKInitializer.initialize(this);
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public static  void initRequest(BaseRequest request) {
       TraceService.getInstance(WeeverApplication.getInstance()).initRequest(request);
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public static  int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    public static String getEntityName() {
//        return entityName; TODO TESTCODE
        return  "myTrace" ;
    }

    public static void setEntityName(String entityName) {
        TraceService.getInstance(WeeverApplication.getInstance()).setEntityName(entityName);
    }

    public TripNode getCurTripNode() {
        if(mCurTripNode == null){
            mCurTripNode = PreferencesUtil.getPreferences(this , CUR_TRIP_NODE) ;
        }
        return mCurTripNode;
    }

    public void setCurTripNode(TripNode curTripNode) {
        mCurTripNode = curTripNode;
        PreferencesUtil.setPreferences(this ,CUR_TRIP_NODE ,  curTripNode);
    }
}
