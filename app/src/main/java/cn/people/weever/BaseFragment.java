package cn.people.weever;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/9.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
