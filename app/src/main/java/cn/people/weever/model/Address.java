package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**地点描述
 * Created by Administrator on 2017/6/2.
 */

public class Address implements Serializable{

    //地点名
    @SerializedName("placeName")
    private String  mPlaceName      ;
    //纬度
    @SerializedName("latitude")
    private double mLatitude       ;
    //经度
    @SerializedName("longitude")
    private double mLongitude      ;

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
