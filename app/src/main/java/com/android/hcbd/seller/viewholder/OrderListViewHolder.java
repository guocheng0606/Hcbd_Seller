package com.android.hcbd.seller.viewholder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.OrderInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.ui.activity.GoodsInfoActivity;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guocheng on 2018/4/18.
 */

public class OrderListViewHolder extends BaseViewHolder<OrderInfo> {

    private TextView tv_code;
    private TextView tv_state;
    private TextView tv_time;
    private TextView tv_amt;
    private Button btn;
    private SwipeMenuLayout swipeMenuLayout;
    private LinearLayout ll_content;

    public OrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_order_list_layout);
        tv_code = $(R.id.tv_code);
        tv_state = $(R.id.tv_state);
        tv_time = $(R.id.tv_time);
        tv_amt = $(R.id.tv_amt);
        btn = $(R.id.btn);
        swipeMenuLayout = $(R.id.swipeMenuLayout);
        ll_content = $(R.id.ll_content);
    }

    @Override
    public void setData(final OrderInfo data) {
        super.setData(data);
        tv_code.setText(""+data.getTabCode());
        tv_state.setText(""+data.getStateContent());
        tv_time.setText("下单时间："+data.getCreateTime().replace("T"," "));
        tv_amt.setText("订单金额："+ CommonUtils.subZeroAndDot(String.valueOf(data.getAmt())));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getState().equals("2")){
                    new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage("确认结算吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    payOrder(data);
                                    swipeMenuLayout.smoothClose();
                                }
                            })
                            .create().show();
                }else{
                    ToastUtils.showShortToast(MyApplication.getInstance(),"该订单不能结算");
                    swipeMenuLayout.smoothClose();
                }

            }
        });
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.LogShow("id = "+data.getId());
                Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                intent.putExtra("info",data);
                getContext().startActivity(intent);
            }
        });
    }

    private void payOrder(OrderInfo data) {
        OkGo.<String>post(HttpUrlUtils.get_order_pay_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", data.getId())
                .execute(new StringCallback() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(getContext());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                ToastUtils.showLongToast(MyApplication.getInstance(),jsonObject.getString("data"));

                                EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_ORDER_PAY_UPDATE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                if(!TextUtils.isEmpty(jsonObject.getString("error"))){
                                    ToastUtils.showLongToast(MyApplication.getInstance(),jsonObject.getString("error"));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }

}
