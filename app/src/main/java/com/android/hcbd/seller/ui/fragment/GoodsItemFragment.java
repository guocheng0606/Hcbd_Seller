package com.android.hcbd.seller.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.adapter.LeftAdapter;
import com.android.hcbd.seller.adapter.RightAdapter;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.views.HaveHeaderListView;
import com.lany.state.StateLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 */
public class GoodsItemFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    Unbinder unbinder;
    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.lv_right)
    HaveHeaderListView lvRight;

    private List<List<String>> leftTypeList;
    //左边ListView的Adapter
    private LeftAdapter leftAdapter;
    //左边数据的标志
    private List<Boolean> flagArray;
    private List<List<GoodsInfo>> rightGoodsList;
    private RightAdapter rightAdapter;
    //是否滑动标志位
    private Boolean isScroll = false;


    private String mParam1;
    private String mParam2;

    public GoodsItemFragment() {
    }

    public static GoodsItemFragment newInstance(String param1, String param2) {
        GoodsItemFragment fragment = new GoodsItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_goods_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        stateLayout.showLoading();
        leftTypeList = MyApplication.getInstance().getLeftTypeList();
        flagArray = new ArrayList<>();
        rightGoodsList = new ArrayList<>();

        getGoodsData();
        initListener();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GOODS_UPDATE_SUCCESS:
                getGoodsData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isScroll = false;
                for (int i = 0; i < leftTypeList.size(); i++) {
                    if (i == position) {
                        flagArray.set(i, true);
                    } else {
                        flagArray.set(i, false);
                    }
                }
                //更新
                leftAdapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    //查找
                    rightSection += rightAdapter.getCountForSection(i) + 1;
                }
                //显示到rightSection所代表的标题
                lvRight.setSelection(rightSection);
            }
        });
        lvRight.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lvRight.getLastVisiblePosition() == (lvRight.getCount() - 1)) {
                            lvLeft.setSelection(ListView.FOCUS_DOWN);
                        }
                        // 判断滚动到顶部
                        if (lvRight.getFirstVisiblePosition() == 0) {
                            lvLeft.setSelection(0);
                        }
                        break;
                }

            }

            int y = 0;
            int x = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightGoodsList.size(); i++) {
                        if (i == rightAdapter.getSectionForPosition(lvRight.getFirstVisiblePosition())) {
                            flagArray.set(i, true);
                            //获取当前标题的标志位
                            x = i;
                        } else {
                            flagArray.set(i, false);
                        }
                    }
                    if (x != y) {
                        leftAdapter.notifyDataSetChanged();
                        //将之前的标志位赋值给y，下次判断
                        y = x;
                    }
                } else {
                    isScroll = true;
                }
            }
        });
    }


    private void getGoodsData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GoodsInfo> goodList = MyApplication.getInstance().getGoodList();
                rightGoodsList.clear();
                for(int i=0;i<MyApplication.getInstance().getLeftTypeList().size();i++) {
                    List<GoodsInfo> list = new ArrayList<>();
                    for (GoodsInfo goodsInfo : goodList) {
                        if (Integer.parseInt(MyApplication.getInstance().getLeftTypeList().get(i).get(0)) == goodsInfo.getTypeId()) {
                            switch (mParam1) {
                                case "1":
                                    if (goodsInfo.getState().equals("1"))
                                        list.add(goodsInfo);
                                    break;
                                case "2":
                                    if (goodsInfo.getState().equals("2"))
                                        list.add(goodsInfo);
                                    break;
                                default:
                                    list.add(goodsInfo);
                                    break;
                            }
                        }
                    }
                    rightGoodsList.add(list);
                }


                /*if(!mParam1.equals("0")){
                    List<List<GoodsInfo>> list = new ArrayList<>();
                    list.addAll(rightGoodsList);
                    for(int i=0;i<list.size();i++){
                        LogUtils.LogShow("size = " + list.get(i).size());
                        if(list.get(i).size() == 0){
                            LogUtils.LogShow("zzz");
                            *//*rightGoodsList.remove(i);
                            leftTypeList.remove(i);*//*
                        }
                    }

                    LogUtils.LogShow("sizeee = " + leftTypeList.size());
                    LogUtils.LogShow("sizerr = " + rightGoodsList.size());

                }*/

                flagArray.clear();
                for (int i = 0; i < leftTypeList.size(); i++) {
                    flagArray.add(false);
                }
                flagArray.set(0, true);


                Message message = new Message();
                message.what = 0x20;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x20:
                    leftAdapter = new LeftAdapter(getActivity(), leftTypeList, flagArray);
                    lvLeft.setAdapter(leftAdapter);

                    rightAdapter = new RightAdapter(getActivity(), leftTypeList, rightGoodsList);
                    lvRight.setAdapter(rightAdapter);
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
