package cn.people.weever.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/23.
 */

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }
}
