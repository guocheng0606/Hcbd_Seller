package com.android.hcbd.seller.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.TypelistAdapter;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.lany.state.StateLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TypeListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<List<String>> typeList;
    private TypelistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        stateLayout.showLoading();
        typeList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TypelistAdapter(TypeListActivity.this, typeList);
        recyclerView.setAdapter(adapter);
        getgoodsType();
        initListener();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getgoodsType();
            }
        });
        adapter.setOnItemClickListener(new TypelistAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showEditDialog(adapter.getAllData().get(position));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GOODS_TYPE_UPDATE:
                getgoodsType();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

    private void getgoodsType() {
        OkGo.<String>post(HttpUrlUtils.get_goods_type_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final String result = response.body();
                        LogUtils.LogShow(result);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(result);
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                        typeList.clear();
                                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONArray array = new JSONArray(jsonArray.getString(i));
                                            List<String> list = new ArrayList<>();
                                            for (int j = 0; j < array.length(); j++) {
                                                list.add(array.getString(j));
                                            }
                                            typeList.add(list);
                                        }

                                        Message message = new Message();
                                        message.what = 0x01;
                                        mHandler.sendMessage(message);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    adapter.notifyDataSetChanged();
                    stateLayout.showContent();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.iv_add:
                showEditDialog(null);
                break;
        }
    }

    private AlertDialog dialog = null;
    private void showEditDialog(final List<String> list) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_type_edit,null);
        final MaterialEditText met = (MaterialEditText) contentView.findViewById(R.id.met_name);
        if(list != null){
            met.setText(list.get(1));
            met.setSelection(met.getText().toString().length());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(list == null ? "添加":"编辑");
        builder.setView(contentView);
        builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getText(R.string.confirm),null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(met.getText().toString())){
                    met.setError("请输入分类");
                    return;
                }
                if(met.getText().toString().length() > 8){
                    met.setError("最多输入8个汉字");
                    return;
                }
                httpEditData(list,met.getText().toString());
                dialog.dismiss();
            }
        });


    }

    private void httpEditData(List<String> list,String str) {
        OkGo.<String>post(HttpUrlUtils.get_type_edit_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", list == null ? "" : list.get(0))
                .params("name", str)
                .params("operate", "")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(TypeListActivity.this);
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

                                MessageEvent messageEvent = new MessageEvent();
                                messageEvent.setEventId(MessageEvent.EVENT_GOODS_TYPE_UPDATE);
                                EventBus.getDefault().post(messageEvent);
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
