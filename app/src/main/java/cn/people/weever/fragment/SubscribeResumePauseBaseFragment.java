package cn.people.weever.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketTimeoutException;

import cn.people.weever.R;
import cn.people.weever.activity.login.LoginActivity;
import cn.people.weever.application.ActivityExitManage;
import cn.people.weever.dialog.CustomProgressDialog;
import cn.people.weever.dialog.ICancelOK;
import cn.people.weever.dialog.OKCancelDlg;
import cn.people.weever.event.NetRequestPostEvent;
import cn.people.weever.event.NetRequestPreEvent;
import cn.people.weever.net.APIError;
import cn.people.weever.net.APIFail;
import cn.people.weever.net.BaseModel;

/**
 * Created by Administrator on 2017/6/16.
 */

public class SubscribeResumePauseBaseFragment extends BaseFragment {

    protected CustomProgressDialog mCustomProgressDialog;

    public void showToast(String text){
        Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int res){
        Toast.makeText(this.getContext(), getContext().getResources().getText(res), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processNetRequestPreEvent(@Nullable NetRequestPreEvent netRequestPreEvent){
        mCustomProgressDialog = new CustomProgressDialog(this.getContext() , R.style.progress_dialog) ;
        mCustomProgressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processNetRequestPostEvent(@Nullable NetRequestPostEvent netRequestPostEvent){
        mCustomProgressDialog.cancel();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processSuccessEvent(@NonNull BaseModel baseModel) {
        if(baseModel.isSuccess()){
            showToast(baseModel.getMessage());
            dealSuccess(baseModel) ;
        }
        else{
            showToast(baseModel.getMessage());
            return ;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processErrorEvent(@NonNull APIError apiErrorError) {
        if(apiErrorError.getThrowable() instanceof SocketTimeoutException){
            showToast("网络连接超时。请检查网络");
        }
        else{
            showToast("未知错误");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processFailEvent(@NonNull APIFail apiFail) {
        if(apiFail.getCode() == BaseModel.SUB_FAIL_STATUS_TOKEN_EXPIRE){
            showTokenExpireDialog() ;
        }
        else {
            showToast(apiFail.getMessage());
        }
    }

    /**
     *
     * @param baseModel
     */
    protected<T> void dealSuccess(BaseModel baseModel){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }

    public void showTokenExpireDialog(){
        OKCancelDlg.createCancelOKDlg(getContext(), "登录超时", "重新登录", "退出应用", new ICancelOK() {
            @Override
            public void cancel() {
                ActivityExitManage.finishAll();
            }

            @Override
            public void ok() {
                ActivityExitManage.finishAll();
                startActivity(LoginActivity.newIntent(getContext()));
            }
        });
    }
}
