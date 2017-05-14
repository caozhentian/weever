package com.example.administrator.firstapplication.tabs;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class AddViewUtils {

    public static final void addView(Context context , LinearLayout ll ,List<String> contents){

        for( String content : contents) {
            final TextView showText = new TextView(context);
            showText.setTextColor(Color.GREEN);
            showText.setTextSize(30);
            showText.setText(content);
            showText.setBackgroundColor(Color.GRAY);
            // set 文本大小
            LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //set 四周距离
            paramsTv.setMargins(10, 10, 10, 10);

            showText.setLayoutParams(paramsTv);

            //添加文本到主布局
            ll.addView(showText);
        }
    }
}
