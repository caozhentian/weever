package cn.people.weever.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import cn.people.weever.application.WeeverApplication;
import cn.people.weever.receiver.PowerReceiver;

/**
 * Created by Administrator on 2017/6/22.
 */

public class RegisterPowerService {

    private static      boolean isRegisterPower ;
    static PowerReceiver sPowerReceiver ;
    /**
     * 注册电源锁广播
     */
    public static  void registerPowerReceiver() {
        if (isRegisterPower) {
            return;
        }
        PowerManager powerManager = (PowerManager) WeeverApplication.getInstance().getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");

        sPowerReceiver = new PowerReceiver(wakeLock);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        WeeverApplication.getInstance().registerReceiver(sPowerReceiver, filter);
        isRegisterPower = true;
    }

    public static   void unregisterPowerReceiver() {
        if (!isRegisterPower) {
            return;
        }
        if (null != sPowerReceiver) {
            WeeverApplication.getInstance().unregisterReceiver(sPowerReceiver);
            sPowerReceiver = null ;
        }
        isRegisterPower = false;
    }
}
