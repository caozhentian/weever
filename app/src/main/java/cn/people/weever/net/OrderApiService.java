package cn.people.weever.net;

import java.util.List;

import cn.people.weever.model.BaseOrder;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**订单相关的api
 */

public interface OrderApiService {


    //订单
    public static final int TO_ORDER_ADD                 =   400      ;
    public static final int TO_ORDER_DELETE              =   401      ;
    public static final int TO_ORDER_LIST                =   402      ;
    public static final int TO_ORDER_CANCEL              =   403      ;
    public static final int TO_ORDER_DETAILS             =   404      ;
    /**
     *查询订单
     * @param sid
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/list")
    Call<BaseModel<List<BaseOrder>>> list(@Field("sid") String sid , @Field("type")int type) ;

    
    
    /**
     * 提交订单
     * @param sid
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/add")
    Call<BaseModel<Object>>  submit(@Field("sid") String sid ,
                                @Field("goods_id") String goodsId ,
                                @Field("size_id")  String size_id,
                                @Field("number")   String number,
                                @Field("inv_type") String inv_type,
                                @Field("inv_title") String inv_title
                                ) ;

    /**
     * 取消订单
     * @param sid
     * @param id
     * @param cancelReason
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/cancel")
    Call<BaseModel<Object>> cancel(@Field("sid") String sid , @Field("order_id") String id ,@Field("cancel_reason") String cancelReason) ;


    /**
     * 确认订单
     * @param sid
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/confirm")
    Call<BaseModel<Object>> confirmReceipt(@Field("sid") String sid , @Field("order_id") String id) ;

    /**
     * 获取订单详情
     * @param sid
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/app/?url=/order/info")
    Call<BaseModel<BaseOrder>> getDetails(@Field("sid") String sid , @Field("order_id") String id) ;


}
