package com.android.hcbd.seller.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.OrderInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.android.hcbd.seller.viewholder.OrderListViewHolder;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
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
 * 订单管理
 */
public class OrderFragment extends Fragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int currentPage = 1;
    private RecyclerArrayAdapter<OrderInfo> adapter;
    private List<OrderInfo> orderList = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        getHttpData();
        initListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_ORDER_PAY_UPDATE:
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                break;
        }
    }

    private void initListener() {

    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(0xFFDDDDDD, 2, 0, 0);
        itemDecoration.setDrawLastItem(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<OrderInfo>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new OrderListViewHolder(parent);
            }
        });
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(0xFF1191C7);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getHttpData() {
        OkGo.<String>post(HttpUrlUtils.get_order_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("page.currentPage", currentPage)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            if (jsonArray.length() > 0) {
                                orderList.clear();
                                Gson gson = new Gson();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    OrderInfo info = gson.fromJson(jsonArray.getString(i), OrderInfo.class);
                                    orderList.add(info);
                                }
                                if (currentPage == 1)
                                    adapter.clear();
                                adapter.addAll(orderList);
                            } else {
                                if (currentPage == 1) {
                                    adapter.clear();
                                } else {
                                    adapter.stopMore();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            adapter.pauseMore();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (currentPage == 1) {
                            ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                        } else {
                            adapter.pauseMore();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getHttpData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        System.out.println("加载更多。。。" + currentPage);
        getHttpData();
    }

}
