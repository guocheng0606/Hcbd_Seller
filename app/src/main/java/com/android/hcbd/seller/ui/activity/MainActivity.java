package com.android.hcbd.seller.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.hcbd.seller.R;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.ui.fragment.GoodsFragment;
import com.android.hcbd.seller.ui.fragment.OrderFragment;
import com.android.hcbd.seller.ui.fragment.SettingFragment;
import com.android.hcbd.seller.ui.fragment.ShopFragment;
import com.android.hcbd.seller.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab)
    PageNavigationView tab;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ShopFragment shopFragment;
    private GoodsFragment goodsFragment;
    private OrderFragment orderFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        initBottomNavigation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()){
            case MessageEvent.EVENT_LOGINOUT:
                startActivity(new Intent(this,LoginActivity.class));
                finishActivity();
                break;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initBottomNavigation() {
        NavigationController navigationController = tab.custom()
                .addItem(newItem(R.drawable.ic_tab_1_nol,R.drawable.ic_tab_1_sel,"店铺管理"))
                .addItem(newItem(R.drawable.ic_tab_2_nol,R.drawable.ic_tab_2_sel,"商品管理"))
                .addItem(newItem(R.drawable.ic_tab_3_nol,R.drawable.ic_tab_3_sel,"订单管理"))
                .addItem(newItem(R.drawable.ic_tab_4_nol,R.drawable.ic_tab_4_sel,"设置"))
                .build();
        //navigationController.setSelect(0);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                //选中时触发
                setTabSelection(index);
            }

            @Override
            public void onRepeat(int index) {
                //System.out.println(index);
                //setTabSelection(index);
            }
        });
    }

    private void setTabSelection(int i) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (i){
            case 0:
                if(shopFragment == null){
                    shopFragment = new ShopFragment();
                    fragmentTransaction.add(R.id.fl_layout,shopFragment);
                }else{
                    fragmentTransaction.show(shopFragment);
                }
                break;
            case 1:
                if(goodsFragment == null){
                    goodsFragment = new GoodsFragment();
                    fragmentTransaction.add(R.id.fl_layout,goodsFragment);
                }else{
                    fragmentTransaction.show(goodsFragment);
                }
                break;
            case 2:
                if(orderFragment == null){
                    orderFragment = new OrderFragment();
                    fragmentTransaction.add(R.id.fl_layout,orderFragment);
                }else{
                    fragmentTransaction.show(orderFragment);
                }
                break;
            case 3:
                if(settingFragment == null){
                    settingFragment = new SettingFragment();
                    fragmentTransaction.add(R.id.fl_layout,settingFragment);
                }else{
                    fragmentTransaction.show(settingFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(shopFragment != null) fragmentTransaction.hide(shopFragment);
        if(goodsFragment != null) fragmentTransaction.hide(goodsFragment);
        if(orderFragment != null) fragmentTransaction.hide(orderFragment);
        if(settingFragment != null) fragmentTransaction.hide(settingFragment);
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable, checkedDrawable, text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xFF00a0e9);
        return normalItemView;
    }



    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            ToastUtils.showShortToast(MainActivity.this,"再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }else{//退出程序
            this.finish();
            System.exit(0);
        }
    }

}
