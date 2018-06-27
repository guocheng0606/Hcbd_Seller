package com.android.hcbd.seller.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by guocheng on 2018/4/2.
 */

public class PkgInfo implements Parcelable {


    /**
     * code : P000000000001
     * content : ["雪碧","矿泉水","可乐","茶","咖啡"]
     * createTime : 2018-03-06T10:29:08
     * id : 1
     * name : 饮料
     * names : P000000000001-饮料
     * num : 2
     * operNames : null
     * orgCode : 027
     * paramsObj : null
     * remark : 雪碧,矿泉水,可乐,茶,咖啡
     * state : 1
     * stateContent : 启用
     */

    private String code;
    private String createTime;
    private int id;
    private String name;
    private String names;
    private int num;
    private Object operNames;
    private String orgCode;
    private Object paramsObj;
    private String remark;
    private String state;
    private String stateContent;
    private List<String> content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Object getOperNames() {
        return operNames;
    }

    public void setOperNames(Object operNames) {
        this.operNames = operNames;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public void setParamsObj(Object paramsObj) {
        this.paramsObj = paramsObj;
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

    public String getStateContent() {
        return stateContent;
    }

    public void setStateContent(String stateContent) {
        this.stateContent = stateContent;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.createTime);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.names);
        dest.writeInt(this.num);
        dest.writeParcelable((Parcelable) this.operNames, flags);
        dest.writeString(this.orgCode);
        dest.writeParcelable((Parcelable) this.paramsObj, flags);
        dest.writeString(this.remark);
        dest.writeString(this.state);
        dest.writeString(this.stateContent);
        dest.writeStringList(this.content);
    }

    public PkgInfo() {
    }

    protected PkgInfo(Parcel in) {
        this.code = in.readString();
        this.createTime = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.names = in.readString();
        this.num = in.readInt();
        this.operNames = in.readParcelable(Object.class.getClassLoader());
        this.orgCode = in.readString();
        this.paramsObj = in.readParcelable(Object.class.getClassLoader());
        this.remark = in.readString();
        this.state = in.readString();
        this.stateContent = in.readString();
        this.content = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PkgInfo> CREATOR = new Parcelable.Creator<PkgInfo>() {
        @Override
        public PkgInfo createFromParcel(Parcel source) {
            return new PkgInfo(source);
        }

        @Override
        public PkgInfo[] newArray(int size) {
            return new PkgInfo[size];
        }
    };
}
