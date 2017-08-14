package cn.people.weever.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.model.OnTraceListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.people.weever.R;
import cn.people.weever.activity.order.clearing.OrderClearingBaseActivity;
import cn.people.weever.activity.poi.AddressSelectVM;
import cn.people.weever.activity.poi.PoiSearchActivity;
import cn.people.weever.application.ActivityExitManage;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.DatetimeUtil;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.map.LocationService;
import cn.people.weever.map.TLocationListener;
import cn.people.weever.map.TraceService;
import cn.people.weever.model.Address;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.model.TripNode;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NavFooterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NewApi")
public class NavFooterFragment extends SubscribeResumePauseBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    public static final String OPERATE_STATUS = "OPERATE_STATUS";
    @BindView(R.id.edtSrc)
    TextView mEdtSrc;
    @BindView(R.id.edtDest)
    TextView mEdtDest;
    @BindView(R.id.radioBtnDayUse)
    RadioButton mRadioBtnDayUse;
    @BindView(R.id.radioBtnHalfDayUse)
    RadioButton mRadioBtnHalfDayUse;
    @BindView(R.id.radioBtnTransfer)
    RadioButton mRadioBtnTransfer;
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
    @BindView(R.id.ll_src_dest_address)
    LinearLayout mLlSrcDestAddress;
    @BindView(R.id.radioGroup)
    LinearLayout mRadioGroup;
    @BindView(R.id.ll_operate)
    LinearLayout mLlOperate;

    Unbinder unbinder;

    private RouteOperateEvent mRouteOperateEvent = new RouteOperateEvent();

    private OrderService mOrderService ;
    private BaseOrder mBaseOrder;

    private LatLng srcLating ;
    private LatLng destLating ;
    private String mSrcAddress    ;
    private String mDestAddress   ;

    private OnFragmentInteractionNavFooterListener mListener;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    private boolean[] mBooleanOperate = new boolean[4] ;

    public NavFooterFragment() {
        // Required empty public constructor
    }

    public static NavFooterFragment newInstance(BaseOrder baseOrder) {
        NavFooterFragment fragment = new NavFooterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, baseOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mBooleanOperate = savedInstanceState.getBooleanArray( OPERATE_STATUS );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_footer, container, false);
        unbinder = ButterKnife.bind(this, view);
        initVar();
        return view;
    }

    private void initVar(){
        if (getArguments() != null) {
            mBaseOrder = (BaseOrder) getArguments().getSerializable(ARG_PARAM1);
        }
        setOrder(mBaseOrder) ;
        mOrderService = new OrderService() ;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(RouteOperateEvent routeOperateEvent) {
        if (mListener != null) {
            mListener.onFragmentInteraction(routeOperateEvent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionNavFooterListener) {
            mListener = (OnFragmentInteractionNavFooterListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionRoutineListener");
        }
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

    public void setOrder(BaseOrder baseOrder){
        if(mBaseOrder == null){
            mLlSrcDestAddress.setVisibility(View.VISIBLE);
            mRadioGroup.setVisibility(View.GONE);
            mLlOperate.setVisibility(View.GONE);
        }
        else{
            mLlSrcDestAddress.setVisibility(View.GONE);
            mRadioGroup.setVisibility(View.GONE);
            mLlOperate.setVisibility(View.VISIBLE);
        }
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
    @OnClick({R.id.btnStart, R.id.btnWait, R.id.btnRestart, R.id.btnCompute})
    public void onViewClicked(View view) {
        if(mBaseOrder == null){
            showToast(getString(R.string.select_order));
            return ;
        }
        ActivityExitManage.setCurBaseFragment( this)  ;
        switch (view.getId()) {
            case R.id.btnStart:
                start() ;
                break;
            case R.id.btnWait:
                waitting() ;
                break;
            case R.id.btnRestart:
                restart() ;
                break;
            case R.id.btnCompute:
                compute() ;
                break;
        }
    }
    @OnClick({R.id.edtSrc, R.id.edtDest})
    public void onViewClickedAddress(View view) {
        switch (view.getId()) {
            case R.id.edtSrc:
                if(mBaseOrder != null){
                    showToast("已关联订单，无法更改目的地址");
                    return ;
                }
                startActivity(PoiSearchActivity.newIntent(getContext(),true));
                break;
            case R.id.edtDest:
                if(mBaseOrder != null){
                    showToast("已关联订单，无法更改目的地址");
                    return ;
                }
                startActivity(PoiSearchActivity.newIntent(getContext(),false));
                break;
        }
    }

    private void start(){
        if(mBaseOrder == null){
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
        if(mBooleanOperate[0] || mBooleanOperate[1] || mBooleanOperate[2] || mBooleanOperate[3]){
            showToast("不能执行计费操作");
            return ;
        }
        OKCancelDlg.createCancelOKDlg(getContext(), "确定开始计费吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {

                TraceService.getInstance(WeeverApplication.getInstance()).startGather(traceListener);
                operate(RouteOperateEvent.TO_ORDER_CHARGING_OPERATE_TYPE) ;
            }
        });

    }

    private void waitting(){
        if(mBaseOrder == null){
            return ;
        }
        if( !mBooleanOperate[0] ){
            showToast("请先点击计费按钮");
            return ;
        }
        if( mBooleanOperate[3]){
            showToast("不能执行等待操作");
            return ;
        }
//        TripNode tripNode  = new TripNode() ;
//        tripNode.setTime(DatetimeUtil.getCurrentDayTimeMillis()/1000);
//        mRouteOperateEvent.setTripNode(tripNode);
//        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_WAITTING_OPERATE_TYPE);
//        mOrderService.routeOperateOrder(mRouteOperateEvent);
        operate(RouteOperateEvent.TO_ORDER_WAITTING_OPERATE_TYPE) ;
    }

    private  void restart(){
        if(mBaseOrder == null){
            return ;
        }
        if( !mBooleanOperate[0] ){
            showToast("请先点击计费按钮");
            return ;
        }
        if(  !mBooleanOperate[1] ){
            showToast("请先点击等待按钮");
            return ;
        }
        if( mBooleanOperate[3]){
            showToast("不能执行再出发操作");
            return ;
        }
//        TripNode tripNode  = new TripNode() ;
//        tripNode.setTime(DatetimeUtil.getCurrentDayTimeMillis()/1000);
//        mRouteOperateEvent.setTripNode(tripNode);
//        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_RESTART_OPERATE_TYPE);
//        mOrderService.routeOperateOrder(mRouteOperateEvent);
        operate(RouteOperateEvent.TO_ORDER_RESTART_OPERATE_TYPE) ;
    }

    public void compute(){
        if(mBaseOrder == null){

            return ;
        }
        if( !mBooleanOperate[0] ){
            showToast("请先点击计费按钮");
            return ;
        }
        if(  mBooleanOperate[1] && !mBooleanOperate[2]){
            showToast("请先点击再出发按钮");
            return ;
        }
        OKCancelDlg.createCancelOKDlg(getContext(), "确定结算吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                TraceService.getInstance(WeeverApplication.getInstance()).stopGather(traceListener);
                operate(RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE) ;
            }
        });

    }

    private void operate(int type){
        {
            TripNode tripNode  = new TripNode() ;
            tripNode.setTime(DatetimeUtil.getCurrentDayTimeMillis()/1000);
            mRouteOperateEvent.setTripNode(tripNode);
            mRouteOperateEvent.setOperateType(type);
            LocationService.getLocationService(getContext()).start();
            LocationService.getLocationService(getContext()).registerListener(new TLocationListener() {
                @Override
                public void process(BDLocation location) {
                    TripNode tripNode  = mRouteOperateEvent.getTripNode() ;
                    Address address = new Address() ;
                    List<Poi> poiList = location.getPoiList() ;
                    if(poiList != null & poiList.size() > 0) {
                        address.setPlaceName(poiList.get(0).getName());
                        address.setLatitude(location.getLatitude());
                        address.setLongitude(location.getLongitude());
                        tripNode.setAddress(address);
                    }
                    mOrderService.routeOperateOrder(mRouteOperateEvent);
                    LocationService.getLocationService(getContext()).stop();
                    LocationService.getLocationService(getContext()).unregisterListener(this);
                }

                @Override
                public void processFirstLoc(BDLocation location) {

                }

                @Override
                public void processLocFail(BDLocation location) {
                    mOrderService.routeOperateOrder(mRouteOperateEvent);
                }
            }) ;
        }
    }
    public interface OnFragmentInteractionNavFooterListener {

        void onFragmentInteraction(RouteOperateEvent routeOperateEvent);
    }

    public void setLocationSrc(String name){
        if(mBaseOrder == null){
            mEdtSrc.setText(name);
//            LocationService.getLocationService(getContext()).start();
//            LocationService.getLocationService(getContext()).registerListener(new TLocationListener() {
//                @Override
//                public void process(BDLocation location) {
//                    List<Poi> poiList = location.getPoiList() ;
//                    mEdtSrc.setText(poiList.get(0).getName());
//                    srcLating =  new LatLng(location.getLatitude(),
//                            location.getLongitude());
//                }
//
//                @Override
//                public void processFirstLoc(BDLocation location) {
//
//                }
//
//                @Override
//                public void processLocFail(BDLocation location) {
//                }
//            }) ;
        }
    }

    @Override
    protected  void dealSuccess(@Nullable BaseModel baseModel){

        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_ROUTE_OPERATE_NET_REQUST){
            showToast("操作成功");
            RouteOperateEvent routeOperateEvent = (RouteOperateEvent) baseModel.getData();
             if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_CHARGING_OPERATE_TYPE){
                 mBooleanOperate[0] = true ;
                 //mBtnStart.setEnabled(false);
            }
            else if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_WAITTING_OPERATE_TYPE){
                 mBooleanOperate[1] = true ;
                 //mBtnWait.setEnabled(false);
            }
            else if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_RESTART_OPERATE_TYPE){
                 mBooleanOperate[1] = false ;
                 mBooleanOperate[2] = false ;
                 //mBtnRestart.setEnabled(true);
                 //mBtnWait.setEnabled(true);
            }
            else if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE){
                startActivity(OrderClearingBaseActivity.newIntent(getContext() , mBaseOrder));
                mEdtDest.setText("");
                mEdtSrc.setText("");
                mRadioBtnDayUse.setChecked(false)      ;
                mRadioBtnTransfer.setChecked(false)    ;
                mRadioBtnHalfDayUse.setChecked(false)  ;
                mBooleanOperate[3] = true ;
                // mBtnCompute.setEnabled(false);
                mBaseOrder = null ;
                getActivity().finish();
            }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(OPERATE_STATUS, mBooleanOperate);
    }
}
