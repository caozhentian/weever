package cn.people.weever.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.receiver.PowerReceiver;
import cn.people.weever.service.OrderService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavFooterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NewApi")
public class NavFooterFragment extends SubscribeResumePauseBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
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
    Unbinder unbinder;

    private RouteOperateEvent mRouteOperateEvent = new RouteOperateEvent();

    private OrderService mOrderService ;
    private BaseOrder mBaseOrder;

    private LatLng srcLating ;
    private LatLng destLating ;

    private OnFragmentInteractionListener mListener;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private PowerReceiver powerReceiver = null;

    public NavFooterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavHeadFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        startTrace() ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WeeverApplication.getInstance().mClient.stopTrace(WeeverApplication.mTrace, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_footer, container, false);
        unbinder = ButterKnife.bind(this, view);
        initVar();
        initListener() ;
        return view;
    }

    private void initVar(){
        if (getArguments() != null) {
            mBaseOrder = (BaseOrder) getArguments().getSerializable(ARG_PARAM1);
        }
        setOrder(mBaseOrder) ;
        mOrderService = new OrderService() ;

        powerManager =  (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionRoutineListener) {
//            mListener = (OnFragmentInteractionRoutineListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionRoutineListener");
//        }
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
        OKCancelDlg.createCancelOKDlg(getContext(), "确定开始计费吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                // 设置起终点信息，对于tranist search 来说，城市名无意义
                mRouteOperateEvent.setTripNode(WeeverApplication.getInstance().getCurTripNode());
                mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_CHARGING_OPERATE_TYPE);
                mOrderService.routeOperateOrder(mRouteOperateEvent);
                PlanNode stNode = PlanNode.withLocation(srcLating)   ;
                PlanNode enNode = PlanNode.withLocation(destLating)  ;
                WeeverApplication.mClient.startGather(traceListener);
            }
        });

    }

    private void initListener() {
        traceListener = new OnTraceListener() {
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    registerPowerReceiver();
                }
                Logger.d(String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
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

                }
                else{
                }
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {

                }
                else{

                }
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }
        };
    }

    private void waitting(){
        if(mBaseOrder == null){
            return ;
        }
        mRouteOperateEvent.setTripNode(WeeverApplication.getInstance().getCurTripNode());
        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_WAITTING_OPERATE_TYPE);
        mOrderService.routeOperateOrder(mRouteOperateEvent);
    }

    private  void restart(){
        if(mBaseOrder == null){
            return ;
        }
        mRouteOperateEvent.setTripNode(WeeverApplication.getInstance().getCurTripNode());
        mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_RESTART_OPERATE_TYPE);
        mOrderService.routeOperateOrder(mRouteOperateEvent);
    }

    private void compute(){
        if(mBaseOrder == null){

            return ;
        }
        OKCancelDlg.createCancelOKDlg(getContext(), "确定结算吗", new ICancelOK() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok() {
                mRouteOperateEvent.setTripNode(WeeverApplication.getInstance().getCurTripNode());
                mRouteOperateEvent.setOperateType(RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE);
                mOrderService.routeOperateOrder(mRouteOperateEvent);
                WeeverApplication.mClient.stopGather(traceListener);
            }
        });

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
        getContext().registerReceiver(powerReceiver, filter);
        WeeverApplication.isRegisterPower = true;
    }

    private void unregisterPowerReceiver() {
        if (!WeeverApplication.isRegisterPower) {
            return;
        }
        if (null != powerReceiver) {
            getContext().unregisterReceiver(powerReceiver);
        }
        WeeverApplication.isRegisterPower = false;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void setLocationSrc(String name){
        if(mBaseOrder == null){
            mEdtSrc.setText(name);
        }
    }

    @Override
    protected  void dealSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_ROUTE_OPERATE_NET_REQUST){
            showToast("操作成功");
            RouteOperateEvent routeOperateEvent = (RouteOperateEvent) baseModel.getData();
            if(routeOperateEvent.getOperateType() == RouteOperateEvent.TO_ORDER_TO_SETTLEMENT_OPERATE_TYPE){
                startActivity(OrderClearingBaseActivity.newIntent(getContext() , mBaseOrder));
                mEdtDest.setText("");
                mEdtSrc.setText("");
                mRadioBtnDayUse.setChecked(false)      ;
                mRadioBtnTransfer.setChecked(false)    ;
                mRadioBtnHalfDayUse.setChecked(false)  ;
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

}
