package com.android.hcbd.seller.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.OrderMenuAdapter;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.OrderInfo;
import com.android.hcbd.seller.entity.OrderItemInfo;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.views.NoScrollListView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.listView)
    NoScrollListView listView;
    @BindView(R.id.tv_amts)
    TextView tvAmts;
    private OrderInfo orderInfo;
    private List<OrderItemInfo> list = new ArrayList<>();
    private OrderMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);

        orderInfo = getIntent().getParcelableExtra("info");
        tvCode.setText("桌号：" + orderInfo.getTabCode());
        tvTime.setText("下单时间：" + orderInfo.getCreateTime().replace("T", " "));
        tvAmts.setText("实付：￥"+ CommonUtils.subZeroAndDot(String.valueOf(orderInfo.getAmt())));
        getHttpData();
        ivBack.setOnClickListener(this);
    }

    private void getHttpData() {
        OkGo.<String>post(HttpUrlUtils.get_order_info_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", orderInfo.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            Gson gson = new Gson();
                            JSONArray array = new JSONArray(jsonObject.getString("list"));
                            for (int i = 0; i < array.length(); i++) {
                                OrderItemInfo info = gson.fromJson(array.getString(i), OrderItemInfo.class);
                                list.add(info);
                            }
                            adapter = new OrderMenuAdapter(GoodsInfoActivity.this, list);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
        }
    }
}
