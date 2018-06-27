package com.android.hcbd.seller.entity;

/**
 * Created by Administrator on 2018/4/11.
 */

public class PkgEditInfo {

    /**
     * id : 12
     * num : 3
     * pkg : {"code":"P000000000002","content":["桃子","李子","苹果","葡萄","香蕉","菠萝","草莓","芒果","橘子"],"createTime":"2018-03-06T10:29:00","id":2,"name":"水果","names":"P000000000002-水果","num":3,"operNames":null,"orgCode":"027","paramsObj":null,"remark":"桃子,李子,苹果,葡萄,香蕉,菠萝,草莓,芒果,橘子","state":"1","stateContent":"启用"}
     */

    private int id;
    private int num;
    private PkgInfo pkg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public PkgInfo getPkg() {
        return pkg;
    }

    public void setPkg(PkgInfo pkg) {
        this.pkg = pkg;
    }
}
