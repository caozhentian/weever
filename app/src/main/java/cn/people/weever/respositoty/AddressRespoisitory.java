package cn.people.weever.respositoty;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import cn.people.weever.db.DaoManager;
import cn.people.weever.model.Address;
import cn.people.weever.model.AddressDao;
import cn.people.weever.model.DaoSession;

/**
 * Created by Administrator on 2017/6/20.
 */

public class AddressRespoisitory {

    public static final int MAX_STORE_VALUE = 15 ;

    private DaoSession daoSession  ;

    public AddressRespoisitory() {
        daoSession =  DaoManager.getInstance().getDaoSession() ;
    }

    public void save(Address address){
        Address addressDB = query(address) ;
        if( addressDB != null) {
//            addressDB.setLatitude(address.getLatitude());
//            addressDB.setLongitude(address.getLongitude());
//            daoSession.update(addressDB);
        }
        else{
            address.setId(UUID.randomUUID().toString());
            daoSession.insert(address) ;
            QueryBuilder<Address> queryBuilder = daoSession.queryBuilder(Address.class) ;
            if( queryBuilder.count() > MAX_STORE_VALUE){
                final  List<Address>  addresses = queryBuilder.limit(1).list();
                daoSession.runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for(Address address1 : addresses){
                            daoSession.delete(address1);
                        }
                    }
                });
            }
        }
    }

    public Address query(Address address){
        DaoSession daoSession = DaoManager.getInstance().getDaoSession() ;
        QueryBuilder<Address> queryBuilder = daoSession.queryBuilder(Address.class) ;
        queryBuilder.where(AddressDao.Properties.MPlaceName.eq(address.getPlaceName()),
                AddressDao.Properties.IsSrc.eq(address.getIsSrc()));
        List<Address> addressList = queryBuilder.list();
        if(addressList == null || addressList.size() == 0 ){
            return null;
        }
        else{
            return addressList.get(0) ;
        }
    }

    public List<Address> queryAll(boolean isSrc){
        DaoSession daoSession = DaoManager.getInstance().getDaoSession() ;
        QueryBuilder<Address> queryBuilder = daoSession.queryBuilder(Address.class).where(AddressDao.Properties.IsSrc.eq(isSrc) ) ;
        List<Address> addressList = queryBuilder.list();
        Collections.reverse(addressList);
        return addressList ;
    }

}
