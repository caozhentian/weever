package cn.people.weever.activity.nav;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.application.WeeverApplication;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.fragment.NavFooterFragment;
import cn.people.weever.map.TraceService;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RouteOperateEvent;

import static com.baidu.mapapi.BMapManager.getContext;


/**
 * 诱导界面
 * 
 * @author sunhao04
 *
 */
public class BNDemoGuideActivity extends BaseActivity {

    public static final String ROUTE_PLAN_NODE = "ROUTE_PLAN_NODE" ;
    public static final String BASE_ORDER = "BASE_ORDER" ;
    private final String TAG = BNDemoGuideActivity.class.getName();
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;
    private BaseOrder mBaseOrder ;

    private NavFooterFragment mNavFooterFragment ;

    /*
     * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
     * 
     */
    // 是否使用通用接口
    private boolean useCommonInterface = true;

    private boolean isNavEnd;

    public static final Intent newIntent(Context packageContext , BNRoutePlanNode bNRoutePlanNode,
                                         BaseOrder baseOrder){
        Intent intent = new Intent(packageContext , BNDemoGuideActivity.class) ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROUTE_PLAN_NODE, bNRoutePlanNode);
        bundle.putSerializable(BASE_ORDER, baseOrder);
        intent.putExtras(bundle);
        return intent ;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        ImageView iv_back = (ImageView)findViewById(R.id.img_back);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNavEnd){
                    finish();
                    return ;
                }
                if(mBaiduNaviCommonModule != null) {
                    mBaiduNaviCommonModule.onBackPressed(true);
                    //finish() ;
                } ;
//                OKCancelDlg.createCancelOKDlg(BNDemoGuideActivity.this, "确定退出导航吗", new ICancelOK() {
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void ok() {
//
//                        if(mBaiduNaviCommonModule != null) {
//                            mBaiduNaviCommonModule.onBackPressed(true);
//                            //finish() ;
//                        } ;
//                    }
//                });
            }
        });
        tv_title.setText("导航");
        createHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        View view = null;
        if (useCommonInterface) {
            //使用通用接口
            mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
                    NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
                    BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onCreate();
                view = mBaiduNaviCommonModule.getView();
            }
            
        } else {
            //使用传统接口
            view = BNRouteGuideManager.getInstance().onCreate(this,mOnNavigationListener);
        }

        initFragment();
        LinearLayout llContent= (LinearLayout) findViewById(R.id.ll_nav_content) ;

        if (view != null) {
            llContent.addView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(ROUTE_PLAN_NODE);
            }
        }
        //显示自定义图标
        if (hd != null) {
            hd.sendEmptyMessageAtTime(MSG_SHOW, 5000);
        }
        
        BNEventHandler.getInstance().getDialog(this);
        // BNEventHandler.getInstance().showDialog();
    }

    private void initFragment(){
        Bundle bundle = getIntent().getExtras();
        mBaseOrder =  (BaseOrder) bundle.getSerializable(BASE_ORDER);
        if(mBaseOrder != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(fragmentManager.findFragmentById(R.id.fl_nav_footer) == null){
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                mNavFooterFragment = NavFooterFragment.newInstance(mBaseOrder) ;
                fragmentTransaction.add(R.id.fl_nav_footer , mNavFooterFragment) ;
                fragmentTransaction.commit();
            }
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onResume();
            }
        } else {
            BNRouteGuideManager.getInstance().onResume();
        }
        
      
     
    }

    protected void onPause() {
        super.onPause();
        
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onPause();
            }
        } else {
            BNRouteGuideManager.getInstance().onPause();
        }
      
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onDestroy();
            }
        } else {
            BNRouteGuideManager.getInstance().onDestroy();
        }
        BNEventHandler.getInstance().disposeDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStop();
            }
        } else {
            BNRouteGuideManager.getInstance().onStop();
        }
       
    }

    /*/
     * (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     * 此处onBackPressed传递false表示强制退出，true表示返回上一级，非强制退出
     */
    @Override
    public void onBackPressed() {
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onBackPressed(true);
                //finish() ;
            }
        } else {
            BNRouteGuideManager.getInstance().onBackPressed(false);
        }
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
            }
        } else {
            BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        }

    };
    
    
    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                Bundle mBundle = new Bundle();
                mBundle.putInt(RouteGuideModuleConstants.KEY_TYPE_KEYCODE, keyCode);
                mBundle.putParcelable(RouteGuideModuleConstants.KEY_TYPE_EVENT, event);
                mBaiduNaviCommonModule.setModuleParams(RouteGuideModuleConstants.METHOD_TYPE_ON_KEY_DOWN, mBundle);
                try {
                    Boolean ret = (Boolean)mBundle.get(RET_COMMON_MODULE);
                    if(ret) {
                        return true;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } 
        return super.onKeyDown(keyCode, event);  
    }
    @Override
    protected void onStart() {
        super.onStart();
        // TODO Auto-generated method stub
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStart();
            }
        } else {
            BNRouteGuideManager.getInstance().onStart();
        }
    }
    private void addCustomizedLayerItems() {
        List<CustomizedLayerItem> items = new ArrayList<CustomizedLayerItem>();
        CustomizedLayerItem item1 = null;
        if (mBNRoutePlanNode != null) {
            item1 = new CustomizedLayerItem(mBNRoutePlanNode.getLongitude(), mBNRoutePlanNode.getLatitude(),
                    mBNRoutePlanNode.getCoordinateType(), getResources().getDrawable(R.drawable.ic_launcher),
                    CustomizedLayerItem.ALIGN_CENTER);
            items.add(item1);

            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
                                new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11", null, CoordinateType.GCJ02));
                    }
                };
            };
        }
    }

    private void showSingleChoiceDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择操作");
        //builder.setIcon(R.mipmap.ic_launcher);
        String[] single_list = {"返回" ,"结算" ,"停留在当前界面" } ;
        builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(which == 0){
                      finish();
                }
                else if(which == 1){
                    mNavFooterFragment.compute();
                }
                else{

                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            //退出导航
            Logger.i(TAG, "navi 导航结束");
            isNavEnd = true ;
            if(mBaseOrder != null){
                showSingleChoiceDialog() ;
            }
            else {
                finish();
            }
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
            
            if (actionType == 0) {
                //导航到达目的地 自动退出
                if(mBaseOrder != null){
                    showSingleChoiceDialog() ;
                }
                else{
                    
                }
                Logger.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
            }

            Logger.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }

    };
    
    private final static String RET_COMMON_MODULE = "module.ret";
    
    private interface RouteGuideModuleConstants {
        final static int METHOD_TYPE_ON_KEY_DOWN = 0x01;
        final static String KEY_TYPE_KEYCODE = "keyCode";
        final static String KEY_TYPE_EVENT = "event";
    }
}
