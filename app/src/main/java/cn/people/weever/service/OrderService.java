package cn.people.weever.service;

import cn.people.weever.MockData.MockOrderService;
import cn.people.weever.MockData.MockService;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.model.QueryModel;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.respositoty.OrderRespository;

/**订单的业务逻辑
 * 
 */

public class OrderService {

    private OrderRespository mOrderRespository ;
    MockOrderService mockOrderService = new MockOrderService() ;
    public OrderService() {
        mOrderRespository = new OrderRespository() ;
    }

    public void list(int status , QueryModel queryModel){
        if(MockService.DEBUG_MOCK){
            mockOrderService.getJsonData(status);
            return ;
        }
        mOrderRespository.list(status,queryModel);
    }

    //
    public void submit(OrderSubmitInfo orderSubmitInfo){
        if(orderSubmitInfo == null){
            return ;
        }
        if(MockService.DEBUG_MOCK){
            mockOrderService.submitOrder(orderSubmitInfo);
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
        if(MockService.DEBUG_MOCK){
            mockOrderService.getDetails(baseOrder);
            return ;
        }
        mOrderRespository.getDetails( baseOrder ) ;
    }

    public void cancelOrder(BaseOrder baseOrder){
        if(MockService.DEBUG_MOCK){
            mockOrderService.cancelOrder(baseOrder);
            return ;
        }
        mOrderRespository.cancelOrder(baseOrder);
    }

    public void takeOrder(BaseOrder baseOrder){
        if(MockService.DEBUG_MOCK){
            mockOrderService.takeOrder(baseOrder);
            return ;
        }
        mOrderRespository.takeOrder(baseOrder);
    }

    public void routeOperateOrder(RouteOperateEvent routeOperateEvent){
        if(MockService.DEBUG_MOCK){
            mockOrderService.routeOperateOrder(routeOperateEvent);
            return ;
        }
        mOrderRespository.routeOperateOrder(routeOperateEvent);
    }
}
