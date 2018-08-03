package com.android.hcbd.seller.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.entity.OrderInfo;
import com.android.hcbd.seller.entity.OrderItemInfo;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/31.
 */

public class GetDataService extends Service {

    private MyBinder binder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.LogShow("Service启动");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public GetDataService getService() {
            return GetDataService.this;
        }
    }

    public boolean flag = false;

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        if (flag) {
                           httpdata();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void httpdata() {
        flag = false;
        OkGo.<String>post(HttpUrlUtils.get_order_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("page.currentPage", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final String result = response.body();

                        //使用RxAndroid异步处理耗时操作
                        Observable.create(new Observable.OnSubscribe<List<OrderInfo>>() {
                            @Override
                            public void call(Subscriber<? super List<OrderInfo>> subscriber) { //创建Observable，用来发送数据
                                JSONObject jsonObject = null;
                                List<OrderInfo> orderList = new ArrayList<>();
                                try {
                                    jsonObject = new JSONObject(result);
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                    Gson gson = new Gson();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        OrderInfo info = gson.fromJson(jsonArray.getString(i), OrderInfo.class);
                                        orderList.add(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                subscriber.onNext(orderList);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())  //使用ui线程来接收Observable发送的数据
                                .subscribe(new Observer<List<OrderInfo>>() {  //创建了Subscriber(订阅者)，用来接收事件处理
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<OrderInfo> list) {
                                        List<Integer> strs = new ArrayList<>();
                                        for (OrderInfo info : list) {
                                            if (!info.getState().equals("2")) {
                                                strs.add(info.getId());
                                            }
                                        }
                                        getOrderdetail(strs,0);
                                    }
                                });
                    }
                });
    }



    private void getOrderdetail(final List<Integer> strs, final int index) {
        if (index == strs.size()) {
            return;
        }
        OkGo.<String>post(HttpUrlUtils.get_order_info_url)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", strs.get(index))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final String result = response.body();

                        //使用RxAndroid异步处理耗时操作
                        Observable.create(new Observable.OnSubscribe<List<OrderItemInfo>>() {
                            @Override
                            public void call(Subscriber<? super List<OrderItemInfo>> subscriber) { //创建Observable，用来发送数据
                                JSONObject jsonObject = null;
                                List<OrderItemInfo> list = new ArrayList<>();
                                try {
                                    jsonObject = new JSONObject(result);
                                    Gson gson = new Gson();
                                    JSONArray array = new JSONArray(jsonObject.getString("list"));
                                    for (int i = 0; i < array.length(); i++) {
                                        OrderItemInfo info = gson.fromJson(array.getString(i), OrderItemInfo.class);
                                        list.add(info);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                subscriber.onNext(list);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())  //使用ui线程来接收Observable发送的数据
                                .subscribe(new Observer<List<OrderItemInfo>>() {  //创建了Subscriber(订阅者)，用来接收事件处理
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<OrderItemInfo> list) {
                                        updateOrderSate(strs,index);
                                        getOrderdetail(strs,index+1);
                                    }
                                });


                    }
                });
    }


    private void updateOrderSate(final List<Integer> strs, final int index){
        OkGo.<String>post(HttpUrlUtils.update_order_state_url)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", strs.get(index))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        if (index == (strs.size()-1)) {
                            flag = true;
                        }
                    }
                });
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
