package cn.people.weever.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.people.weever.R;
import cn.people.weever.common.timer.ITimerExecute;
import cn.people.weever.common.timer.TimerByHandler;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.RealTimeOrderInfo;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.service.OrderService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavHeadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("NewApi")
public class NavHeadFragment extends SubscribeResumePauseBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.txtAllDistance)
    TextView mTxtAllDistance;
    @BindView(R.id.txtAllTime)
    TextView mTxtAllTime;
    @BindView(R.id.txtAllWaitTime)
    TextView mTxtAllWaitTime;
    @BindView(R.id.txtAllWaitAmount)
    TextView mTxtAllWaitAmount;
    @BindView(R.id.ll_top)
    LinearLayout mLlTop;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private BaseOrder mBaseOrder;

    private TimerByHandler mTimerByHandler;

    private OrderService mOrderService;

    private OnFragmentInteractionListener mListener;

    public NavHeadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavHeadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavHeadFragment newInstance(BaseOrder baseOrder) {
        NavHeadFragment fragment = new NavHeadFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, baseOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBaseOrder = (BaseOrder) getArguments().getSerializable(ARG_PARAM1);
        }
        mOrderService = new OrderService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_head, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void realtime() {
        mTimerByHandler = new TimerByHandler(new ITimerExecute() {
            @Override
            public void onExecute() {
                if (mBaseOrder != null) {
                    mOrderService.getRealTimeOrderInfo(mBaseOrder);
                }
            }
        });
        mTimerByHandler.start();
    }

    @Override
    protected void dealSuccess(@Nullable BaseModel baseModel) {
        if (baseModel.getApiOperationCode() == OrderApiService.TO_ORDER_REAL_TIME_INFO_NET_REQUST) {
            showToast("操作成功");
            RealTimeOrderInfo realTimeOrderInfo = (RealTimeOrderInfo) baseModel.getData();
            mTxtAllWaitTime.setText(realTimeOrderInfo.getWaitTime());
            mTxtAllWaitAmount.setText(realTimeOrderInfo.getWaitCost());
        }
    }
}
