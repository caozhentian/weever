package cn.people.weever.activity.poi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import cn.people.weever.R;
import cn.people.weever.activity.BaseActivity;
import cn.people.weever.activity.car.AddressGpsAdapter;
import cn.people.weever.service.AddressService;

/**
 * 演示poi搜索功能
 */
public class PoiSearchActivity extends BaseActivity implements
        OnGetPoiSearchResultListener {

	public static final String SRC  =  "src" ;
	
    private PoiSearch    mPoiSearch =  null   ;
  
    List<PoiInfo>        mPoiInfoList          ;
    private ImageView    iv_clear_searchtext   ;
    private ImageView    img_back ;
    private TextView     tv_title ;
    private View         ll_tabs ,ll_going,ll_completed  ,
                         view_going ,  view_completed ,tv_going ;
    /**
     * 搜索关键字输入窗口
     */
    private EditText editCity = null;
    private AutoCompleteTextView keyWorldsView = null;
    
    private int loadIndex = 0;
    
    private boolean mSrc = false ;
    
    private ListView lv_location ; 
    
    private AddressGpsAdapter mAddressGpsAdapter ;

    private AddressService mAddressService ;

    public static final Intent newIntent(Context packageContext , boolean src ){
    	Intent intent = new Intent(packageContext , PoiSearchActivity.class) ;
    	intent.putExtra(SRC, src) ;
    	return intent ;
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch2);
        initView() ;
        initData();
    }

    @Override
    public void initData() {
        mAddressService  = new AddressService() ;
    }

    public void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_back  = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSrc = getIntent().getBooleanExtra(SRC, false) ;
        if(mSrc){
            tv_title.setText("选择出发地");
        }
        else{
            tv_title.setText("选择目的地");
    	 }
    	 ll_tabs      = findViewById(R.id.ll_tabs)  ;
    	 ll_going     = findViewById(R.id.ll_going) ;
    	 tv_going     = findViewById(R.id.tv_going) ;
    	 ll_going.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_going.setSelected(true);
                view_going.setVisibility(View.VISIBLE);
                ll_completed.setSelected(false);
                view_completed.setVisibility(View.INVISIBLE);
			}
		 }) ;
         ll_completed = findViewById(R.id.ll_completed);
         ll_completed.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				tv_going.setSelected(false);
                view_going.setVisibility(View.INVISIBLE);
                ll_completed.setSelected(true);
                view_completed.setVisibility(View.VISIBLE);
 			}
 		 }) ;
         view_going = (View) findViewById(R.id.view_going);
         view_completed = (View) findViewById(R.id.view_completed);
         // 初始化搜索模块，注册搜索事件监听
         mPoiSearch = PoiSearch.newInstance();
         mPoiSearch.setOnGetPoiSearchResultListener(this);

         iv_clear_searchtext =  (ImageView) findViewById(R.id.iv_clear_searchtext) ;
         editCity = (EditText) findViewById(R.id.city) ;
         
         keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
       
         /**
          * 当输入关键字变化时，动态更新建议列表
          */
         keyWorldsView.addTextChangedListener(new TextWatcher() {

             @Override
             public void afterTextChanged(Editable arg0) {

             }

             @Override
             public void beforeTextChanged(CharSequence arg0, int arg1,
                                           int arg2, int arg3) {

             }

             @Override
             public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                       int arg3) {
                 if (cs.length() <= 0) {
                 	iv_clear_searchtext.setVisibility(View.GONE) ;
                 	ll_tabs.setVisibility(View.VISIBLE) ;
                 	mPoiInfoList.clear() ;
     				//加入其它地址
                    mPoiInfoList.addAll(mAddressService.queryAll(mSrc)) ;
     				mAddressGpsAdapter.notifyDataSetChanged() ;
                    return;
                 }

                 /**
                  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                  */
                 searchButtonProcess() ;
                 iv_clear_searchtext.setVisibility(View.VISIBLE) ;
             }
         });
         
         iv_clear_searchtext.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				keyWorldsView.setText("")  ;
 				iv_clear_searchtext.setVisibility(View.GONE) ;
 				ll_tabs.setVisibility(View.VISIBLE) ;
 				mPoiInfoList.clear() ;
 				//加入其它地址
 				mAddressGpsAdapter.notifyDataSetChanged() ;
 			}
 		});

        lv_location = (ListView)findViewById(R.id.lv_location);
        mPoiInfoList = new LinkedList<PoiInfo>();
        mPoiInfoList.addAll(mAddressService.queryAll(mSrc)) ;
        mAddressGpsAdapter = new AddressGpsAdapter(this,mPoiInfoList);
        lv_location.setAdapter(mAddressGpsAdapter);
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	PoiInfo poiInfo = mPoiInfoList.get(position) ;
                EventBus.getDefault().post(new AddressSelectVM(mSrc ,poiInfo ));
                mAddressService.save(poiInfo,mSrc);
                finish() ;
            }
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 响应城市内搜索按钮点击事件
     *
     * @param
     */
    public void searchButtonProcess() {
        String citystr = editCity.getText().toString();
        String keystr = keyWorldsView.getText().toString();
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(citystr).keyword(keystr).pageNum(loadIndex));
    }

    public void goToNextPage(View v) {
        loadIndex++;
        searchButtonProcess();
    }

   

   


    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     * @param result
     */
    public void onGetPoiResult(PoiResult result) {
    	mPoiInfoList.clear() ;
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
         
        }
        else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	mPoiInfoList.addAll(result.getAllPoi()) ;
        }
        else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
        }
        ll_tabs.setVisibility(View.GONE)          ;
        mAddressGpsAdapter.notifyDataSetChanged() ;
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * @param result
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
       
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

}
