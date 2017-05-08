package cn.people.weever.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.OpenClientUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.car.MainActivity;
import cn.people.weever.activity.poi.AddressSelectVM;
import cn.people.weever.activity.poi.PoiSearchActivity;
import cn.people.weever.mapapi.overlayutil.CityConstant;
import cn.people.weever.mapapi.overlayutil.DrivingRouteOverlay;

public class HomeActivity extends   SubcribeCreateDestroyActivity implements OnGetRoutePlanResultListener
       ,NavigationView.OnNavigationItemSelectedListener {

    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    public MyLocationListener myListener = new MyLocationListener();

    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    RouteLine mRouteLine = null;
    // 定位相关
    LocationClient mLocClient;
    BitmapDescriptor mCurrentMarker;
    MapView mMapView;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    //UI相关
    @BindView(R.id.txtAllTime)
    TextView mTxtAllTime;
    @BindView(R.id.txtAllWaitTime)
    TextView mTxtAllWaitTime;
    @BindView(R.id.txtAllWaitAmount)
    TextView mTxtAllWaitAmount;
    @BindView(R.id.ll_top)
    LinearLayout mLlTop;
    @BindView(R.id.radioBtnHalfDayUse)
    RadioButton mRadioBtnHalfDayUse;
    @BindView(R.id.radioBtnDayUse)
    RadioButton mRadioBtnDayUse;
    @BindView(R.id.radioBtnTransfer)
    RadioButton mRadioBtnTransfer;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.btnStart)
    Button mBtnStart;
    @BindView(R.id.btnWait)
    Button mBtnWait;
    @BindView(R.id.btnReturn)
    Button mBtnReturn;
    @BindView(R.id.btnRestart)
    Button mBtnRestart;
    @BindView(R.id.btnCompute)
    Button mBtnCompute;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.txtAllDistance)
    TextView mTxtAllDistance;
    @BindView(R.id.edtSrc)
    TextView mEdtSrc;
    @BindView(R.id.edtDest)
    TextView mEdtDest;
    private MyLocationConfiguration.LocationMode mCurrentMode;

    private LatLng srcLating ;
    private LatLng destLating ;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(CityConstant.GEO_XIAN);
        mBaiduMap.setMapStatus(u2);
        mBaiduMap.setTrafficEnabled(true) ;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null,
                        accuracyCircleFillColor, accuracyCircleStrokeColor));
        mLocClient.setLocOption(option);
        mLocClient.start();
        initView();
        initData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void initData() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    @Override
    public void initView() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nav) {
            startNavi(srcLating , mEdtSrc.getEditableText().toString() ,
                      destLating , mEdtDest.getEditableText().toString()) ;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @OnClick({R.id.btnStart, R.id.btnWait, R.id.btnReturn, R.id.btnRestart, R.id.btnCompute})
    public void onViewClickedOperate(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                start();
                break;
            case R.id.btnWait:
                break;
            case R.id.btnReturn:
                break;
            case R.id.btnRestart:
                break;
            case R.id.btnCompute:
                break;
        }
    }

    private void start(){
        String startNodeStr = mEdtSrc.getText().toString()   ;
        String endNodeStr   = mEdtDest.getText().toString() ;
        if(TextUtils.isEmpty(startNodeStr)){
            showToast("出发地不能为空");
            return ;
        }
        if(TextUtils.isEmpty(endNodeStr)){
            showToast("目的地不能为空");
            return ;
        }
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        //startNodeStr = "科技六路" ;
        //endNodeStr   = "金宇蓝苑" ;
        PlanNode stNode = PlanNode.withLocation(srcLating)   ;
        PlanNode enNode = PlanNode.withLocation(destLating)  ;
        mBaiduMap.clear();
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
        mLlTop.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.edtSrc, R.id.edtDest})
    public void onViewClickedAddress(View view) {
        switch (view.getId()) {
            case R.id.edtSrc:
                startActivity(PoiSearchActivity.newIntent(this,true));
                break;
            case R.id.edtDest:
                startActivity(PoiSearchActivity.newIntent(this,false));
                break;
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            showToast("抱歉，未找到结果" )  ;
            return ;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.getRouteLines().size() > 1 ) { //使用其中一条路线
            mRouteLine = drivingRouteResult.getRouteLines().get(0);
            setRelativeInfo() ;
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            //routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    private void setRelativeInfo(){
        long  time = mRouteLine.getDuration();
        if ( time / 3600 == 0 ) {
            mTxtAllTime.setText( "" + time / 60 + "分钟" );
        } else {
            mTxtAllTime.setText( "" + time / 3600 + "小时" + (time % 3600) / 60 + "分钟" );
        }
        int  distance = mRouteLine.getDistance() ;
        mTxtAllDistance.setText(distance+"米") ;
    }
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return null;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || location.getLocType() == BDLocation.TypeServerError || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //使用街道
                List<Poi> poiList = location.getPoiList() ;
                if(poiList != null && poiList.size() > 1) {
                    Poi poi = poiList.get(0);
                    mEdtSrc.setText(poi.getName());
                }
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                srcLating = ll ;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(12.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void process(AddressSelectVM o){
        if(o.isSrc()){
            mEdtSrc.setText(o.getmPoiInfo().name)   ;
            srcLating = o.getmPoiInfo().location   ;
        }
        else{
            mEdtDest.setText(o.getmPoiInfo().name)  ;
            destLating = o.getmPoiInfo().location  ;
        }
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(LatLng srcLatLng ,   String src ,
                          LatLng destLatLng ,  String dest ) {

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(srcLatLng).endPoint(destLatLng)
                .startName(src).endName(dest);

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(HomeActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}
