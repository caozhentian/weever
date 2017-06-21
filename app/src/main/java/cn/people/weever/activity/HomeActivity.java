package cn.people.weever.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
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
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.nav.BNDemoGuideActivity;
import cn.people.weever.activity.nav.BNEventHandler;
import cn.people.weever.activity.order.clearing.OrderClearingBaseActivity;
import cn.people.weever.activity.order.list.MyOrdersActivity;
import cn.people.weever.activity.poi.AddressSelectVM;
import cn.people.weever.activity.poi.PoiSearchActivity;
import cn.people.weever.activity.setting.SettingUpActivity;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.timer.ITimerExecute;
import cn.people.weever.common.timer.TimerByHandler;
import cn.people.weever.common.util.DatetimeUtil;
import cn.people.weever.common.util.MapUtil;
import cn.people.weever.common.util.NumberFormat;
import cn.people.weever.config.FileConfig;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.mapapi.overlayutil.CityConstant;
import cn.people.weever.mapapi.overlayutil.DrivingRouteOverlay;
import cn.people.weever.model.Address;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RealTimeOrderInfo;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.model.TripNode;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.receiver.PowerReceiver;
import cn.people.weever.service.OrderService;

public class HomeActivity extends SubcribeCreateDestroyActivity implements OnGetRoutePlanResultListener
       ,NavigationView.OnNavigationItemSelectedListener {

    public static final String ARG_BASE_ORDER = "baseorder" ;

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

    private RouteOperateEvent mRouteOperateEvent = new RouteOperateEvent();

    private OrderService mOrderService ;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private PowerReceiver powerReceiver = null;

    /**
     * 打包周期
     */
    public int packInterval = 5;
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

    private boolean[] status = new boolean[4] ;

    private BaseOrder mBaseOrder ;

    private TimerByHandler mTimerByHandler ;

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
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setProdName("WeeverDemo");
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
		BitmapDescriptor currentMarker = BitmapDescriptorFactory
                           .fromResource(R.drawable.ic_map_location);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, currentMarker,
                        accuracyCircleFillColor, accuracyCircleStrokeColor));
        mLocClient.setLocOption(option);
        mLocClient.start();
        startTrace() ;
        initListener();
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
            OKCancelDlg.createCancelOKDlg(this, "确认退出应用吗?", new ICancelOK() {
                @Override
                public void cancel() {

                }

                @Override
                public void ok() {
                    finish();
                }
            });

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
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        powerManager =  (PowerManager) getSystemService(Context.POWER_SERVICE);
        mOrderService = new OrderService() ;


    }

    @Override
    public void initView() {
      mBaseOrder = (BaseOrder) getIntent().getSerializableExtra(ARG_BASE_ORDER);
      if(mBaseOrder != null){
         mEdtSrc.setText(mBaseOrder.getPlanboardingTripNode().getAddress().getPlaceName());
         mEdtDest.setText(mBaseOrder.getPlanDropOffTripNode().getAddress().getPlaceName()) ;
         if(mBaseOrder.getType() == BaseOrder.ORDER_TYPE_DAY){
             mRadioBtnDayUse.setChecked(true);
         }
         else if(mBaseOrder.getType() == BaseOrder.ORDER_TYPE_DAY_HALF){
             mRadioBtnHalfDayUse.setChecked(true);
         }
         else if(mBaseOrder.getType() == BaseOrder.ORDER_TYPE_PICK_UP ||
                 mBaseOrder.getType() == BaseOrder.ORDER_STAUS_APPOINTMENT){
             mRadioBtnTransfer.setChecked(true);
         }
          if(mBaseOrder != null) {
              mRouteOperateEvent.setOrderId(mBaseOrder.getOrderId());
              //根据订单初始化
              if (mBaseOrder.getPlanboardingTripNode().getAddress() != null) {
                  srcLating = new LatLng(mBaseOrder.getPlanboardingTripNode().getAddress().getLatitude(),
                          mBaseOrder.getPlanboardingTripNode().getAddress().getLongitude());
              }
              if (mBaseOrder.getPlanDropOffTripNode().getAddress() != null) {
                  destLating = new LatLng(mBaseOrder.getPlanDropOffTripNode().getAddress().getLatitude(),
                          mBaseOrder.getPlanDropOffTripNode().getAddress().getLongitude());
              }
          }
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
                waitting() ;
                break;
//            case R.id.btnReturn:
//                break;
            case R.id.btnRestart:
                restart() ;
                break;
            case R.id.btnCompute:
                compute() ;
                break;
        }
    }

    private void waitting(){
        if(mBaseOrder == null){
            showToast("无关联订单，请先关联订单");
            return ;
        }
        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_WAITTING_OPERATE_TYPE);
        mOrderService.routeOperateOrder(mRouteOperateEvent);
    }

    private  void restart(){
        if(mBaseOrder == null){
            showToast("无关联订单，请先关联订单");
            return ;
        }
        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_RESTART_OPERATE_TYPE);
        mOrderService.routeOperateOrder(mRouteOperateEvent);
    }

	private void compute(){
        if(mBaseOrder == null){
            showToast("无关联订单，请先关联订单");
            return ;
        }
        OKCancelDlg.createCancelOKDlg(this, "确定结算吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                WeeverApplication.mClient.stopGather(traceListener);
            }
        });

	}

    private void start(){
        if(mBaseOrder == null){
            showToast("无关联订单，请先关联订单");
            return ;
        }
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
        OKCancelDlg.createCancelOKDlg(this, "确定开始计费吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                // 设置起终点信息，对于tranist search 来说，城市名无意义
                PlanNode stNode = PlanNode.withLocation(srcLating)   ;
                PlanNode enNode = PlanNode.withLocation(destLating)  ;
                mBaiduMap.clear();
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
                mLlTop.setVisibility(View.VISIBLE);
                WeeverApplication.mClient.startGather(traceListener);
            }
        });

    }

    @OnClick({R.id.edtSrc, R.id.edtDest})
    public void onViewClickedAddress(View view) {
        switch (view.getId()) {
            case R.id.edtSrc:
                if(mBaseOrder != null){
                   showToast("已关联订单，无法更改目的地址");
                   return ;
                }
                startActivity(PoiSearchActivity.newIntent(this,true));
                break;
            case R.id.edtDest:
                if(mBaseOrder != null){
                    showToast("已关联订单，无法更改目的地址");
                    return ;
                }
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
            return;
        }
        if (drivingRouteResult.getRouteLines().size() > 1 ) { //使用其中一条路线
            mRouteLine = drivingRouteResult.getRouteLines().get(0);
            setRelativeInfo() ;
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
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
        double  distance = mRouteLine.getDistance() ;

        mTxtAllDistance.setText(NumberFormat.getdouble(distance/1000, 1)+"公里") ;
    }
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

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
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            //使用街道
            List<Poi> poiList = location.getPoiList() ;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            if(mBaseOrder == null) { //如果有订单关联，出发地是订单的出发地
                srcLating = ll;
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                //使用街道
                if(poiList != null && poiList.size() > 1) {
                    Poi poi = poiList.get(0);
                    if(mBaseOrder == null) {
                        mEdtSrc.setText(poi.getName());
                    }
                }
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            TripNode tripNode  = new TripNode() ;
            tripNode.setTime(DatetimeUtil.getCurrentDayTimeMillis()/1000);
            Address address = new Address() ;
            address.setPlaceName(poiList.get(0).getName()) ;
            address.setLatitude(location.getLatitude())    ;
            address.setLongitude(location.getLongitude())  ;
            tripNode.setAddress(address);
            WeeverApplication.getInstance().setCurTripNode(tripNode);
            mRouteOperateEvent.setTripNode(tripNode);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
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

	private void initListener() {

        trackListener = new OnTrackListener() {
            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                // 添加路线（轨迹）
                MapUtil mapUtil = MapUtil.getInstance();
                LatLng currentLatLng = mapUtil.convertTrace2Map(response.getLatestPoint().getLocation());
                List<LatLng> latLngs = new ArrayList<>(1) ;
                latLngs.add(currentLatLng) ;
                OverlayOptions polylineOptions = new PolylineOptions().width(10)
                        .color(Color.RED).points(latLngs);
                mBaiduMap.addOverlay(polylineOptions);
            }};

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {

            }} ;
        traceListener = new OnTraceListener() {
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    registerPowerReceiver();
                }
                //showToast(String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                //showToast(String.format("%s", message));
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    unregisterPowerReceiver();
                }
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    showToast( "轨迹采集开始");
                    mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_CHARGING_OPERATE_TYPE);
                    mOrderService.routeOperateOrder(mRouteOperateEvent);
                    mTimerByHandler = new TimerByHandler(new ITimerExecute() {
                        @Override
                        public void onExecute() {
                            if(mBaseOrder != null) {
                                mOrderService.getRealTimeOrderInfo(mBaseOrder);
                            }
                        }
                    });
                    mTimerByHandler.start();
                }
                else{
                    showToast( "轨迹采集失败，请重新点击");
                }
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    showToast( "轨迹采集结束");
                    mTimerByHandler.stop() ;
                    mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE);
                    mOrderService.routeOperateOrder(mRouteOperateEvent);
                }
                else{
                    showToast( "轨迹采集失败，请重新点击");
                }
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }
        };
    }
    
	private void startTrace(){
        WeeverApplication.mClient.startTrace(WeeverApplication.mTrace, traceListener);
	}


    /**
     * 注册电源锁广播
     */
    private void registerPowerReceiver() {
        if (WeeverApplication.isRegisterPower || powerManager == null) {
            return;
        }
        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == powerReceiver) {
            powerReceiver = new PowerReceiver(wakeLock);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(powerReceiver, filter);
        WeeverApplication.isRegisterPower = true;
    }

    private void unregisterPowerReceiver() {
        if (!WeeverApplication.isRegisterPower) {
            return;
        }
        if (null != powerReceiver) {
            unregisterReceiver(powerReceiver);
        }
        WeeverApplication.isRegisterPower = false;
    }

    private boolean hasInitSuccess = false;
    String authinfo = "";

    private void routeplanToNavi() {

        if (!hasInitSuccess) {
            Toast.makeText(HomeActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(srcLating.longitude, srcLating.latitude, mEdtSrc.getText().toString(), null, BNRoutePlanNode.CoordinateType.BD09LL);
        eNode = new BNRoutePlanNode(destLating.longitude, destLating.latitude, mEdtDest.getText().toString(), null, BNRoutePlanNode.CoordinateType.BD09LL);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode),
                    eventListerner);
        }
    }
    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            startActivity(BNDemoGuideActivity.newIntent(HomeActivity.this ,mBNRoutePlanNode , mBaseOrder));

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(HomeActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };
    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, FileConfig.S_SDCardPath, FileConfig.APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {

                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                HomeActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(HomeActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(HomeActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(HomeActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9458069");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    @Override
    protected  void dealSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_ROUTE_OPERATE_NET_REQUST){
            showToast("操作成功");

            RouteOperateEvent routeOperateEvent = (RouteOperateEvent) baseModel.getData();
            if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE){
                startActivity(OrderClearingBaseActivity.newIntent(HomeActivity.this , mBaseOrder));
                mEdtDest.setText("");
                mEdtSrc.setText("");
                mRadioBtnDayUse.setChecked(false)      ;
                mRadioBtnTransfer.setChecked(false)    ;
                mRadioBtnHalfDayUse.setChecked(false)  ;
                mBaseOrder = null ;
            }
        }
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_REAL_TIME_INFO_NET_REQUST){
            showToast("操作成功");
            RealTimeOrderInfo realTimeOrderInfo = (RealTimeOrderInfo) baseModel.getData();
            mTxtAllWaitTime.setText(realTimeOrderInfo.getWaitTime())    ;
            mTxtAllWaitAmount.setText(realTimeOrderInfo.getWaitCost())  ;
        }
    }
}
