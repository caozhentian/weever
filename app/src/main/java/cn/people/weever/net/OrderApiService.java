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
    public static final int TO_ORDER_SETTLEMENT_NET_REQUST         =             403           ;
    public static final int TO_ORDER_SUBMIT_NET_REQUST              =             404           ;

    public static final int TO_ORDER_LIST_ALL_NET_REQUST                 =             410           ;
    public static final int TO_ORDER_LIST_PENDING_APPOINTMENT_NET_REQUST        =             412           ;
    public static final int TO_ORDER_LIST_PENDING_EXECUTION_NET_REQUST          =             413           ;
    public static final int TO_ORDER_LIST_CANCELED_NET_REQUST              =             414          ;
    public static final int TO_ORDER_LIST_PENDING_SETTLEMENT_NET_REQUST          =            415           ;


    public static final int TO_ORDER_DETAILS_NET_REQUST             =   408                     ;
    /**
     *查询订单
     * @param token
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("order/list")
    Call<BaseModel<List<BaseOrder>>> list(@Field("token") String token , @Field("type")int type) ;


    /**
     * 提交订单
     * @return
     */
    @FormUrlEncoded
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
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/cancel")
    Call<BaseModel<Object>> cancel(@Field("token") String token , @Field("order_id") String id ) ;

    @FormUrlEncoded
    @POST("order/getDailyRentaOrderInfo")
    Call<BaseModel<DailyRentaOrder>> getDailyRentaOrderDetails(@Field("sid") String sid , @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getHalfDayRentalOrderInfo")
    Call<BaseModel<HalfDayRentalOrder>> getHalfDayRentalOrderDetails(@Field("sid") String sid , @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getTransferOrderInfo")
    Call<BaseModel<TransferOrder>> getTransferOrderDetails(@Field("sid") String sid , @Field("order_id") String id) ;
    @FormUrlEncoded
    @POST("order/getFixTimeOrderInfo")
    Call<BaseModel<FixTimeOrder>>  getFixTimeOrderDetails(@Field("sid") String sid , @Field("order_id") String id) ;

}
