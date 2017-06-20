package cn.people.weever.service;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.ArrayList;
import java.util.List;

import cn.people.weever.model.Address;
import cn.people.weever.respositoty.AddressRespoisitory;

/**
 * Created by Administrator on 2017/6/20.
 */

public class AddressService {
    private AddressRespoisitory mAddressRespoisitory ;

    public AddressService() {
        mAddressRespoisitory = new AddressRespoisitory();
    }

    public void save(PoiInfo poiInfo , boolean src){
        Address address = new Address() ;
        address.setIsSrc(src);
        address.setLatitude(poiInfo.location.latitude);
        address.setLongitude(poiInfo.location.longitude);
        address.setPlaceName(poiInfo.name);
        address.setAddress(poiInfo.address) ;
        save(address) ;
    }

    public void save(Address address){
        mAddressRespoisitory.save(address);
    }

    public List<PoiInfo> queryAll(boolean isSrc){
        List<Address> addressList =   mAddressRespoisitory.queryAll(isSrc) ;
        List<PoiInfo> poiInfoList = new ArrayList<>(addressList.size()) ;
        for(Address address : addressList){
            PoiInfo poiInfo = new PoiInfo() ;
            poiInfo.address = address.getAddress() ;
            poiInfo.name = address.getPlaceName()   ;
            poiInfo.location = new LatLng(address.getLatitude() , address.getLongitude()) ;
            poiInfoList.add(poiInfo) ;
        }
        return poiInfoList ;
    }
}
