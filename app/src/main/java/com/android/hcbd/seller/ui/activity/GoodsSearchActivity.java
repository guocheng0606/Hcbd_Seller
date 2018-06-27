package com.android.hcbd.seller.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.GoodsSearchAdapter;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lany.state.StateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsSearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private GoodsSearchAdapter adapter;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        adapter = new GoodsSearchAdapter(this,goodsInfoList);
        listView.setAdapter(adapter);
        initListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GOODS_UPDATE_SUCCESS:
                getSearchData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString())){
                    stateLayout.showContent();
                    goodsInfoList.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }
                getSearchData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.iv_search:
                if(TextUtils.isEmpty(etName.getText().toString())){
                    stateLayout.showContent();
                    goodsInfoList.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }

                KeyboardUtils.hideSoftInput(this);
                stateLayout.showLoading();
                getSearchData();
                break;
        }
    }

    private void getSearchData() {
        goodsInfoList.clear();
        for(int i = 0; i< MyApplication.getInstance().getGoodList().size(); i++){
            if(MyApplication.getInstance().getGoodList().get(i).getName().indexOf(etName.getText().toString()) != -1){
                goodsInfoList.add(MyApplication.getInstance().getGoodList().get(i));
            }
        }
        if(goodsInfoList.size() > 0){
            adapter.notifyDataSetChanged();
            stateLayout.showContent();
        }else{
            stateLayout.showEmpty();
        }

    }
}
