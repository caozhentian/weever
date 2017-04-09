package cn.people.weever.common.util;

import android.widget.Toast;

import cn.people.weever.application.WeeverApplication;

/**
 * Created by Administrator on 2017/4/8.
 */

public class ToastUtil {

    public static final void showToast(String text){
        Toast.makeText(WeeverApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

}
