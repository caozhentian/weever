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
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
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

import butterknife.ButterKnife;
import cn.people.weever.R;
import cn.people.weever.activity.order.list.MyOrdersActivity;
import cn.people.weever.activity.poi.AddressSelectVM;
import cn.people.weever.activity.setting.SettingUpActivity;
import cn.people.weever.application.StartExitAppManager;
import cn.people.weever.common.util.NavUtils;
import cn.people.weever.fragment.NavFooterFragment;
import cn.people.weever.fragment.NavHeadFragment;
import cn.people.weever.map.LocationService;
import cn.people.weever.map.TLocationListener;
import cn.people.weever.mapapi.overlayutil.CityConstant;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RouteOperateEvent;

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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        mLocationService.registerListener(myListener);
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
        initNavi();
    }

    @Override
    public void initView() {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
