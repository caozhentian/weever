package cn.people.weever.respositoty;

import java.util.List;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.DailyRentaOrder;
import cn.people.weever.model.FixTimeOrder;
import cn.people.weever.model.HalfDayRentalOrder;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.model.QueryModel;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.model.TransferOrder;
import cn.people.weever.net.BaseCallback;
import cn.people.weever.net.BaseModel;
import cn.people.weever.net.OrderApiService;
import cn.people.weever.net.RequestBodyCreator;
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

    // 订单列表
    public void list(int status, QueryModel queryModel ){
        Call<BaseModel<List<BaseOrder>>> call = mOrderApiService.list( status , queryModel.getPage() , queryModel.getPageSize()) ;
        call.enqueue(new BaseCallback<List<BaseOrder>>(OrderApiService.TO_ORDER_LIST_NET_REQUST));
    }

    /**
     * 出发 计费 等待 再出发
     * @param routeOperateEvent
     */
    public void routeOperateOrder(RouteOperateEvent routeOperateEvent){
        Call<BaseModel<RouteOperateEvent>> call = mOrderApiService.routeOperateOrder(RequestBodyCreator.CreateRequestBodyJSON(routeOperateEvent)) ;
        call.enqueue(new BaseCallback<RouteOperateEvent>(OrderApiService.TO_ORDER_ROUTE_OPERATE_NET_REQUST)) ;
    }

    public void submit(OrderSubmitInfo orderSubmitInfo){
        Call<BaseModel<Object>> call = mOrderApiService.submit(RequestBodyCreator.CreateRequestBodyJSON(orderSubmitInfo)) ;
        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_SUBMIT_NET_REQUST)) ;
    }
    // 取消订单
//    public void cancel(String orderId ,String reason ){
//        String sid = "" ;
//        Call<BaseModel<Object>> call = mOrderApiService.cancel(sid ,orderId ,reason  ) ;
//        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_CANCEL));
//    }

    // 订单详情
    public void getDetails(BaseOrder baseOrder){

        if(baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY){
            Call<BaseModel<DailyRentaOrder>> call = mOrderApiService.getDailyRentaOrderDetails(baseOrder.getOrderId())  ;
            call.enqueue(new BaseCallback<DailyRentaOrder>(OrderApiService.TO_ORDER_TYPE_DAY_DETAILS_NET_REQUST))   ;
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_DAY_HALF){
            Call<BaseModel<HalfDayRentalOrder>> call = mOrderApiService.getHalfDayRentalOrderDetails(baseOrder.getOrderId())   ;
            call.enqueue(new BaseCallback<HalfDayRentalOrder>(OrderApiService.TO_ORDER_TYPE_DAYHALF_DETAILS_NET_REQUST))  ;
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_PICK_UP){
            Call<BaseModel<TransferOrder>> call = mOrderApiService.getTransferOrderDetails(baseOrder.getOrderId())        ;
            call.enqueue(new BaseCallback<TransferOrder>(OrderApiService.TO_ORDER_TYPE_TRANSFER_DETAILS_NET_REQUST))  ;
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_STAUS_APPOINTMENT){
            Call<BaseModel<TransferOrder>> call = mOrderApiService.getTransferOrderDetails(baseOrder.getOrderId())         ;
            call.enqueue(new BaseCallback<TransferOrder>(OrderApiService.TO_ORDER_TYPE_TRANSFER_DETAILS_NET_REQUST))  ;
        }
        else if(baseOrder.getType() == BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME){
            Call<BaseModel<FixTimeOrder>> call = mOrderApiService.getFixTimeOrderDetails(baseOrder.getOrderId())        ;
            call.enqueue(new BaseCallback<FixTimeOrder>(OrderApiService.TO_ORDER_TYPE_FixTime_DETAILS_NET_REQUST))  ;
        }

    }

    public void takeOrder(BaseOrder baseOrder){
        Call<BaseModel<Object>> call = mOrderApiService.takeOrder(baseOrder.getOrderId())        ;
        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_TAKE_NET_REQUST))  ;
    }

    public void cancelOrder(BaseOrder baseOrder){
        Call<BaseModel<Object>> call = mOrderApiService.takeOrder(baseOrder.getOrderId())        ;
        call.enqueue(new BaseCallback<Object>(OrderApiService.TO_ORDER_CANCEL_NET_REQUST))     ;
    }
}
