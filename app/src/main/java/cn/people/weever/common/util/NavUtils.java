package cn.people.weever.common.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.nav.BNDemoGuideActivity;
import cn.people.weever.activity.nav.BNEventHandler;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.config.FileConfig;
import cn.people.weever.model.BaseOrder;

/**
 * Created by Administrator on 2017/6/21.
 */

public class NavUtils {

    public static  boolean hasInitSuccess = false;
    public static final String TTS_APP_ID = "9458069";

    public static void initNavi(BaseActivity context) {

        if(hasInitSuccess){
            return ;
        }
        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(context, FileConfig.S_SDCardPath, FileConfig.APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {

                if (0 == status) {
                    Logger.d("key校验成功!");
                } else {
                    Logger.e("key校验失败, " + msg);
                }

            }

            public void initSuccess() {
                Logger.d("百度导航引擎初始化成功!");
                hasInitSuccess = true ;
                initSetting();
            }

            public void initStart() {
                Logger.d("百度导航引擎初始化成功!");
            }

            public void initFailed() {
                Logger.e("百度导航引擎初始化失败");
            }

        }, null, ttsHandler, ttsPlayStateListener);
    }

    public static  void routeplanToNavi(BaseActivity context ,LatLng srcLating , String srcAddress , LatLng destLating , String destAddress ,
                                   BaseOrder baseOrder ) {

        if (!NavUtils.hasInitSuccess) {
            Toast.makeText(WeeverApplication.getInstance(), "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(srcLating.longitude, srcLating.latitude, srcAddress   , null, BNRoutePlanNode.CoordinateType.BD09LL);
        eNode = new BNRoutePlanNode(destLating.longitude, destLating.latitude, destAddress, null, BNRoutePlanNode.CoordinateType.BD09LL);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            BaiduNaviManager.getInstance().launchNavigator(context, list, 1, true, new DemoRoutePlanListener(sNode ,baseOrder),
                    eventListerner);
        }
    }

    public static class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        private BaseOrder mBaseOrder  ;

        public DemoRoutePlanListener(BNRoutePlanNode node ,BaseOrder baseOrder) {
            mBNRoutePlanNode = node ;
            mBaseOrder = baseOrder   ;
        }

        @Override
        public void onJumpToNavigator() {
            WeeverApplication.getInstance().startActivity(
                    BNDemoGuideActivity.newIntent(WeeverApplication.getInstance() ,mBNRoutePlanNode , mBaseOrder));
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(WeeverApplication.getInstance(), "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
    static  BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };
    /**
     * 内部TTS播报状态回传handler
     */
    private static  Handler ttsHandler = new Handler() {
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
    private static BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private static  void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, TTS_APP_ID);
        BNaviSettingManager.setNaviSdkParam(bundle);
    }
}
