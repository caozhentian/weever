package cn.people.weever.net;

import java.util.List;

import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.DailyRentaOrder;
import cn.people.weever.model.FixTimeOrder;
import cn.people.weever.model.HalfDayRentalOrder;
import cn.people.weever.model.TransferOrder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**订单相关的api
 */

public interface OrderApiService {

    //订单
    public static final int TO_ORDER_CHARGING_NET_REQUST            =             400          ;
    public static final int TO_ORDER_WAITTING_NET_REQUST            =             401          ;
    public static final int TO_ORDER_RESTART_NET_REQUST             =             402          ;
    public static final int TO_ORDER_TO_SETTLEMENT_NET_REQUST      =          403           ;
    public static final int TO_ORDER_SUBMIT_NET_REQUST              =             404           ;

    public static final int TO_ORDER_LIST_NET_REQUST                          =             410           ;
    public static final int TO_ORDER_LIST_ALL_NET_REQUST                          =             410           ;
    public static final int TO_ORDER_LIST_PENDING_APPOINTMENT_NET_REQUST        =             412           ;
    public static final int TO_ORDER_LIST_PENDING_EXECUTION_NET_REQUST          =             413           ;
    public static final int TO_ORDER_LIST_CANCELED_NET_REQUST                     =             414          ;
    public static final int TO_ORDER_LIST_PENDING_SETTLEMENT_NET_REQUST          =            415           ;
    public static final int TO_ORDER_LIST_MAX_VALUE_NET_REQUST                          =             419           ;


    public static final int TO_ORDER_TYPE_DAY_DETAILS_NET_REQUST             =   420                      ;
    public static final int TO_ORDER_TYPE_DAYHALF_DETAILS_NET_REQUST        =   421                      ;
    public static final int TO_ORDER_TYPE_TRANSFER_DETAILS_NET_REQUST        =  422                      ;
    public static final int TO_ORDER_TYPE_FixTime_DETAILS_NET_REQUST        =   423                      ;

    /**
     *查询订单
     * @param status
     * @return
     */
    @FormUrlEncoded
    @POST("order/list")
    Call<BaseModel<List<BaseOrder>>> list( @Field("status")int status ,@Field("page")int page , @Field("pageSize")int pageSize ) ;


    /**
     * 提交订单
     * @return
     */
    @POST("order/submit")
    Call<BaseModel<Object>>  submit(@Body RequestBody orderSubmitInfo) ;

    /**
     * 订单
     * @return
     */
    @FormUrlEncoded
    @POST("order/operateOrder")
    Call<BaseModel<Object>>  operateOrder(@Body RequestBody orderSubmitInfo) ;

    /**
     * 取消订单
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/cancel")
    Call<BaseModel<Object>> cancel( @Field("order_id") String id ) ;

    @FormUrlEncoded
    @POST("order/getDailyRentaOrderInfo")
    Call<BaseModel<DailyRentaOrder>> getDailyRentaOrderDetails( @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getHalfDayRentalOrderInfo")
    Call<BaseModel<HalfDayRentalOrder>> getHalfDayRentalOrderDetails( @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getTransferOrderInfo")
    Call<BaseModel<TransferOrder>> getTransferOrderDetails( @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getFixTimeOrderInfo")
    Call<BaseModel<FixTimeOrder>>  getFixTimeOrderDetails( @Field("order_id") String id) ;

}
