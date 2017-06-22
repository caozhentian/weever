package cn.people.weever.service;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.OnTraceListener;

import java.util.concurrent.atomic.AtomicInteger;

import cn.people.weever.application.WeeverApplication;
import cn.people.weever.common.util.PreferencesUtil;

/**
 * Created by Administrator on 2017/6/22.
 */

public class TraceService {

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
//        return entityName; TODO TESTCODE
        return  "myTrace" ;
    }

    public  void setEntityName(String entityName) {
        this.entityName = entityName;
        PreferencesUtil.setPreferences(WeeverApplication.getInstance(),"CAR_KEY",entityName);
        mTrace.setEntityName(entityName);
    }

    public void startTrace(OnTraceListener traceListener){
        mClient.stopTrace(mTrace , traceListener);
    }
    public void stopTrace() {
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
