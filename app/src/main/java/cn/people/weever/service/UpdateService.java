package cn.people.weever.service;

import cn.people.weever.net.IApiOperationCode;
import cn.people.weever.respositoty.UpdateApkRespository;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UpdateService extends BaseService{

    protected UpdateApkRespository mUpdateApkRespository ;

    public UpdateService(IApiOperationCode iApiOperationCode) {
        super(iApiOperationCode);
        mUpdateApkRespository = new UpdateApkRespository() ;
    }

    public void getUpdateApkInfo() {
        //mUpdateApkRespository.getUpdateApkInfo();
    }
}
