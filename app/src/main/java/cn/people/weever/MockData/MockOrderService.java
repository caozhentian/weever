package cn.people.weever.MockData;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cn.people.weever.model.Address;
import cn.people.weever.model.BaseOrder;
import cn.people.weever.model.Company;
import cn.people.weever.model.DailyRentaOrder;
import cn.people.weever.model.FixTimeOrder;
import cn.people.weever.model.HalfDayRentalOrder;
import cn.people.weever.model.OrderSubmitInfo;
import cn.people.weever.model.RouteOperateEvent;
import cn.people.weever.model.TransferOrder;
import cn.people.weever.model.TripNode;
import cn.people.weever.net.OrderApiService;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockOrderService extends MockService {

    public static List<BaseOrder> orders =  new LinkedList<>() ;

    public static List<BaseOrder> orders2 =  new LinkedList<>() ;

    public static List<BaseOrder> orders3 =  new LinkedList<>() ;

    public static List<BaseOrder> orders4 =  new LinkedList<>() ;

    public MockResponse getJsonData(int status) {
        MockResponse mockResponse = getSuccessResponse() ;
        mockResponse.getModel().setData(getOrders(status));
        MockBaseCallback<List<BaseOrder>> mockBaseCallback = new  MockBaseCallback<List<BaseOrder>>(OrderApiService.TO_ORDER_LIST_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }

    private List<BaseOrder> getOrders(int status){
        List<BaseOrder> curOrder = orders ;
        if(status == BaseOrder.ORDER_STAUS_APPOINTMENT){

        }
        else if(status == BaseOrder.ORDER_STAUS_ORDER){
            curOrder = orders2 ;
        }
        else if(status == BaseOrder.ORDER_STAUS_PAY){
            curOrder = orders3 ;
        }
        else if(status == BaseOrder.ORDER_STAUS_FINISH){
            curOrder = orders4 ;
        }
        if(curOrder.size()> 0){
            return curOrder ;
        }
        curOrder.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY, status));
        curOrder.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY_HALF,status));
        curOrder.add(generatePreOrder(BaseOrder.ORDER_TYPE_PICK_UP,status));
        curOrder.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR,status));
        curOrder.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME,status));
        return curOrder ;
    }


    private BaseOrder generatePreOrder(int type , int status){
        BaseOrder baseOrder    =  new BaseOrder()    ;
        if(type == BaseOrder.ORDER_TYPE_DAY){
            DailyRentaOrder dailyRentaOrder = new DailyRentaOrder() ;
            dailyRentaOrder.setDailyRentalCost(400) ;
            baseOrder = dailyRentaOrder ;
        }
        else if(type == BaseOrder.ORDER_TYPE_DAY_HALF){
            HalfDayRentalOrder halfDayRentalOrder = new HalfDayRentalOrder() ;
            halfDayRentalOrder.setHalfDayRentalCost(200) ;
            baseOrder = halfDayRentalOrder ;
        }
        else if(type == BaseOrder.ORDER_TYPE_PICK_UP || type == BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR){
            TransferOrder transferOrder = new TransferOrder() ;
            transferOrder.setFlightNumber("Hiai Hang") ;
            transferOrder.setTransferCost(400) ;
        }

        baseOrder.setOrderId(UUID.randomUUID().toString().substring(0 , 15));
        baseOrder.setSubscribePerson("LiMing");

        TripNode tripNode = new TripNode() ;
        tripNode.setTime(System.currentTimeMillis()/1000 - 70*60*60);
        Address address = new Address() ;
        address.setPlaceName("旺座现代城")   ;
        address.setLatitude(34.221227);
        address.setLongitude(108.898191);
        tripNode.setAddress(address);
        baseOrder.setPlanboardingTripNode(tripNode);

        tripNode = new TripNode() ;
        tripNode.setTime(System.currentTimeMillis()/1000 - 65*60*60);
        address = new Address() ;
        address.setPlaceName("企业壹号公园")   ;
        address.setLatitude(34.17589);
        address.setLongitude(108.879616);
        tripNode.setAddress(address);
        baseOrder.setPlanDropOffTripNode(tripNode);

        baseOrder.setPlanTotalTime("30分钟");

        if(status == BaseOrder.ORDER_STAUS_PAY) {
            TripNode tripNodeM = new TripNode();
            tripNodeM.setTime(System.currentTimeMillis() / 1000 - 66 * 60 * 60);
            Address addressM = new Address();
            addressM.setPlaceName("旺座现代城");
            addressM.setLatitude(34.172786);
            addressM.setLongitude(108.881262);
            tripNodeM.setAddress(addressM);
            baseOrder.setActualBoardingTripNode(tripNodeM);
            tripNodeM = new TripNode() ;
            tripNodeM.setTime(System.currentTimeMillis()/1000 - 66*60*60);
            addressM = new Address()  ;
            addressM.setPlaceName("企业壹号公园")   ;
            addressM.setLatitude(34.17589)   ;
            addressM.setLongitude(108.879616) ;
            tripNodeM.setAddress(addressM);
            baseOrder.setActualDropOffTripNode(tripNodeM);
        }


        Company company = new Company() ;
        company.setCompanyNum("A000fdCDS");
        baseOrder.setCompany(company);

        baseOrder.setPreDiscount(150);
        baseOrder.setPercentDiscount("90%");
        baseOrder.setPostDiscount(135);

        baseOrder.setStartingFare("15");
        baseOrder.setActualRideTime("5 hours");
        baseOrder.setActualRideTimeCost(50);
        baseOrder.setActualWaitTime("30 Min");
        baseOrder.setActualWaitTimeCost(30);

        baseOrder.setActualMileage("5公里");
        baseOrder.setActualRideTimeCost(10);
        baseOrder.setType(type);
        baseOrder.setStatus(status);
        return baseOrder ;
    }

    public void getDetails(BaseOrder baseOrder){
        MockResponse mockResponse = getSuccessResponse() ;
        List<BaseOrder> curOrders = new LinkedList<>() ;
        curOrders.addAll(orders) ;
        curOrders.addAll(orders2) ;
        curOrders.addAll(orders3) ;
        curOrders.addAll(orders4) ;
        for(BaseOrder baseOrderS : curOrders){
            if(baseOrder.getOrderId().equals(baseOrderS.getOrderId())){
                int type = baseOrder.getType() ;

                if(type == BaseOrder.ORDER_TYPE_DAY){
                    DailyRentaOrder dailyRentaOrder = (DailyRentaOrder)baseOrderS ;
                    MockBaseCallback<DailyRentaOrder> mockBaseCallback = new  MockBaseCallback<DailyRentaOrder>(OrderApiService.TO_ORDER_TYPE_DAY_DETAILS_NET_REQUST, mockResponse) ;
                    mockBaseCallback.onResponse();
                }
                else if(type == BaseOrder.ORDER_TYPE_DAY_HALF){
                    HalfDayRentalOrder halfDayRentalOrder = (HalfDayRentalOrder)baseOrderS ;
                    MockBaseCallback<HalfDayRentalOrder> mockBaseCallback = new  MockBaseCallback<HalfDayRentalOrder>(OrderApiService.TO_ORDER_TYPE_DAYHALF_DETAILS_NET_REQUST, mockResponse) ;
                    mockBaseCallback.onResponse();
                }
                else if(type == BaseOrder.ORDER_TYPE_PICK_UP || type == BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR){
                    TransferOrder transferOrder = (TransferOrder)baseOrderS ;
                    MockBaseCallback<TransferOrder> mockBaseCallback = new  MockBaseCallback<TransferOrder>(OrderApiService.TO_ORDER_TYPE_TRANSFER_DETAILS_NET_REQUST, mockResponse) ;
                    mockBaseCallback.onResponse();
                }
                else{
                    FixTimeOrder fixTimeOrder = (FixTimeOrder) baseOrderS ;
                    MockBaseCallback<FixTimeOrder> mockBaseCallback = new  MockBaseCallback<FixTimeOrder>(OrderApiService.TO_ORDER_TYPE_FixTime_DETAILS_NET_REQUST, mockResponse) ;
                    mockBaseCallback.onResponse();
                }

            }
        }
    }

    public void cancelOrder(BaseOrder baseOrder){
        List<BaseOrder> curOrders = new LinkedList<>() ;
        curOrders.addAll(orders) ;
        curOrders.addAll(orders2) ;
        curOrders.addAll(orders3) ;
        curOrders.addAll(orders4) ;
        curOrders.remove(baseOrder) ;
        MockResponse mockResponse = getSuccessResponse() ;
        MockBaseCallback<Object> mockBaseCallback = new  MockBaseCallback<Object>(OrderApiService.TO_ORDER_CANCEL_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
    }

    public void submitOrder(OrderSubmitInfo orderSubmitInfo){
        List<BaseOrder> curOrders = new LinkedList<>() ;
        curOrders.addAll(orders) ;
        curOrders.addAll(orders2) ;
        curOrders.addAll(orders3) ;
        curOrders.addAll(orders4) ;
        for(BaseOrder baseOrder : curOrders){
            if(baseOrder.getOrderId().equals(orderSubmitInfo.getOrderId())){
                baseOrder.setStatus(BaseOrder.ORDER_STAUS_PAY);
            }
        }
        MockResponse mockResponse = getSuccessResponse() ;
        MockBaseCallback<Object> mockBaseCallback = new  MockBaseCallback<Object>(OrderApiService.TO_ORDER_SUBMIT_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
    }

    public void takeOrder(BaseOrder baseOrder){
        baseOrder.setStatus(BaseOrder.ORDER_STAUS_ORDER);
        MockResponse mockResponse = getSuccessResponse() ;
        MockBaseCallback<Object> mockBaseCallback = new  MockBaseCallback<Object>(OrderApiService.TO_ORDER_TAKE_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
    }

    public void routeOperateOrder(RouteOperateEvent routeOperateEvent){
        MockResponse mockResponse = getSuccessResponse() ;
        mockResponse.getModel().setData(routeOperateEvent);
        MockBaseCallback<RouteOperateEvent> mockBaseCallback = new  MockBaseCallback<RouteOperateEvent>(OrderApiService.TO_ORDER_ROUTE_OPERATE_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
    }

    @Override
    public MockResponse getJsonData() {
        return null;
    }
}
