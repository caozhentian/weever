package cn.people.weever.activity.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;


/**
 *   关于我们
 * Created by Administrator on 2017/1/10 0010.
 */

public class AboutWeActivity extends BaseActivity {

    private ImageView  iv_back;
    private TextView   tv_title;
    private TextView  tv_introduce;
    private WebView wv_about_we;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_we);
        initView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        iv_back = (ImageView)findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_introduce = (TextView) findViewById(R.id.tv_introduce);
        wv_about_we = (WebView) findViewById(R.id.wv_about_we);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutWeActivity.this.finish();
            }
        });
        tv_title.setText("关于我们");
        wv_about_we.loadUrl("http://yhsj.3tichina.com/h5/about.php");
    }
}
