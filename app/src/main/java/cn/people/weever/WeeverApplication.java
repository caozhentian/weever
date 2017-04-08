package cn.people.weever;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.people.weever.service.LocationService;

/**
 * Created by caozhentian 2017/3/31.
 */

public class WeeverApplication extends Application {
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        LocationService locationService = new LocationService(getApplicationContext());
        LocationService.setLocationService(locationService) ;
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

    }
}
