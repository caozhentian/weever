package cn.people.weever.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**地点描述
 * Created by Administrator on 2017/6/2.
 */

@Entity
public class Address implements Serializable{

    private static final long serialVersionUID = -8940196742313994740L ;

    @Property
    @Id
    private String id;

    @Property
    private boolean isSrc ;
    //地点名
    @Property
    @SerializedName("placeName")
    private String  mPlaceName      ;

    @Property
    private String  mAddress         ;
    //纬度
    @Property
    @SerializedName("latitude")
    private double mLatitude       ;
    //经度
    @Property
    @SerializedName("longitude")
    private double mLongitude      ;

//    public Address(String id, boolean isSrc, String mPlaceName, String mAddress,
//            double mLatitude, double mLongitude) {
//        this.id = id;
//        this.isSrc = isSrc;
//        this.mPlaceName = mPlaceName;
//        this.mAddress = mAddress;
//        this.mLatitude = mLatitude;
//        this.mLongitude = mLongitude;
//    }

    public Address() {
    }

    @Generated(hash = 1333443384)
    public Address(String id, boolean isSrc, String mPlaceName, String mAddress,
            double mLatitude, double mLongitude) {
        this.id = id;
        this.isSrc = isSrc;
        this.mPlaceName = mPlaceName;
        this.mAddress = mAddress;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSrc() {
        return isSrc;
    }

    public void setSrc(boolean src) {
        isSrc = src;
    }

    public boolean getIsSrc() {
        return this.isSrc;
    }

    public void setIsSrc(boolean isSrc) {
        this.isSrc = isSrc;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getMPlaceName() {
        return this.mPlaceName;
    }

    public void setMPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public double getMLatitude() {
        return this.mLatitude;
    }

    public void setMLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getMLongitude() {
        return this.mLongitude;
    }

    public void setMLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getMAddress() {
        return this.mAddress;
    }

    public void setMAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
