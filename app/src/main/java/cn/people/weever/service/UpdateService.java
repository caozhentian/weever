package cn.people.weever.service;

import cn.people.weever.respositoty.UpdateApkRespository;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UpdateService {

    protected UpdateApkRespository mUpdateApkRespository ;

    public UpdateService() {
        mUpdateApkRespository = new UpdateApkRespository();
    }

    public void getUpdateApkInfo() {
        mUpdateApkRespository.getUpdateApkInfo();
    }
}
