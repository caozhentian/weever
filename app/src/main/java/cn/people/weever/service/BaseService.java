package cn.people.weever.service;

import cn.people.weever.net.IApiOperationCode;

/**
 * Created by Administrator on 2017/6/23.
 */

public class BaseService {

    protected IApiOperationCode mIApiOperationCode ;

    public BaseService(IApiOperationCode IApiOperationCode) {
        mIApiOperationCode = IApiOperationCode;
    }
}
