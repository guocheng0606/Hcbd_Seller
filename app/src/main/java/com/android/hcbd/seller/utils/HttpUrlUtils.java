package com.android.hcbd.seller.utils;

/**
 * Created by guocheng on 2017/6/19.
 */

public class HttpUrlUtils {

    //public static String BASEURL = "http://112.124.108.24:8989/order";
    public static String BASEURL = "http://47.98.134.90:18107/order";
    //public static String BASEURL = "http://192.168.8.88:8080/order";

    public static String login_url = BASEURL + "/orderApp/loginAction!login.action";//登录
    public static String edit_password_url = BASEURL + "/orderApp/loginAction!editPwd.action";//修改密码

    public static String get_all_table_url = BASEURL + "/orderApp/tableAction!list.action";//桌子列表
    public static String get_table_operate_url = BASEURL + "/orderApp/tableAction!operate.action";//桌子操作
    public static String get_shop_url = BASEURL + "/orderApp/shopAction!getShop.action";//桌子操作

    public static String get_goods_type_url = BASEURL + "/orderApp/appTypeAction!list.action";//获取商品类型
    public static String get_type_edit_url = BASEURL + "/orderApp/appTypeAction!operate.action";//获取商品类型的操作

    public static String get_goods_url = BASEURL + "/orderApp/menuAction!getList.action";//获取商品
    public static String get_goods_operate_url = BASEURL + "/orderApp/menuAction!operate.action";//上架或者下架商品
    public static String get_goods_edit_url = BASEURL + "/orderApp/menuAction!edit.action";//编辑商品

    public static String get_pkg_list_url = BASEURL + "/orderApp/menuAction!getPkgList.action";//套餐内容列表
    public static String get_pkg_edit_url = BASEURL + "/orderApp/pkgAction!edit.action";//套餐内容列表
    public static String get_pkg_delete_url = BASEURL + "/orderApp/pkgAction!delete.action";//套餐内容删除

    public static String get_pkg_goods_to_edit_url = BASEURL + "/orderApp/menuAction!toEditMenu.action";//套餐编辑
    public static String get_pkg_goods_edit_url = BASEURL + "/orderApp/menuAction!editMenuPkg.action";//套餐编辑

    public static String get_to_pay_url = BASEURL + "/orderApp/tableAction!toPay.action";//套餐编辑

    public static String get_order_list_url = BASEURL + "/orderApp/carAction!list.action";//订单管理列表
    public static String get_order_pay_url = BASEURL + "/orderApp/carAction!payCar.action";//订单管理列表结算
    public static String get_order_info_url = BASEURL + "/orderApp/carAction!view.action";//查看订单详情
    public static String update_order_state_url = BASEURL + "/orderApp/carAction!updState.action";//修改订单状态



}
