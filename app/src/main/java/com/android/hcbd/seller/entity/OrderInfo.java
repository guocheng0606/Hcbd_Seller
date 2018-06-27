package com.android.hcbd.seller.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guocheng on 2018/4/18.
 */

public class OrderInfo implements Parcelable {

    /**
     * amt : 18.0
     * amts : null
     * carItemIdHql :
     * code : null
     * createTime : 2018-04-18T14:41:16
     * id : 46
     * itemsIds : null
     * menuIds : null
     * name : null
     * names : null-null
     * nums : null
     * operId : a1ee0fc5-a255-4e41-aef8-18b8cfe285ce
     * operNames : null
     * orgCode : null
     * paramsObj : null
     * pkgs : null
     * remark :
     * seq : 1
     * shopId : 1
     * state : 1
     * stateContent : 已下单
     * states : null
     * tabCode : 1F03
     */

    private double amt;
    private Object amts;
    private String carItemIdHql;
    private Object code;
    private String createTime;
    private int id;
    private Object itemsIds;
    private Object menuIds;
    private Object name;
    private String names;
    private Object nums;
    private String operId;
    private Object operNames;
    private Object orgCode;
    private Object paramsObj;
    private Object pkgs;
    private String remark;
    private int seq;
    private int shopId;
    private String state;
    private String stateContent;
    private Object states;
    private String tabCode;

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public Object getAmts() {
        return amts;
    }

    public void setAmts(Object amts) {
        this.amts = amts;
    }

    public String getCarItemIdHql() {
        return carItemIdHql;
    }

    public void setCarItemIdHql(String carItemIdHql) {
        this.carItemIdHql = carItemIdHql;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getItemsIds() {
        return itemsIds;
    }

    public void setItemsIds(Object itemsIds) {
        this.itemsIds = itemsIds;
    }

    public Object getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Object menuIds) {
        this.menuIds = menuIds;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public Object getNums() {
        return nums;
    }

    public void setNums(Object nums) {
        this.nums = nums;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public Object getOperNames() {
        return operNames;
    }

    public void setOperNames(Object operNames) {
        this.operNames = operNames;
    }

    public Object getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Object orgCode) {
        this.orgCode = orgCode;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public void setParamsObj(Object paramsObj) {
        this.paramsObj = paramsObj;
    }

    public Object getPkgs() {
        return pkgs;
    }

    public void setPkgs(Object pkgs) {
        this.pkgs = pkgs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateContent() {
        return stateContent;
    }

    public void setStateContent(String stateContent) {
        this.stateContent = stateContent;
    }

    public Object getStates() {
        return states;
    }

    public void setStates(Object states) {
        this.states = states;
    }

    public String getTabCode() {
        return tabCode;
    }

    public void setTabCode(String tabCode) {
        this.tabCode = tabCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.amt);
        dest.writeParcelable((Parcelable) this.amts, flags);
        dest.writeString(this.carItemIdHql);
        dest.writeParcelable((Parcelable) this.code, flags);
        dest.writeString(this.createTime);
        dest.writeInt(this.id);
        dest.writeParcelable((Parcelable) this.itemsIds, flags);
        dest.writeParcelable((Parcelable) this.menuIds, flags);
        dest.writeParcelable((Parcelable) this.name, flags);
        dest.writeString(this.names);
        dest.writeParcelable((Parcelable) this.nums, flags);
        dest.writeString(this.operId);
        dest.writeParcelable((Parcelable) this.operNames, flags);
        dest.writeParcelable((Parcelable) this.orgCode, flags);
        dest.writeParcelable((Parcelable) this.paramsObj, flags);
        dest.writeParcelable((Parcelable) this.pkgs, flags);
        dest.writeString(this.remark);
        dest.writeInt(this.seq);
        dest.writeInt(this.shopId);
        dest.writeString(this.state);
        dest.writeString(this.stateContent);
        dest.writeParcelable((Parcelable) this.states, flags);
        dest.writeString(this.tabCode);
    }

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        this.amt = in.readDouble();
        this.amts = in.readParcelable(Object.class.getClassLoader());
        this.carItemIdHql = in.readString();
        this.code = in.readParcelable(Object.class.getClassLoader());
        this.createTime = in.readString();
        this.id = in.readInt();
        this.itemsIds = in.readParcelable(Object.class.getClassLoader());
        this.menuIds = in.readParcelable(Object.class.getClassLoader());
        this.name = in.readParcelable(Object.class.getClassLoader());
        this.names = in.readString();
        this.nums = in.readParcelable(Object.class.getClassLoader());
        this.operId = in.readString();
        this.operNames = in.readParcelable(Object.class.getClassLoader());
        this.orgCode = in.readParcelable(Object.class.getClassLoader());
        this.paramsObj = in.readParcelable(Object.class.getClassLoader());
        this.pkgs = in.readParcelable(Object.class.getClassLoader());
        this.remark = in.readString();
        this.seq = in.readInt();
        this.shopId = in.readInt();
        this.state = in.readString();
        this.stateContent = in.readString();
        this.states = in.readParcelable(Object.class.getClassLoader());
        this.tabCode = in.readString();
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
