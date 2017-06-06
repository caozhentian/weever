package cn.people.weever.MockData;

import java.util.LinkedList;
import java.util.List;

import cn.people.weever.model.Address;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.TripNode;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockOrderService extends MockService {


    @Override
    public MockResponse getJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        mockResponse.getModel().setData(getOrders());
        MockBaseCallback<List<BaseOrder>> mockBaseCallback = new  MockBaseCallback<List<BaseOrder>>(1 , mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }

    private List<BaseOrder> getOrders(){
        List<BaseOrder> orders =  new LinkedList<>() ;

        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY, BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY_HALF,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_PICK_UP,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME,BaseOrder.ORDER_STAUS_ORDER));
        return   orders ;
    }


    private BaseOrder generatePreOrder(int type , int status){
        BaseOrder baseOrder    =  new BaseOrder()    ;
        baseOrder.setOrderId("10001");
        baseOrder.setSubscribePerson("LiMing");
        TripNode tripNode = new TripNode() ;
        tripNode.setTime(System.currentTimeMillis()/1000 - 70*60*60);
        Address address = new Address() ;
        address.setPlaceName("Here")   ;
        address.setLatitude(34.221227);
        address.setLongitude(108.898191);
        tripNode.setAddress(address);
        baseOrder.setPlanboardingTripNode(tripNode);
        TripNode tripNodeM = new TripNode() ;
        tripNodeM.setTime(System.currentTimeMillis()/1000 - 66*60*60);
        Address addressM = new Address()  ;
        addressM.setPlaceName("Here2")   ;
        addressM.setLatitude(34.172786)   ;
        addressM.setLongitude(108.881262) ;
        tripNodeM.setAddress(addressM);
        baseOrder.setPlanDropOffTripNode(tripNodeM) ;
        baseOrder.setType(type);
        baseOrder.setStatus(status);
        return baseOrder ;
    }
}
