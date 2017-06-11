package cn.people.weever.service;

import android.text.TextUtils;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.model.QueryModel;
import cn.people.weever.respositoty.OrderRespository;

/**订单的业务逻辑
 * 
 */

public class OrderService {

    private OrderRespository mOrderRespository ;

    public OrderService() {
        mOrderRespository = new OrderRespository() ;
    }

    public void list(int status , QueryModel queryModel){
        mOrderRespository.list(status,queryModel);
    }
    
    // 取消订单
//    public void cancel(String orderId ,String reason ){
//        if(TextUtils.isEmpty(orderId) || TextUtils.isEmpty(reason)){
//            return ;
//        }
//        mOrderRespository.cancel(orderId , reason);
//    }

 
    //
    public void submit(OrderSubmitInfo orderSubmitInfo){
        if(orderSubmitInfo == null){
            return ;
        }
        mOrderRespository.submit(orderSubmitInfo);
    }
    // 订单详情
//    public void getDetails(String id){
//        if(TextUtils.isEmpty(id)){
//            return ;
//        }
//        mOrderRespository.getDetails( id ) ;
//    }

    public void getDetails(BaseOrder baseOrder){
        mOrderRespository.getDetails( baseOrder ) ;
    }
}
