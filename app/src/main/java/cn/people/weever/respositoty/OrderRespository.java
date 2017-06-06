package cn.people.weever.respositoty;

import java.util.List;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.net.BaseCallback;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.net.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Retrofit;

/**订单
 */

public class OrderRespository {

    private OrderApiService mOrderApiService;

    public  OrderRespository() {
        Retrofit retrofit = RetrofitFactory.getBaseRetrofit();
        mOrderApiService = retrofit.create(OrderApiService.class);
    }

    

//    Call<ResponseBody> photoOrder(
//            @Part("remark") RequestBody remark,
//            @Part("address_id") RequestBody  addressId,
//            @Part("address_id") RequestBody  type,
//            @PartMap() Map<String, RequestBody> maps);
    // 订单列表
    public void list(int type){

        Call<BaseModel<List<BaseOrder>>> call = mOrderApiService.list("" ,type ) ;
        call.enqueue(new BaseCallback<List<BaseOrder>>(OrderApiService.TO_ORDER_LIST));
    }
   
    // 取消订单
    public void cancel(String orderId ,String reason ){
        String sid = "" ;
        Call<BaseModel<Object>> call = mOrderApiService.cancel(sid ,orderId ,reason  ) ;
        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_CANCEL));
    }
    // 确认收货
    public void confirmReceipt(){
        String sid = "" ;
        Call<BaseModel<Object>> call = mOrderApiService.confirmReceipt(sid ,"" ) ;
        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_ADD));
    }
    // 订单详情
    public void getDetails(String id){
        String sid = "" ;
        Call<BaseModel<BaseOrder>> call = mOrderApiService.getDetails(sid ,id) ;
        call.enqueue(new BaseCallback<BaseOrder>(OrderApiService.TO_ORDER_DETAILS));
    }
    

}
