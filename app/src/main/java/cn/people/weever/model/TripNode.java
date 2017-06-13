package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import cn.people.weever.common.util.DatetimeUtil;

/**行程节点： 时间和 地点信息
 * Created by  on 2017/6/2.
 */

public class TripNode {

    //时间 单位秒
    @SerializedName("time")
    private long   mTime        ;

    @SerializedName("address")
    private Address mAddress    ;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    public String  getDateStr(){
        return DatetimeUtil.getDate(mTime * 1000 , DatetimeUtil.DEFAULT_FORMAT1) ;
    }
}
