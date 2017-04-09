package cn.people.weever.application;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.model.Driver;

/**
 * Created by ztcao on 2016/12/20.
 */

public class WeeverApplication extends Application {

    private static WeeverApplication sWeeverApplication ;

    private static Driver sCurDriver;
    private  boolean  isFirstLoc = true;
    public static String city_name = "";
    public static  int height;
    public static  int width;

    @Override
    public void onCreate() {
        super.onCreate();
//        if(ProductMode.DEV_MODE){
//            Stetho.initializeWithDefaults(this)      ; //for dubug
//        }
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
        sWeeverApplication = this ;
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
        Driver user  =  PreferencesUtil.getPreferences(sWeeverApplication , Driver.USER_KEY) ;
        return user ;
    }

    public static void setCurUser(Driver curUser) {
        WeeverApplication.sCurDriver = curUser;
        PreferencesUtil.setPreferences(sWeeverApplication,Driver.USER_KEY,curUser);
    }

    private void initdb(){
//        locationClient = new LocationClient(this);
//        locationClient.registerLocationListener(listener);
//        /**
//         * 设置定位参数
//         */
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); //打开GPRS
//        option.setAddrType("all");//返回的定位结果包含地址信息
//        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
//        option.setScanSpan(10000); //设置发起定位请求的间隔时间为5000ms
////        option.disableCache(false);//禁止启用缓存定位
////		option.setPoiNumber(5);    //最多返回POI个数
////		option.setPoiDistance(1000); //poi查询距离
////		option.setPoiExtraInfo(true);  //是否需要POI的电话和地址等详细信息
//
//        locationClient.setLocOption(option);
//        locationClient.start();  //	调用此方法开始定位
    }


}
