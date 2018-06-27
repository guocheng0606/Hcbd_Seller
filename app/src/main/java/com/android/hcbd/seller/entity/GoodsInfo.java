package com.android.hcbd.seller.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/3/29.
 */

public class GoodsInfo implements Parcelable {

    /**
     * amt : 5.0
     * code : 1C00000001
     * id : 33
     * name : 青菜
     * remark :
     * state : 2
     * typeId : 10
     * url : /upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png
     */

    private double amt;
    private String code;
    private int id;
    private String name;
    private String remark;
    private String state;
    private int typeId;
    private String url;

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.amt);
        dest.writeString(this.code);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.remark);
        dest.writeString(this.state);
        dest.writeInt(this.typeId);
        dest.writeString(this.url);
    }

    public GoodsInfo() {
    }

    protected GoodsInfo(Parcel in) {
        this.amt = in.readDouble();
        this.code = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.remark = in.readString();
        this.state = in.readString();
        this.typeId = in.readInt();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<GoodsInfo> CREATOR = new Parcelable.Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel source) {
            return new GoodsInfo(source);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };
}
