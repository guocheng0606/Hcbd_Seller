package com.android.hcbd.seller.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.MyViewPagerAdapter;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.ui.activity.GoodsEditActivity;
import com.android.hcbd.seller.ui.activity.GoodsSearchActivity;
import com.android.hcbd.seller.ui.activity.PackageListActivity;
import com.android.hcbd.seller.ui.activity.PkgGoodsEditActivity;
import com.android.hcbd.seller.ui.activity.TypeListActivity;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.google.gson.Gson;
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
 * 商品管理
 */
public class GoodsFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;

    private List<List<String>> leftTypeList;
    private List<GoodsInfo> goodList = new ArrayList<>();
    private MyViewPagerAdapter adapter;

    private String mParam1;
    private String mParam2;


    public GoodsFragment() {
    }

    public static GoodsFragment newInstance(String param1, String param2) {
        GoodsFragment fragment = new GoodsFragment();
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
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        leftTypeList = new ArrayList<>();
        getgoodsType();
        ivSearch.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GOODS_TYPE_UPDATE:
            case MessageEvent.EVENT_GOODS_UPDATE:
                getgoodsType();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getgoodsType() {
        OkGo.<String>post(HttpUrlUtils.get_goods_type_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        if(adapter == null)
                            ProgressDialogUtils.showLoading(getActivity());
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
                                    if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                        leftTypeList.clear();
                                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONArray array = new JSONArray(jsonArray.getString(i));
                                            List<String> list = new ArrayList<>();
                                            for (int j = 0; j < array.length(); j++) {
                                                list.add(array.getString(j));
                                            }
                                            leftTypeList.add(list);
                                        }

                                        MyApplication.getInstance().setLeftTypeList(leftTypeList);

                                        Message message = new Message();
                                        message.what = 0x20;
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

    private void getGoodsData(){
        OkGo.<String>post(HttpUrlUtils.get_goods_url)
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

                                    if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                        goodList.clear();
                                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                        for(int i=0;i<jsonArray.length();i++){
                                            GoodsInfo goodsInfo = gson.fromJson(jsonArray.getString(i), GoodsInfo.class);
                                            goodList.add(goodsInfo);
                                        }

                                        MyApplication.getInstance().setGoodList(goodList);

                                        Message message = new Message();
                                        message.what = 0x21;
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x20:
                    getGoodsData();
                    break;
                case 0x21:
                    ProgressDialogUtils.dismissLoading();
                    if(adapter == null){
                        setupTabLayout();
                    }else{
                        MessageEvent messageEvent = new MessageEvent(MessageEvent.EVENT_GOODS_UPDATE_SUCCESS);
                        EventBus.getDefault().post(messageEvent);
                    }
                    break;
            }
        }
    };

    private void setupTabLayout(){
        //ViewPager关联适配器
        adapter = new MyViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(GoodsItemFragment.newInstance("0",null),"全部");
        adapter.addFragment(GoodsItemFragment.newInstance("1",null),"出售中");
        adapter.addFragment(GoodsItemFragment.newInstance("2",null),"已下架");
        viewPager.setAdapter(adapter);
        //ViewPager和TabLayout关联
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), GoodsSearchActivity.class));
                break;
            case R.id.iv_more:
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_right_layout,null);
                handleLogic(contentView);
                popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                // 设置好参数之后再show
                popupWindow.showAsDropDown(view);
                break;
        }
    }

    PopupWindow popupWindow = null;
    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
                Intent intent = new Intent();
                switch (v.getId()){
                    case R.id.tv_menu1:
                        intent.setClass(getActivity(), TypeListActivity.class);
                        break;
                    case R.id.tv_menu2:
                        intent.setClass(getActivity(), GoodsEditActivity.class);
                        break;
                    case R.id.tv_menu3:
                        intent.setClass(getActivity(), PackageListActivity.class);
                        break;
                    case R.id.tv_menu4:
                        intent.setClass(getActivity(), PkgGoodsEditActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        contentView.findViewById(R.id.tv_menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_menu2).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_menu3).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_menu4).setOnClickListener(listener);
    }


}
