package cn.people.weever.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import cn.people.weever.application.ActivityExitManage;

/**
 * Created by Administrator on 2017/6/9.
 */

public abstract class BaseFragment extends Fragment {

    protected  int mApiOperationCode ;

    protected boolean mIsRegisterEventBus ;

    protected boolean mIsUnRegisterEventBus ;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(mIsRegisterEventBus == false) {
            mIsRegisterEventBus = true;
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mIsUnRegisterEventBus == false) {
            mIsUnRegisterEventBus = true ;
            EventBus.getDefault().unregister(this);
        }

    }

    public int getApiOperationCode() {
        return mApiOperationCode;
    }

    public void setApiOperationCode(int apiOperationCode) {
        mApiOperationCode = apiOperationCode;
    }

    @Override
    public void onResume() {
        super.onResume();
        //ActivityExitManage.setCurBaseFragment( this)  ;
    }

    @Override
    public void onPause() {
        super.onPause();
        //ActivityExitManage.setCurBaseFragment( null)  ;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ActivityExitManage.setCurBaseFragment( this)  ;
            if(mIsRegisterEventBus == false) {
                mIsRegisterEventBus = true;
                EventBus.getDefault().register(this);
            }
        }
        else{
            ActivityExitManage.setCurBaseFragment( null)  ;
            if(mIsUnRegisterEventBus == false) {
                mIsUnRegisterEventBus = true ;
                EventBus.getDefault().unregister(this);
            }
        }
    }
}
