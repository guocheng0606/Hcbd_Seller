package com.android.hcbd.seller.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.MyViewPagerAdapter;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.NumberFormatUtil;
import com.lany.state.StateLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 店铺管理
 */
public class ShopFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;

    private String mParam1;
    private String mParam2;

    private int floor;
    private int room;
    private int table;


    public ShopFragment() {
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);

        stateLayout.showLoading();
        httpData();
        return view;
    }

    private void httpData() {
        OkGo.<String>post(HttpUrlUtils.get_shop_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", MyApplication.getInstance().getLoginInfo().getUserInfo().getAccountIds())
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
                            if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                JSONObject object = new JSONObject(jsonObject.getString("data"));
                                floor = object.getInt("floor");
                                room = object.getInt("room");
                                table = object.getInt("table");
                                setupTabLayout();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void setupTabLayout() {
        stateLayout.showContent();
        //ViewPager关联适配器
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());
        for(int i=0;i<floor;i++){
            adapter.addFragment(ShopItemFragment.newInstance((i+1)+"F", null), NumberFormatUtil.formatInteger(i + 1)+"楼");
        }
        if(room > 0)
            adapter.addFragment(ShopItemFragment.newInstance("BJ",null),"包间");
        viewPager.setAdapter(adapter);
        //ViewPager和TabLayout关联
        tabLayout.setupWithViewPager(viewPager);
        if (floor > 0)
            viewPager.setOffscreenPageLimit(floor);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
