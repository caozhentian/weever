package com.example.jpushdemo;

import android.content.Context;
import android.os.Handler;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.people.weever.BuildConfig;

/**
 * Created by Administrator on 2017/5/4.
 */

public class JPushService {

    public static final String MESSAGE_RECEIVED_ACTION = "MESSAGE_RECEIVED_ACTION" ;

    public static final  void    initJPush(Context applicationContext){
        JPushInterface.init(applicationContext);
        JPushInterface.setDebugMode(BuildConfig.DEBUG) ;
    }

    public static final  void    stopJPush(Context applicationContext){
        JPushInterface.stopPush(applicationContext);
    }

    public static final void setAlias(Context applicationContext ,String alias){
        JPushInterface.setAliasAndTags(applicationContext , alias, null, mTagAliasCallback);
    }

    /**
     *
     * @param applicationContext
     * @param tag 逗号分割的字符串
     */
    public static final void setTag(Context applicationContext ,String tag){

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            tagSet.add(sTagItme);
        }
        JPushInterface.setAliasAndTags(applicationContext , null, JPushInterface.filterValidTags(tagSet), mTagAliasCallback);
    }

    public static final void setAliasTags(Context applicationContext ,String alias ,String tag ){
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            tagSet.add(sTagItme);
        }
        JPushInterface.setAliasAndTags(applicationContext , alias, JPushInterface.filterValidTags(tagSet), mTagAliasCallback);
    }

    public static final void setAliasAndTags(Context applicationContext ,String andTag ,String andAlias){
        // ","隔开的多个 转换成 Set
        String[] sArray = andTag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            tagSet.add(sTagItme);
        }
        JPushInterface.setAliasAndTags(applicationContext,
                andTag , JPushInterface.filterValidTags(tagSet), mTagAliasCallback);
    }

    private static final TagAliasCallback mTagAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    //持久化保存Tag 避免重复设置
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
            }
        }

    };

    private static final int MSG_SET_ALIAS = 1001;
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    //Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    Context applicationContext = null ;
                    JPushInterface.setAliasAndTags(applicationContext,
                            (String) msg.obj,
                            null,
                            mTagAliasCallback);
                    break;
                default:
                    //Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
