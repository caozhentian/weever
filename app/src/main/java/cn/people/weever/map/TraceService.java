package cn.people.weever.map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.PreferencesUtil;
import cn.people.weever.common.util.ToastUtil;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.service.RegisterPowerService;

/**
 * Created by Administrator on 2017/6/22.
 */

public class TraceService {

    public static  boolean IS_START_TRACE = false ;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private static  TraceService sTraceService ;
    /**
     * 轨迹客户端
     */
    public  LBSTraceClient mClient = null;
    /**
     * 轨迹服务
     */
    public  Trace mTrace = null;

    /**
     * 轨迹服务ID
     */
    public  long serviceId = 140822;

    /**
     * Entity标识
     */
    private   String entityName = "myTrace";
    private Object  objLock = new Object();

    private TraceService(Context context) {
        mClient = new LBSTraceClient(context);
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mClient.setInterval(gatherInterval, packInterval);
        entityName = PreferencesUtil.getStringPreferences(WeeverApplication.getInstance() , "CAR_KEY") ;
        mTrace = new Trace(serviceId, entityName);
    }


    public static final TraceService getInstance(Context context){
        if(sTraceService == null){
            sTraceService = new TraceService(context) ;
        }
        return sTraceService ;
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    public  String getEntityName() {
        if(TextUtils.isEmpty(entityName)){
            entityName = PreferencesUtil.getStringPreferences(WeeverApplication.getInstance() , "CAR_KEY") ;
        }
       return entityName;
       // return  "myTrace" ;
    }

    public  void setEntityName(String entityName) {
        this.entityName = entityName;
        PreferencesUtil.setPreferences(WeeverApplication.getInstance(),"CAR_KEY",entityName);
        mTrace.setEntityName(entityName);
    }

    public void startTrace(OnTraceListener traceListener){
        if(IS_START_TRACE){
            return ;
        }
        if(traceListener == null){
            startTrace() ;
            return ;
        }
        mClient.startTrace(mTrace , traceListener);
    }

    public void startTrace(){
        mClient.startTrace(mTrace , new OnTraceListener() {
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    RegisterPowerService.registerPowerReceiver();
                    Logger.d(String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                    Log.e("ABC",String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                    IS_START_TRACE = true ;
                    ToastUtil.showToast("开启轨迹服务成功");
                }
                else{
                    //Logger.e(String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                    Log.e("ABC2",String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                    ToastUtil.showToast("无法开启轨迹服务");
                }
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                Logger.d(String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    RegisterPowerService.unregisterPowerReceiver();
                    ToastUtil.showToast("停止轨迹服务");
                }
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                Logger.d(String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    ToastUtil.showToast("开启采集轨迹");
                }
                else{
                    ToastUtil.showToast("无法开启采集轨迹");
                }
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                Logger.d(String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    ToastUtil.showToast("停止采集轨迹");
                }
                else{

                }
            }
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }
        });
    }
    public void stopTrace() {
        if(!IS_START_TRACE){
            return ;
        }
        IS_START_TRACE = false ;
        mClient.stopTrace(mTrace, null);
    }

    public void startGather(OnTraceListener traceListener){
        mClient.startGather(traceListener);
    }
    public void stopGather(OnTraceListener traceListener){
        mClient.stopGather(traceListener);
    }

    public void queryHistoryTrack(HistoryTrackRequest historyTrackRequest , OnTrackListener onTrackListener){
        mClient.queryHistoryTrack(historyTrackRequest , onTrackListener);
    }
}
