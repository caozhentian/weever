package cn.people.weever.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/11 0011.
 *  版本更新
 */

public class VersionUpdating implements Serializable{

    private double andro_num;
    private String andro_apk;

    public double getAndro_num() {
        return andro_num;
    }

    public void setAndro_num(double andro_num) {
        this.andro_num = andro_num;
    }

    public String getAndro_apk() {
        return andro_apk;
    }

    public void setAndro_apk(String andro_apk) {
        this.andro_apk = andro_apk;
    }
}
