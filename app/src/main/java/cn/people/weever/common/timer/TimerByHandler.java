package cn.people.weever.common.timer;

import android.os.Handler;

/**
 * Created by Administrator on 2017/6/20.
 */

public class TimerByHandler {

    //执行的任务
    private ITimerExecute mITimerExecute ;

    //执行的时间间隔 以秒为单位, 默认值1分钟
    private long  mSecond  = 6000;

    private Handler mHandler ;

    private Runnable mRunnable ;
    public TimerByHandler(ITimerExecute ITimerExecute, long second, Handler handler) {
        if(second <=0){
            throw new IllegalArgumentException("second <= 0") ;
        }
        mITimerExecute = ITimerExecute;
        mSecond = second;
        if(mHandler != null) {
            mHandler = handler;
        }
        else{
            mHandler = new Handler();
        }
    }

    public TimerByHandler(ITimerExecute ITimerExecute) {
        if(ITimerExecute == null ){
            throw new IllegalArgumentException("ITimerExecute == null") ;
        }
        mITimerExecute = ITimerExecute;
        mHandler = new Handler();
    }

    public void  start(){
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mITimerExecute.onExecute()  ;
                //Log.e("time", "here")       ;
                mHandler.postDelayed(this, mSecond * 1000);
            }
        };
        mHandler.postDelayed(mRunnable, mSecond * 1000);
    }

    public void stop(){
       if(mRunnable != null){
           mHandler.removeCallbacks(mRunnable);
       }
    }
}
