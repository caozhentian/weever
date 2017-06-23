package cn.people.weever.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import cn.people.weever.BuildConfig;

import static com.orhanobut.logger.Logger.ERROR;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LogUtil {

    public final static void  initLog(){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
                .tag("PRETTY_LOGGER")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();
         Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
             @Override public boolean isLoggable(int priority, String tag) {
                 if(BuildConfig.DEBUG) {
                     return BuildConfig.DEBUG;
                 }
                 else{
                     return priority >= ERROR ;
                 }
             }
         }) ;
        Logger.addLogAdapter(new DiskLogAdapter(){
            @Override public boolean isLoggable(int priority, String tag) {
                if( BuildConfig.DEBUG ){
                    return true ;
                }
                else{
                    return priority >= ERROR ;
                }
            }
        });
    }
}
