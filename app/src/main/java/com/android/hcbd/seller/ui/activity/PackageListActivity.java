package com.android.hcbd.seller.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.PkglistAdapter;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.PkgInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.lany.state.StateLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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

public class PackageListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private List<PkgInfo> pkgInfoList;
    private PkglistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        stateLayout.showLoading();
        pkgInfoList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerDecoration itemDecoration = new DividerDecoration(0xFFEDEDED, 1, 0, 0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new PkglistAdapter(this,pkgInfoList);
        recyclerView.setAdapter(adapter);
        getHttpData();
        initListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_PKG_UPDATE:
                getHttpData();
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

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpData();
            }
        });
        adapter.setOnItemClickListener(new PkglistAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(PackageListActivity.this,PackageEditActivity.class);
                intent.putExtra("info",adapter.getAllData().get(position));
                startActivity(intent);
            }
        });
    }

    private void getHttpData() {
        OkGo.<String>post(HttpUrlUtils.get_pkg_list_url)
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
                                    Gson gson = new Gson();
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                        pkgInfoList.clear();
                                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                        for(int i=0;i<jsonArray.length();i++){
                                            PkgInfo pkgInfo = gson.fromJson(jsonArray.getString(i),PkgInfo.class);
                                            pkgInfoList.add(pkgInfo);
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
                Intent intent = new Intent(this,PackageEditActivity.class);
                startActivity(intent);
                break;
        }
    }


}
