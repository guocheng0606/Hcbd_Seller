package com.android.hcbd.seller.event;

/**
 * Created by guocheng on 2018/3/28.
 */
public class MessageEvent {

    /*发出的广播类型*/
    public static final int EVENT_GOODS_TYPE_UPDATE = 100;
    public static final int EVENT_GOODS_UPDATE = 101;
    public static final int EVENT_GOODS_UPDATE_SUCCESS = 102;

    public static final int EVENT_TABLE_UPDATE = 105;
    public static final int EVENT_PKG_UPDATE = 106;

    public static final int EVENT_ORDER_PAY_UPDATE = 107;

    public static final int EVENT_LOGINOUT = 120;

    private int eventId;
    private Object obj;
    private Object obj2;
    public MessageEvent() {
    }

    public MessageEvent(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
    }
}
