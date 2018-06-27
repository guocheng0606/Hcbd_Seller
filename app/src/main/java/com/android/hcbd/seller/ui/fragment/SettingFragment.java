package com.android.hcbd.seller.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.seller.R;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.ui.activity.UpdatePasswordActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 设置
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_item1)
    TextView tvItem1;
    @BindView(R.id.tv_item2)
    TextView tvItem2;
    @BindView(R.id.tv_item3)
    TextView tvItem3;
    @BindView(R.id.tv_item4)
    TextView tvItem4;
    @BindView(R.id.tv_item5)
    TextView tvItem5;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    public SettingFragment() {
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvItem1.setOnClickListener(this);
        tvItem2.setOnClickListener(this);
        tvItem3.setOnClickListener(this);
        tvItem4.setOnClickListener(this);
        tvItem5.setOnClickListener(this);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_item1:
                startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
                break;
            case R.id.tv_item2:

                break;
            case R.id.tv_item3:

                break;
            case R.id.tv_item4:

                break;
            case R.id.tv_item5:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("您确认退出登录吗？");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventId(MessageEvent.EVENT_LOGINOUT);
                EventBus.getDefault().post(messageEvent);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

}
