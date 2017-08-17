package cn.people.weever.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import cn.people.weever.R;
import cn.people.weever.activity.login.ClockInActivity;
import cn.people.weever.activity.order.list.MyOrdersActivity;
import cn.people.weever.activity.poi.AddressSelectVM;
import cn.people.weever.activity.setting.SettingUpActivity;
import cn.people.weever.application.StartExitAppManager;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.NavUtils;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.fragment.NavFooterFragment;
import cn.people.weever.fragment.NavHeadFragment;
import cn.people.weever.map.LocationService;
import cn.people.weever.map.TLocationListener;
import cn.people.weever.map.TraceService;
import cn.people.weever.mapapi.overlayutil.CityConstant;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.Driver;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.DriverApiService;
import cn.people.weever.service.DriverService;

public class HomeActivity extends SubcribeCreateDestroyActivity implements NavigationView.OnNavigationItemSelectedListener
        , NavFooterFragment.OnFragmentInteractionNavFooterListener{

    public static final String ARG_BASE_ORDER = "baseorder" ;

    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    public NavLocationListener myListener = new NavLocationListener();

    // 定位相关
    LocationService mLocationService;
    MapView mMapView;
    BaiduMap mBaiduMap;

    private MyLocationConfiguration.LocationMode mCurrentMode;

    private LatLng srcLating ;
    private LatLng destLating ;
    private String mSrcAddress    ;
    private String mDestAddress   ;

    private NavFooterFragment mNavFooterFragment ;

    private BaseOrder mBaseOrder ;
    private boolean isSelectSrcAddress;

    private NavigationView navigationView ;
    private TextView txt_name ;
    private TextView txt_telphone ;
    private TextView txt_car_num;


    public static final Intent newIntent(Context packageContext, BaseOrder baseOrder){
        Intent intent = new Intent(packageContext ,HomeActivity.class ) ;
        intent.putExtra(ARG_BASE_ORDER , baseOrder) ;
        return intent ;
    }
    public static final Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext ,HomeActivity.class ) ;
        return intent ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        initView();
        initData();

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,null);
        mBaiduMap.setMyLocationConfigeration(configuration);
        MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(CityConstant.GEO_XIAN);

        mBaiduMap.setMapStatus(u2);
        mBaiduMap.setTrafficEnabled(true) ;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationService = LocationService.getLocationService(this);
		BitmapDescriptor currentMarker = BitmapDescriptorFactory
                           .fromResource(R.drawable.ic_map_location);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, currentMarker,
                        accuracyCircleFillColor, accuracyCircleStrokeColor));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            StartExitAppManager.exitApp(HomeActivity.this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void initData() {
        DriverService driverService = new DriverService() ;
        driverService.autoLogin();
        initNavi();
    }

    @Override
    public void initView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_home);
        txt_name = (TextView) headerLayout.findViewById(R.id.txt_name);
        txt_name.setText(WeeverApplication.getCurUser().getName());
        txt_telphone = (TextView) headerLayout.findViewById(R.id.txt_telphone);
        txt_telphone.setText(WeeverApplication.getCurUser().getUserName());
        txt_car_num    = (TextView) headerLayout.findViewById(R.id.txt_car_num);
        txt_car_num.setText(PreferencesUtil.getStringPreferences(WeeverApplication.getInstance() , "CAR_KEY"));
        mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_BASE_ORDER);
      initFragment() ;
    }

    private void initFragment(){
        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if(fragmentManager.findFragmentById(R.id.fl_nav_footer) == null){

            mNavFooterFragment =  NavFooterFragment.newInstance(mBaseOrder);
            fragmentTransaction.add(R.id.fl_nav_footer , mNavFooterFragment) ;
            fragmentTransaction.commit();
        }
        else{
            mNavFooterFragment =  NavFooterFragment.newInstance(mBaseOrder);
            fragmentTransaction.replace(R.id.fl_nav_footer , mNavFooterFragment) ;
            fragmentTransaction.commit();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nav) {
            if(srcLating == null || destLating == null){
                return true;
            }
            routeplanToNavi() ;
        }
        else if(id == R.id.nav_gallery){
            startActivity(MyOrdersActivity.newIntent(this));
        }
        else if(id == R.id.nav_manage){
            startActivity(SettingUpActivity.newIntent(this));
        }
        else if(id == R.id.nav_home){

        }
        else if(id == R.id.nav_clock_in){
           startActivity(ClockInActivity.newIntent(this));
        }
        else if(id == R.id.nav_punch_out){
            punchOut() ;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void punchOut(){
        OKCancelDlg.createCancelOKDlg(this, getString(R.string.work_punch_out)+"?", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
              DriverService driverService =  new DriverService() ;
              driverService.punchOut();
            }
        });
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        mLocationService.unregisterListener(myListener);
        mLocationService.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        // 退出时销毁定位
        mLocationService.registerListener(myListener);
        mLocationService.start();
        super.onResume();
        txt_car_num.setText(PreferencesUtil.getStringPreferences(WeeverApplication.getInstance() , "CAR_KEY"));
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(RouteOperateEvent routeOperateEvent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if(fragmentManager.findFragmentById(R.id.fl_nav_head) == null){
            fragmentTransaction.add(R.id.fl_nav_head , NavHeadFragment.newInstance(mBaseOrder)) ;
            fragmentTransaction.commit();
        }
        else{
            fragmentTransaction.replace(R.id.fl_nav_head , NavHeadFragment.newInstance(mBaseOrder)) ;
            fragmentTransaction.commit();
        }
    }

    public class NavLocationListener extends TLocationListener {

        @Override
        public void process(BDLocation location) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            srcLating = ll;
            List<Poi> poiList = location.getPoiList() ;
            //使用街道
            if(poiList != null && poiList.size() > 1) {
                Poi poi = poiList.get(0);
                if(!isSelectSrcAddress ) {
                    mNavFooterFragment.setLocationSrc(poi.getName());
                }
            }

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

        @Override
        public void processFirstLoc(BDLocation location) {

        }

        @Override
        public void processLocFail(BDLocation location) {

        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void process(AddressSelectVM o){
        if(o.isSrc()){
            isSelectSrcAddress = true ;
            mSrcAddress = (o.getmPoiInfo().name)   ;
            srcLating = o.getmPoiInfo().location   ;
        }
        else{
            mDestAddress = (o.getmPoiInfo().name)  ;
            destLating = o.getmPoiInfo().location  ;
        }
    }

    private void routeplanToNavi() {
        NavUtils.routeplanToNavi(this , srcLating , mSrcAddress ,destLating ,  mDestAddress , mBaseOrder) ;
    }


    private void initNavi() {
        NavUtils.initNavi(this) ;
    }

    @Override
    protected<T> void dealSuccess(BaseModel baseModel){
        if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_PUNCH_OUT){
            showToast(baseModel.getMessage());
            //停止Trace 跟踪
            TraceService.getInstance(this).stopTrace();
            PreferencesUtil.setPreferences(WeeverApplication.getInstance(),"CAR_KEY",null);
            txt_car_num.setText(null);
        }
        else if(baseModel.getApiOperationCode() == DriverApiService.TO_USER_AUTO_LOGIN){
            Driver driver = (Driver) baseModel.getData();
            if(!TextUtils.isEmpty(driver.getCardNum())){
                PreferencesUtil.setPreferences(WeeverApplication.getInstance(),"CAR_KEY",driver.getCardNum());
                //先停止
                TraceService.getInstance(this).stopTrace();
                TraceService.getInstance(this).startTrace(null);
                TraceService.getInstance(WeeverApplication.getInstance()).setEntityName(driver.getCardNum())  ;
            }
        }

    }

}
