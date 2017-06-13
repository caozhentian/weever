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
import cn.people.weever.model.TransferOrder;
import cn.people.weever.model.TripNode;
import cn.people.weever.net.OrderApiService;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MockOrderService extends MockService {

    List<BaseOrder> orders =  new LinkedList<>() ;

    @Override
    public MockResponse getJsonData() {
        MockResponse mockResponse = getSuccessResponse() ;
        mockResponse.getModel().setData(getOrders());
        MockBaseCallback<List<BaseOrder>> mockBaseCallback = new  MockBaseCallback<List<BaseOrder>>(OrderApiService.TO_ORDER_LIST_NET_REQUST , mockResponse) ;
        mockBaseCallback.onResponse();
        return  mockResponse ;
    }

    private List<BaseOrder> getOrders(){

        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY, BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_DAY_HALF,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_PICK_UP,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_CONVEYOR,BaseOrder.ORDER_STAUS_ORDER));
        orders.add(generatePreOrder(BaseOrder.ORDER_TYPE_AIRPORT_FIXED_TIME,BaseOrder.ORDER_STAUS_ORDER));
        return   orders ;
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

        baseOrder.setOrderId(UUID.randomUUID().toString());
        baseOrder.setSubscribePerson("LiMing");

        TripNode tripNode = new TripNode() ;
        tripNode.setTime(System.currentTimeMillis()/1000 - 70*60*60);
        Address address = new Address() ;
        address.setPlaceName("HereS")   ;
        address.setLatitude(34.221227);
        address.setLongitude(108.898191);
        tripNode.setAddress(address);
        baseOrder.setPlanboardingTripNode(tripNode);

        tripNode = new TripNode() ;
        tripNode.setTime(System.currentTimeMillis()/1000 - 65*60*60);
        address = new Address() ;
        address.setPlaceName("HereD")   ;
        address.setLatitude(34.17589);
        address.setLongitude(108.879616);
        tripNode.setAddress(address);
        baseOrder.setPlanboardingTripNode(tripNode);

        baseOrder.setPlanTotalTime("30分钟");

        if(status == BaseOrder.ORDER_STAUS_PAY) {
            TripNode tripNodeM = new TripNode();
            tripNodeM.setTime(System.currentTimeMillis() / 1000 - 66 * 60 * 60);
            Address addressM = new Address();
            addressM.setPlaceName("Here2S");
            addressM.setLatitude(34.172786);
            addressM.setLongitude(108.881262);
            tripNodeM.setAddress(addressM);
            baseOrder.setActualBoardingTripNode(tripNodeM);
            tripNodeM = new TripNode() ;
            tripNodeM.setTime(System.currentTimeMillis()/1000 - 66*60*60);
            addressM = new Address()  ;
            addressM.setPlaceName("Here2D")   ;
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

        for(BaseOrder baseOrderS : orders){
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



}
