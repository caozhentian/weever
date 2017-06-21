package cn.people.weever.respositoty;

/**
 * Created by Administrator on 2017/6/21.
 */

public class ApiOperationCodeRespoistory {

    protected  static int mApiOperationCode ;

    public static void store(int apiOperationCode){
        mApiOperationCode = apiOperationCode ;
    }

    public static int  query(){
        return mApiOperationCode ;
    }
}
