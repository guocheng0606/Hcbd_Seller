package com.android.hcbd.seller.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.PkgInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackageEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.et_num)
    EditText etNum;
    private PkgInfo pkgInfo;
    private List<String> mVals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);
        ButterKnife.bind(this);

        pkgInfo = getIntent().getParcelableExtra("info");
        setFlowData();
        if (pkgInfo == null) {
            tvTitle.setText("套餐内容录入");
        } else {
            tvTitle.setText("套餐内容编辑");
            etName.setText(""+pkgInfo.getName());
            etNum.setText(""+pkgInfo.getNum());
            mVals.clear();
            mVals.addAll(pkgInfo.getContent());
            flowlayout.getAdapter().notifyDataChanged();
        }

        initListener();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, final int position, FlowLayout parent) {
                new AlertDialog.Builder(PackageEditActivity.this)
                        .setTitle("删除提示")
                        .setMessage("是否删除该内容？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mVals.remove(position);
                                flowlayout.getAdapter().notifyDataChanged();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.iv_add:
                if (TextUtils.isEmpty(etContent.getText().toString()))
                    return;
                mVals.add(etContent.getText().toString());
                etContent.setText("");
                flowlayout.getAdapter().notifyDataChanged();
                break;
            case R.id.btn_complete:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastUtils.showLongToast(PackageEditActivity.this, "请输入套餐名称");
                    return;
                }
                if (mVals.size() == 0) {
                    ToastUtils.showLongToast(PackageEditActivity.this, "请设置内容");
                    return;
                }
                if(!TextUtils.isEmpty(etNum.getText().toString())){
                    if(Integer.parseInt(etNum.getText().toString()) > flowlayout.getAdapter().getCount()){
                        ToastUtils.showLongToast(PackageEditActivity.this, "可选数量不能超过已添加的内容个数");
                        return;
                    }
                }
                httpComplate();
                break;
        }
    }

    private void setFlowData() {
        flowlayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(PackageEditActivity.this).inflate(R.layout.layout_flow_tv,
                        flowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    private void httpComplate() {
        String str = "";
        for (String s : mVals) {
            str += "," + s;
        }
        str = str.substring(1, str.length());

        HttpParams params = new HttpParams();

        params.put("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode());
        params.put("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode());
        params.put("token", MyApplication.getInstance().getLoginInfo().getToken());

        if (pkgInfo != null) {
            params.put("oid", pkgInfo.getId());
            params.put("id", pkgInfo.getId());
            params.put("state", pkgInfo.getState());
            params.put("code", pkgInfo.getCode());
        }

        params.put("name", etName.getText().toString());
        params.put("num", String.valueOf(TextUtils.isEmpty(etNum.getText().toString())? 1: etNum.getText().toString()));
        params.put("remark", str);
        params.put("shop.id", MyApplication.getInstance().getLoginInfo().getUserInfo().getAccountIds());
        params.put("orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode());

        OkGo.<String>post(HttpUrlUtils.get_pkg_edit_url)
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                ToastUtils.showLongToast(MyApplication.getInstance(), jsonObject.getString("data"));

                                finishActivity();
                                MessageEvent messageEvent = new MessageEvent(MessageEvent.EVENT_PKG_UPDATE);
                                EventBus.getDefault().post(messageEvent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                if (!TextUtils.isEmpty(jsonObject.getString("error"))) {
                                    ToastUtils.showLongToast(MyApplication.getInstance(), jsonObject.getString("error"));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
