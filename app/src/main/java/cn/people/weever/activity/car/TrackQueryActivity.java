package cn.people.weever.activity.car;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.analysis.DrivingBehaviorRequest;
import com.baidu.trace.api.analysis.DrivingBehaviorResponse;
import com.baidu.trace.api.analysis.OnAnalysisListener;
import com.baidu.trace.api.analysis.SpeedingInfo;
import com.baidu.trace.api.analysis.StayPointRequest;
import com.baidu.trace.api.analysis.StayPointResponse;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.Point;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.constant.TraceConstants;
import cn.people.weever.common.util.BitmapUtil;
import cn.people.weever.common.util.CommonUtil;
import cn.people.weever.common.util.MapUtil;
import cn.people.weever.map.TraceService;

/**
 * 轨迹查询
 */
public class TrackQueryActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener {

    private  static final String S_START_TIME =  "start_time" ;

    private  static final String S_END_TIME   =  "end_time" ;
    public static final String CAR_NUM = "CarNum";


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.imgBtn_track_analysis)
    ImageButton mImgBtnTrackAnalysis;
    private WeeverApplication trackApp = null;

    /**
     * 地图工具
     */
    private MapUtil mapUtil = null;

    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();

    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 轨迹分析对话框
     */
    //private TrackAnalysisDialog trackAnalysisDialog = null;

    /**
     * 轨迹分析详情框布局
     */
    // private TrackAnalysisInfoLayout trackAnalysisInfoLayout = null;

    /**
     * 当前轨迹分析详情框对应的marker
     */
    private Marker analysisMarker = null;

    /**
     * 驾驶行为请求
     */
    private DrivingBehaviorRequest drivingBehaviorRequest = new DrivingBehaviorRequest();

    /**
     * 停留点请求
     */
    private StayPointRequest stayPointRequest = new StayPointRequest();

    /**
     * 轨迹分析监听器
     */
    private OnAnalysisListener mAnalysisListener = null;

    /**
     * 查询轨迹的开始时间
     */
    private long startTime = CommonUtil.getLongTime("2017-05-24 18:10:00");

    /**
     * 查询轨迹的结束时间
     */
    private long endTime = CommonUtil.getLongTime("2017-05-24 19:20:00");

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();

    /**
     * 轨迹分析  超速点集合
     */
    private List<Point> speedingPoints = new ArrayList<>();

    /**
     * 轨迹分析  急加速点集合
     */
    private List<Point> harshAccelPoints = new ArrayList<>();

    /**
     * 轨迹分析  急刹车点集合
     */
    private List<Point> harshBreakingPoints = new ArrayList<>();

    /**
     * 轨迹分析  急转弯点集合
     */
    private List<Point> harshSteeringPoints = new ArrayList<>();

    /**
     * 轨迹分析  停留点集合
     */
    private List<Point> stayPoints = new ArrayList<>();

    /**
     * 轨迹分析 超速点覆盖物集合
     */
    private List<Marker> speedingMarkers = new ArrayList<>();

    /**
     * 轨迹分析 急加速点覆盖物集合
     */
    private List<Marker> harshAccelMarkers = new ArrayList<>();

    /**
     * 轨迹分析  急刹车点覆盖物集合
     */
    private List<Marker> harshBreakingMarkers = new ArrayList<>();

    /**
     * 轨迹分析  急转弯点覆盖物集合
     */
    private List<Marker> harshSteeringMarkers = new ArrayList<>();

    /**
     * 轨迹分析  停留点覆盖物集合
     */
    private List<Marker> stayPointMarkers = new ArrayList<>();

    /**
     * 是否查询超速点
     */
    private boolean isSpeeding = false;

    /**
     * 是否查询急加速点
     */
    private boolean isHarshAccel = false;

    /**
     * 是否查询急刹车点
     */
    private boolean isHarshBreaking = false;

    /**
     * 是否查询急转弯点
     */
    private boolean isHarshSteering = false;

    /**
     * 是否查询停留点
     */
    private boolean isStayPoint = false;

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    private int pageIndex = 1;

    /**
     * 轨迹分析上一次请求时间
     */
    private long lastQueryTime = 0;

    private String entityName ;
    public static final Intent newIntent(Context packageContext , long startTime , long endTime, String carNum) {
        Intent intent = new Intent(packageContext, TrackQueryActivity.class);
        intent.putExtra(S_START_TIME , startTime) ;
        Date  date = new Date(startTime*1000) ;
        intent.putExtra(S_END_TIME , endTime );
        Date  date2 = new Date(endTime*1000) ;
        intent.putExtra(CAR_NUM, carNum);
        return intent;
    }

    @Override
    public void initData() {
        startTime  = getIntent().getLongExtra(S_START_TIME , 0L) ;
        endTime    = getIntent().getLongExtra(S_END_TIME , 0L)    ;
        entityName = getIntent().getStringExtra(CAR_NUM) ;
        queryHistoryTrack();
    }

    @Override
    public void initView() {
        init() ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackquery);
        ButterKnife.bind(this);
        tvTitle.setText("路线轨迹");
        trackApp = (WeeverApplication) getApplicationContext();
        pageIndex = 1;
        initView() ;
        initData() ;
    }

    /**
     * 初始化
     */
    private void init() {
        mapUtil = MapUtil.getInstance();
        mapUtil.init((MapView) findViewById(R.id.track_query_mapView));
        //mapUtil.baiduMap.setOnMarkerClickListener(this);
        mapUtil.setCenter(trackApp);
        //trackAnalysisInfoLayout = new TrackAnalysisInfoLayout(this, mapUtil.baiduMap);
        initListener();
        BitmapUtil.init();

    }

    /**
     * 轨迹分析
     *
     * @param v
     */
//    public void onTrackAnalysis(View v) {
//        if (null == trackAnalysisDialog) {
//            trackAnalysisDialog = new TrackAnalysisDialog(this);
//        }
//        // 显示窗口
//        trackAnalysisDialog.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        // 处理PopupWindow在Android N系统上的兼容性问题
//        if (Build.VERSION.SDK_INT < 24) {
//            trackAnalysisDialog.update(trackAnalysisDialog.getWidth(), trackAnalysisDialog.getHeight());
//        }
//        if (CommonUtil.getCurrentTime() - lastQueryTime > Constants.ANALYSIS_QUERY_INTERVAL) {
//            lastQueryTime = CommonUtil.getCurrentTime();
//            speedingPoints.clear();
//            harshAccelPoints.clear();
//            harshBreakingPoints.clear();
//            stayPoints.clear();
//            queryDrivingBehavior();
//            queryStayPoint();
//        }
//
//    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {

        // 设置需要纠偏
        historyTrackRequest.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
// 设置需要去噪
        processOption.setNeedDenoise(true);
// 设置需要抽稀
        processOption.setNeedVacuate(true);
// 设置需要绑路
        processOption.setNeedMapMatch(true);
// 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
// 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.driving);
// 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
// 设置里程填充方式为驾车
        historyTrackRequest.setSupplementMode(SupplementMode.driving);
        TraceService.getInstance(WeeverApplication.getInstance()).initRequest(historyTrackRequest);
        //String entityName = PreferencesUtil.getStringPreferences(WeeverApplication.getInstance() , "CAR_KEY") ;
        historyTrackRequest.setEntityName(entityName);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(TraceConstants.PAGE_SIZE);
        TraceService.getInstance(WeeverApplication.getInstance()).queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    /**
     * 查询驾驶行为
     */
    private void queryDrivingBehavior() {
//        trackApp.initRequest(drivingBehaviorRequest);
//        drivingBehaviorRequest.setEntityName(trackApp.entityName);
//        drivingBehaviorRequest.setStartTime(startTime);
//        drivingBehaviorRequest.setEndTime(endTime);
//        trackApp.mClient.queryDrivingBehavior(drivingBehaviorRequest, mAnalysisListener);
    }

    /**
     * 查询停留点
     */
    private void queryStayPoint() {
//        trackApp.initRequest(stayPointRequest);
//        stayPointRequest.setEntityName(trackApp.entityName);
//        stayPointRequest.setStartTime(startTime);
//        stayPointRequest.setEndTime(endTime);
//        stayPointRequest.setStayTime(Constants.STAY_TIME);
//        trackApp.mClient.queryStayPoint(stayPointRequest, mAnalysisListener);
    }

    /**
     * 轨迹分析对话框 选项点击事件
     *
     * @param compoundButton
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()) {
//            case R.id.chk_speeding:
//                isSpeeding = isChecked;
//                handleMarker(speedingMarkers, isSpeeding);
//                break;
//
//            case R.id.chk_harsh_breaking:
//                isHarshBreaking = isChecked;
//                handleMarker(harshBreakingMarkers, isHarshBreaking);
//                break;
//
//            case R.id.chk_harsh_accel:
//                isHarshAccel = isChecked;
//                handleMarker(harshAccelMarkers, isHarshAccel);
//                break;
//
//            case R.id.chk_harsh_steering:
//                isHarshSteering = isChecked;
//                handleMarker(harshSteeringMarkers, isHarshSteering);
//                break;
//
//            case R.id.chk_stay_point:
//                isStayPoint = isChecked;
//                handleMarker(stayPointMarkers, isStayPoint);
//                break;
//
//            default:
//                break;
        }
    }

//    /**
//     * 按钮点击事件
//     *
//     * @param view
//     */
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            // 轨迹查询选项
//            case R.id.btn_activity_options:
//                ViewUtil.startActivityForResult(this, TrackQueryOptionsActivity.class, Constants.REQUEST_CODE);
//                break;
//
//            default:
//                break;
//        }
//    }

//    /**
//     * 轨迹分析覆盖物点击事件
//     *
//     * @param marker
//     *
//     * @return
//     */
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        Bundle bundle = marker.getExtraInfo();
//        if (null == bundle) {
//            return false;
//        }
//        int type = bundle.getInt("type");
//        switch (type) {
//            case R.id.chk_speeding:
//                trackAnalysisInfoLayout.titleText.setText(R.string.track_analysis_speeding_title);
//                trackAnalysisInfoLayout.key1.setText(R.string.actual_speed);
//                trackAnalysisInfoLayout.value1.setText(String.valueOf(bundle.getDouble("actualSpeed")));
//                trackAnalysisInfoLayout.key2.setText(R.string.limit_speed);
//                trackAnalysisInfoLayout.value2.setText(String.valueOf(bundle.getDouble("limitSpeed")));
//                break;
//
//            case R.id.chk_harsh_accel:
//                trackAnalysisInfoLayout.titleText.setText(R.string.track_analysis_accel_title);
//                trackAnalysisInfoLayout.key1.setText(R.string.acceleration);
//                trackAnalysisInfoLayout.value1.setText(String.valueOf(bundle.getDouble("acceleration")));
//                trackAnalysisInfoLayout.key2.setText(R.string.initial_speed_2);
//                trackAnalysisInfoLayout.value2.setText(String.valueOf(bundle.getDouble("initialSpeed")));
//                trackAnalysisInfoLayout.key3.setText(R.string.end_speed_2);
//                trackAnalysisInfoLayout.value3.setText(String.valueOf(bundle.getDouble("endSpeed")));
//                break;
//
//            case R.id.chk_harsh_breaking:
//                trackAnalysisInfoLayout.titleText.setText(R.string.track_analysis_breaking_title);
//                trackAnalysisInfoLayout.key1.setText(R.string.acceleration);
//                trackAnalysisInfoLayout.value1.setText(String.valueOf(bundle.getDouble("acceleration")));
//                trackAnalysisInfoLayout.key2.setText(R.string.initial_speed_1);
//                trackAnalysisInfoLayout.value2.setText(String.valueOf(bundle.getDouble("initialSpeed")));
//                trackAnalysisInfoLayout.key3.setText(R.string.end_speed_1);
//                trackAnalysisInfoLayout.value3.setText(String.valueOf(bundle.getDouble("endSpeed")));
//                break;
//
//            case R.id.chk_harsh_steering:
//                trackAnalysisInfoLayout.titleText.setText(R.string.track_analysis_steering_title);
//                trackAnalysisInfoLayout.key1.setText(R.string.centripetal_acceleration);
//                trackAnalysisInfoLayout.value1.setText(String.valueOf(bundle.getDouble("centripetalAcceleration")));
//                trackAnalysisInfoLayout.key2.setText(R.string.turn_type);
//                trackAnalysisInfoLayout.value2.setText(String.valueOf(bundle.getDouble("turnType")));
//                trackAnalysisInfoLayout.key3.setText(R.string.turn_speed);
//                trackAnalysisInfoLayout.value3.setText(String.valueOf(bundle.getDouble("turnSpeed")));
//                break;
//
//            case R.id.chk_stay_point:
//                trackAnalysisInfoLayout.titleText.setText(R.string.track_analysis_stay_title);
//                trackAnalysisInfoLayout.key1.setText(R.string.stay_start_time);
//                trackAnalysisInfoLayout.value1.setText(CommonUtil.formatTime(bundle.getLong("startTime") * 1000));
//                trackAnalysisInfoLayout.key2.setText(R.string.stay_end_time);
//                trackAnalysisInfoLayout.value2.setText(CommonUtil.formatTime(bundle.getLong("endTime") * 1000));
//                trackAnalysisInfoLayout.key3.setText(R.string.stay_duration);
//                trackAnalysisInfoLayout.value3.setText(CommonUtil.formatSecond(bundle.getInt("duration")));
//                break;
//
//            default:
//                break;
//        }
//        //  保存当前操作的marker
//        analysisMarker = marker;
//
//        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//        InfoWindow trackAnalysisInfoWindow = new InfoWindow(trackAnalysisInfoLayout.mView, marker.getPosition(), -47);
//        //显示InfoWindow
//        mapUtil.baiduMap.showInfoWindow(trackAnalysisInfoWindow);
//
//        return false;
//    }

    private void clearAnalysisList() {
        if (null != speedingPoints) {
            speedingPoints.clear();
        }
        if (null != harshAccelPoints) {
            harshAccelPoints.clear();
        }
        if (null != harshBreakingPoints) {
            harshBreakingPoints.clear();
        }
        if (null != harshSteeringPoints) {
            harshSteeringPoints.clear();
        }
    }

    private void initListener() {
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    showToast(response.getMessage());
                    return ;
                } else if (0 == total) {
                    showToast("无数据");
                    return ;
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > TraceConstants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack();
                } else {
                    mapUtil.drawHistoryTrack(trackPoints, sortType);
                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                super.onLatestPointCallback(response);
            }
        };

        mAnalysisListener = new OnAnalysisListener() {
            @Override
            public void onStayPointCallback(StayPointResponse response) {
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    lastQueryTime = 0;
                    showToast(response.getMessage());
                    return;
                }
                if (0 == response.getStayPointNum()) {
                    return;
                }
                stayPoints.addAll(response.getStayPoints());
                //handleOverlays(stayPointMarkers, stayPoints, isStayPoint);
            }

            @Override
            public void onDrivingBehaviorCallback(DrivingBehaviorResponse response) {
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    lastQueryTime = 0;
                    showToast(response.getMessage());
                    return;
                }

                if (0 == response.getSpeedingNum() && 0 == response.getHarshAccelerationNum()
                        && 0 == response.getHarshBreakingNum() && 0 == response.getHarshSteeringNum()) {
                    return;
                }

                clearAnalysisList();
                clearAnalysisOverlay();

                List<SpeedingInfo> speedingInfos = response.getSpeedings();
                for (SpeedingInfo info : speedingInfos) {
                    speedingPoints.addAll(info.getPoints());
                }
                harshAccelPoints.addAll(response.getHarshAccelerationPoints());
                harshBreakingPoints.addAll(response.getHarshBreakingPoints());
                harshSteeringPoints.addAll(response.getHarshSteeringPoints());

//                handleOverlays(speedingMarkers, speedingPoints, isSpeeding);
//                handleOverlays(harshAccelMarkers, harshAccelPoints, isHarshAccel);
//                handleOverlays(harshBreakingMarkers, harshBreakingPoints, isHarshBreaking);
//                handleOverlays(harshSteeringMarkers, harshSteeringPoints, isHarshSteering);
            }
        };
    }

    /**
     * 处理轨迹分析覆盖物
     *
     * @param markers
     * @param points
     * @param isVisible
     */
    private void handleOverlays(List<Marker> markers, List<? extends Point> points, boolean
            isVisible) {
        if (null == markers || null == points) {
            return;
        }
//        for (com.baidu.trace.model.Point point : points) {
//            OverlayOptions overlayOptions = new MarkerOptions()
//                    .position(MapUtil.convertTrace2Map(point.getLocation()))
//                    .icon(BitmapUtil.bmGcoding).zIndex(9).draggable(true);
//            Marker marker = (Marker) mapUtil.baiduMap.addOverlay(overlayOptions);
//            Bundle bundle = new Bundle();
//
//            if (point instanceof SpeedingPoint) {
//                SpeedingPoint speedingPoint = (SpeedingPoint) point;
//                bundle.putInt("type", R.id.chk_speeding);
//                bundle.putDouble("actualSpeed", speedingPoint.getActualSpeed());
//                bundle.putDouble("limitSpeed", speedingPoint.getLimitSpeed());
//
//            } else if (point instanceof HarshAccelerationPoint) {
//                HarshAccelerationPoint accelPoint = (HarshAccelerationPoint) point;
//                bundle.putInt("type", R.id.chk_harsh_accel);
//                bundle.putDouble("acceleration", accelPoint.getAcceleration());
//                bundle.putDouble("initialSpeed", accelPoint.getInitialSpeed());
//                bundle.putDouble("endSpeed", accelPoint.getEndSpeed());
//
//            } else if (point instanceof HarshBreakingPoint) {
//                HarshBreakingPoint breakingPoint = (HarshBreakingPoint) point;
//                bundle.putInt("type", R.id.chk_harsh_breaking);
//                bundle.putDouble("acceleration", breakingPoint.getAcceleration());
//                bundle.putDouble("initialSpeed", breakingPoint.getInitialSpeed());
//                bundle.putDouble("endSpeed", breakingPoint.getEndSpeed());
//
//            } else if (point instanceof HarshSteeringPoint) {
//                HarshSteeringPoint steeringPoint = (HarshSteeringPoint) point;
//                bundle.putInt("type", R.id.chk_harsh_steering);
//                bundle.putDouble("centripetalAcceleration", steeringPoint.getCentripetalAcceleration());
//                bundle.putString("turnType", steeringPoint.getTurnType().name());
//                bundle.putDouble("turnSpeed", steeringPoint.getTurnSpeed());
//
//            } else if (point instanceof StayPoint) {
//                StayPoint stayPoint = (StayPoint) point;
//                bundle.putInt("type", R.id.chk_stay_point);
//                bundle.putLong("startTime", stayPoint.getStartTime());
//                bundle.putLong("endTime", stayPoint.getEndTime());
//                bundle.putInt("duration", stayPoint.getDuration());
//            }
//            marker.setExtraInfo(bundle);
//            markers.add(marker);
//        }

        handleMarker(markers, isVisible);
    }

    /**
     * 处理marker
     *
     * @param markers
     * @param isVisible
     */
    private void handleMarker(List<Marker> markers, boolean isVisible) {
        if (null == markers || markers.isEmpty()) {
            return;
        }
        for (Marker marker : markers) {
            marker.setVisible(isVisible);
        }

        if (markers.contains(analysisMarker)) {
            mapUtil.baiduMap.hideInfoWindow();
        }

    }

    /**
     * 清除驾驶行为分析覆盖物
     */
    public void clearAnalysisOverlay() {
        clearOverlays(speedingMarkers);
        clearOverlays(harshAccelMarkers);
        clearOverlays(harshBreakingMarkers);
        clearOverlays(stayPointMarkers);
    }

    private void clearOverlays(List<Marker> markers) {
        if (null == markers) {
            return;
        }
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (null != trackAnalysisInfoLayout) {
//            trackAnalysisInfoLayout = null;
//        }
//        if (null != trackAnalysisDialog) {
//            trackAnalysisDialog.dismiss();
//            trackAnalysisDialog = null;
//        }
        if (null != trackPoints) {
            trackPoints.clear();
        }
        if (null != stayPoints) {
            stayPoints.clear();
        }
        clearAnalysisList();
        trackPoints = null;
        speedingPoints = null;
        harshAccelPoints = null;
        harshSteeringPoints = null;
        stayPoints = null;

        clearAnalysisOverlay();
        speedingMarkers = null;
        harshAccelMarkers = null;
        harshBreakingMarkers = null;
        stayPointMarkers = null;

        mapUtil.clear();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}