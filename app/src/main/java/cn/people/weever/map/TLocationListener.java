package cn.people.weever.map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public abstract  class TLocationListener implements BDLocationListener {

    private boolean isFirstLoc = false ;
    @Override
    public void onReceiveLocation(BDLocation location) {
        {
            // map view 销毁后不在处理新接收的位置
            if (location == null || location.getLocType() == BDLocation.TypeServerError ) {
                processLocFail(location) ;
                return;
            }
            process(location) ;
            if (isFirstLoc) {
                isFirstLoc = false;
                //使用街道
                List<Poi> poiList = location.getPoiList() ;
                //使用街道
                if(poiList != null && poiList.size() > 1) {
                    Poi poi = poiList.get(0);
                    processFirstLoc(location) ;
                }
            }
        }
    }

    public abstract  void process(BDLocation location) ;

    public abstract  void processFirstLoc(BDLocation location) ;

    public abstract  void processLocFail(BDLocation location) ;
}
