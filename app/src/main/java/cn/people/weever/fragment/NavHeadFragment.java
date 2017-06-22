package cn.people.weever.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.people.weever.R;
import cn.people.weever.common.timer.ITimerExecute;
import cn.people.weever.common.timer.TimerByHandler;
import cn.people.weever.common.util.NumberFormat;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RealTimeOrderInfo;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionRoutineListener} interface
 * to handle interaction events.
 * Use the {@link NavHeadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NewApi")
public class NavHeadFragment extends SubscribeResumePauseBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.txtAllDistance)
    TextView mTxtAllDistance;
    @BindView(R.id.txtAllTime)
    TextView mTxtAllTime;
    @BindView(R.id.txtAllWaitTime)
    TextView mTxtAllWaitTime;
    @BindView(R.id.txtAllWaitAmount)
    TextView mTxtAllWaitAmount;
    @BindView(R.id.ll_top)
    LinearLayout mLlTop;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private BaseOrder mBaseOrder;

    private TimerByHandler mTimerByHandler;

    private OrderService mOrderService;

    private OnFragmentInteractionRoutineListener mListener;

    // 搜索相关
    RoutePlanSearch mSearch = null ;    // 搜索模块，也可去掉地图模块独立使用
    private LatLng srcLating      ;
    private LatLng destLating     ;

    public NavHeadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavHeadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavHeadFragment newInstance(BaseOrder baseOrder) {
        NavHeadFragment fragment = new NavHeadFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, baseOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar() ;
    }

    private void initVar(){
        mOrderService = new OrderService();
        if (getArguments() != null) {
            mBaseOrder = (BaseOrder) getArguments().getSerializable(ARG_PARAM1);
        }
        mOrderService = new OrderService() ;
        if(mBaseOrder != null){
            if (mBaseOrder.getPlanboardingTripNode().getAddress() != null) {
                srcLating = new LatLng(mBaseOrder.getPlanboardingTripNode().getAddress().getLatitude(),
                        mBaseOrder.getPlanboardingTripNode().getAddress().getLongitude());
            }
            if (mBaseOrder.getPlanDropOffTripNode().getAddress() != null) {
                destLating = new LatLng(mBaseOrder.getPlanDropOffTripNode().getAddress().getLatitude(),
                        mBaseOrder.getPlanDropOffTripNode().getAddress().getLongitude());
            }
        }
        initSearch() ;

    }

    private void initSearch(){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
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
                    RouteLine routeLine = drivingRouteResult.getRouteLines().get(0);
                    setRelativeInfo(routeLine) ;
//                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
//                    mBaiduMap.setOnMarkerClickListener(overlay);
//                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
//                    overlay.addToMap();
//                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        PlanNode stNode = PlanNode.withLocation(srcLating)   ;
        PlanNode enNode = PlanNode.withLocation(destLating)  ;
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
    }

    private void setRelativeInfo(RouteLine routeLine){
        long  time = routeLine.getDuration();
        if ( time / 3600 == 0 ) {
            mTxtAllTime.setText( "" + time / 60 + "分钟" );
        } else {
            mTxtAllTime.setText( "" + time / 3600 + "小时" + (time % 3600) / 60 + "分钟" );
        }
        double  distance = routeLine.getDistance() ;
        mTxtAllDistance.setText(NumberFormat.getdouble(distance/1000, 1)+"公里") ;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_head, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnFragmentInteractionRoutineListener {
        void onFragmentInteraction(Uri uri);
    }

    private void realtime() {
        mTimerByHandler = new TimerByHandler(new ITimerExecute() {
            @Override
            public void onExecute() {
                if (mBaseOrder != null) {
                    mOrderService.getRealTimeOrderInfo(mBaseOrder);
                }
            }
        });
        mTimerByHandler.start();
    }

    @Override
    protected void dealSuccess(@Nullable BaseModel baseModel) {
        if (baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_REAL_TIME_INFO_NET_REQUST) {
            showToast("操作成功");
            RealTimeOrderInfo realTimeOrderInfo = (RealTimeOrderInfo) baseModel.getData();
            mTxtAllWaitTime.setText(realTimeOrderInfo.getWaitTime());
            mTxtAllWaitAmount.setText(realTimeOrderInfo.getWaitCost());
        }
    }
}
