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
import cn.people.weever.R;
import cn.people.weever.activity.order.detail.OrderDetailsBaseActivity;
import cn.people.weever.event.OrderStatusChangeEvent;
import cn.people.weever.fragment.BaseFragment;
import cn.people.weever.fragment.SubscribeResumePauseBaseFragment;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.QueryModel;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;
import cn.people.weever.widget.PullToRefreshView;


/**
 * Created by Administrator on 2017/5/9.
 */

public class PlaceholderFragment extends SubscribeResumePauseBaseFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String  ARG_SECTION_NUMBER    =  "section_number"  ;

    private static final String  ARG_ORDER_STATUS      =  "order_status"     ;

    private int mOrderStatus ;

    private int mSectionNumber ;

    @BindView(R.id.pull_refresh_lv)
    PullToRefreshView mPullRefresh_Lv;
    @BindView(R.id.lv)
    ListView mLv;
    Unbinder unbinder;

    private  List<BaseOrder>  mBaseOrderList  = new LinkedList<>() ;
    private OrderAdapter mOrderAdapter    ;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mSectionNumber =  getArguments().getInt(ARG_SECTION_NUMBER) ;
        mOrderStatus = getArguments().getInt(ARG_ORDER_STATUS) ;
        mOrderService = new OrderService() ;
        mQueryModel   = new QueryModel()     ;
        mOrderAdapter    = new OrderAdapter(this.getContext() , mBaseOrderList) ;
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
        //queryOrderList() ;
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
    public void dealSuccess(@Nullable BaseModel baseModel){
        if(baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_LIST_NET_REQUST){
            List<BaseOrder> baseOrderList = (List<BaseOrder>) baseModel.getData();
            if(mQueryModel != null && mQueryModel.isFirstPage()){
                mBaseOrderList.clear();
            }
            if(baseOrderList == null || baseOrderList.size() == 0){
                return ;
            }
            mBaseOrderList.addAll(baseOrderList) ;
            if(mOrderAdapter != null) {
                mOrderAdapter.notifyDataSetChanged();
            }
        }
    }

    //@Subscribe(sticky = true ,threadMode = ThreadMode.MAIN)
    public void dealSuccess(@Nullable OrderStatusChangeEvent orderStatusChangeEvent){
        mQueryModel.resetPage();
        queryOrderList() ;
    }

    //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            //EventBus.getDefault().register(this);
            mSectionNumber =  getArguments().getInt(ARG_SECTION_NUMBER) ;
            mOrderStatus = getArguments().getInt(ARG_ORDER_STATUS) ;
            mOrderService = new OrderService() ;
            mQueryModel   = new QueryModel()     ;
            queryOrderList() ;
        } else {
           // EventBus.getDefault().unregister(this);
        }
    }
}
