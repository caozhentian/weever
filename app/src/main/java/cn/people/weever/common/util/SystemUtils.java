package cn.people.weever.common.util;

import android.app.ActivityManager;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by liangzili on 15/8/3.
 */
public class SystemUtils {
    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @return boolean
     */
    public static boolean isAppAlive(Context context){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ManifestUtil.getPackageName(context) ;
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Logger.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Logger.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }


}
