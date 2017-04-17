package cn.people.weever.activity.car;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import java.util.ArrayList;

import cn.people.weever.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GprsLocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GprsLocFragment extends Fragment implements OnGetPoiSearchResultListener {
    private static final String ARG_LATITUDE   =  "arg_latitude"  ;
    private static final String ARG_LONGITUDE  =  "arg_longitude" ;
    private static final String ARG_ADDRESS    =   "arg_address"   ;

    private String mLatitude   ;
    private String mLongitude  ;
    private String mAddress    ;

    private ListView lv_location;

    private ArrayList<String> suggest;
    private ArrayList<PoiInfo> dataList = new ArrayList<PoiInfo>();

    private static PoiSearch mPoiSearch;

    private AddressGpsAdapter addressGpsAdapter;


    public GprsLocFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latitude Parameter 1.
     * @param longitude Parameter 2.
     * @return A new instance of fragment GprsLocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GprsLocFragment newInstance(String latitude, String longitude , String address) {
        GprsLocFragment fragment = new GprsLocFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LATITUDE, latitude);
        args.putString(ARG_LONGITUDE, longitude);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatitude = getArguments().getString(ARG_LATITUDE);
            mLongitude = getArguments().getString(ARG_LONGITUDE);
            mAddress  = getArguments().getString(ARG_ADDRESS);
            searchNeayBy() ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gprs_loc, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){

        lv_location = (ListView) view.findViewById(R.id.lv_location);
        addressGpsAdapter = new AddressGpsAdapter(getContext(),dataList);
        lv_location.setAdapter(addressGpsAdapter);
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AddressGpsLocActivity addressGpsLocActivity = new AddressGpsLocActivity();
//            Intent intent = new Intent();
//            intent.putExtra("name",dataList.get(position).name);
//            intent.putExtra("address",dataList.get(position).address);
//            intent.putExtra("province",province);
//            intent.putExtra("city",city);
//            mcontext.setResult(1001,intent);
//            mcontext.finish();
            }
        });
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        // TODO Auto-generated method stub
        // 获取POI检索结果
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(getContext(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            if(poiResult != null){
                if(poiResult.getAllPoi()!= null && poiResult.getAllPoi().size()>0){
                    dataList.addAll(poiResult.getAllPoi());
//                    Toast.makeText(getContext(), dataList.size()+"",Toast.LENGTH_LONG).show();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    public  void searchNeayBy(){
        // POI初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
        poiNearbySearchOption.location(new LatLng(Double.parseDouble(mLatitude),
                Double.parseDouble(mLongitude)));
        poiNearbySearchOption.radius(1000);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
        poiNearbySearchOption.keyword(mAddress);  // distance_from_near_to_far
        poiNearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
                case 0:
                    addressGpsAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
    }
};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
