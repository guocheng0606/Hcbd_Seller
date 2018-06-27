package com.android.hcbd.seller.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.TableListAdapter;
import com.android.hcbd.seller.entity.TableInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.google.gson.Gson;
import com.lany.state.StateLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

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
import butterknife.Unbinder;

/**
 */
public class ShopItemFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private List<TableInfo> tableList = new ArrayList<>();
    private TableListAdapter adapter;

    private String mParam1;
    private String mParam2;

    public ShopItemFragment() {
    }

    public static ShopItemFragment newInstance(String param1, String param2) {
        ShopItemFragment fragment = new ShopItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        httpData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_TABLE_UPDATE:
                httpData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        stateLayout.showLoading();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        adapter = new TableListAdapter(getActivity(), tableList);
        recyclerView.setAdapter(adapter);
    }

    private void httpData() {
        OkGo.<String>post(HttpUrlUtils.get_all_table_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("tab", mParam1)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

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
                                        tableList.clear();
                                        Gson gson = new Gson();
                                        JSONArray array = new JSONArray(jsonObject.getString("data"));
                                        for (int i = 0; i < array.length(); i++) {
                                            TableInfo tableInfo = gson.fromJson(array.getString(i), TableInfo.class);
                                            tableList.add(tableInfo);
                                        }

                                        Message message = new Message();
                                        message.what = 0x12;
                                        mHandler.sendMessage(message);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x12:
                    adapter.notifyDataSetChanged();
                    stateLayout.showContent();
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
