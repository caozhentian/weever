package cn.people.weever.application;

import android.app.Application;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.people.weever.BuildConfig;
import cn.people.weever.config.FileConfig;
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
	
	/**
     * 轨迹客户端
     */
    public static LBSTraceClient mClient = null;

    /**
     * 轨迹服务
     */
    public static Trace mTrace = null;

    /**
     * 轨迹服务ID
     */
    public static long serviceId = 140822;

    /**
     * Entity标识
     */
    public String entityName = "myTrace";

    public static boolean isRegisterPower = false;

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
        FileConfig.initDirs() ;
        initMap()   ;
        initTrace() ;
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

	private void initTrace(){
		mClient = new LBSTraceClient(this);
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mClient.setInterval(gatherInterval, packInterval);
        mTrace = new Trace(serviceId, entityName);
        // 初始化轨迹服务监听器
        // 开启服务
        //mClient.startTrace(mTrace, null) ;
	}

	public static final void exitApp(){
        mClient.stopTrace(mTrace ,null);
    }
    /**
     * 获取当前位置
     */
    public void getCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
//        if (NetUtil.isNetworkAvailable(mContext)
//                && trackConf.contains("is_trace_started")
//                && trackConf.contains("is_gather_started")
//                && trackConf.getBoolean("is_trace_started", false)
//                && trackConf.getBoolean("is_gather_started", false)) {
//            LatestPointRequest request = new LatestPointRequest(getTag(), serviceId, entityName);
//            ProcessOption processOption = new ProcessOption();
//            processOption.setNeedDenoise(true);
//            processOption.setRadiusThreshold(100);
//            request.setProcessOption(processOption);
//            mClient.queryLatestPoint(request, trackListener);
//        } else {
//            mClient.queryRealTimeLoc(locRequest, entityListener);
//        }
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
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public static  int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }
}
