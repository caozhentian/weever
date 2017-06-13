package cn.people.weever.activity.order.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.people.weever.BaseFragment;
import cn.people.weever.R;
import cn.people.weever.activity.order.detail.OrderDetailsBaseActivity;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.QueryModel;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;
import cn.people.weever.widget.PullToRefreshView;


/**
 * Created by Administrator on 2017/5/9.
 */

public class PlaceholderFragment extends BaseFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String  ARG_SECTION_NUMBER    =  "section_number"  ;

    private static final String  ARG_ORDER_STATUS      =  "order_status"     ;

    private int mOrderStatus ;

    @BindView(R.id.pull_refresh_lv)
    PullToRefreshView mPullRefresh_Lv;
    @BindView(R.id.lv)
    ListView mLv;
    Unbinder unbinder;

    private OrderAdapter mOrderAdapter  ;

    private  List<BaseOrder>  mBaseOrderList ;

    private OrderService      mOrderService  ;

    private QueryModel        mQueryModel ;

    public PlaceholderFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber , int orderStaus) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_ORDER_STATUS, orderStaus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mOrderStatus = getArguments().getByte(ARG_ORDER_STATUS) ;
        mOrderService = new OrderService() ;
        mQueryModel   = new QueryModel()     ;
        mBaseOrderList = new LinkedList<>() ;
        mOrderAdapter = new OrderAdapter(this.getContext() , mBaseOrderList) ;
        mLv.setAdapter(mOrderAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(OrderDetailsBaseActivity.newIntent(getContext() , mBaseOrderList.get(position)));
            }
        });

        mPullRefresh_Lv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                mQueryModel.addPage(1);
                queryOrderList() ;
                mPullRefresh_Lv.onFooterRefreshComplete();
            }
        });
        mPullRefresh_Lv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                mQueryModel.resetPage();
                queryOrderList() ;
                mPullRefresh_Lv.onHeaderRefreshComplete();
            }
        });
        queryOrderList() ;
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void  queryOrderList(){
        mOrderService.list(mOrderStatus , mQueryModel ) ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processBaseOrderEvent(@Nullable BaseModel<List<BaseOrder>> baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_LIST_NET_REQUST){
            List<BaseOrder> baseOrderList = baseModel.getData() ;
            if(mQueryModel != null && mQueryModel.isFirstPage()){
                mBaseOrderList.clear();
            }
            mBaseOrderList.addAll(baseOrderList) ;
        }
    }
}
