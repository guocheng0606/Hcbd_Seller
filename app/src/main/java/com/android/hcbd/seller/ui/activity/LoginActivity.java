package com.android.hcbd.seller.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.LoginInfo;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.SharedPreferencesUtil;
import com.android.hcbd.seller.utils.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String user = SharedPreferencesUtil.get(this, "username_info");
        etUsername.setText(user);
        etUsername.setSelection(user.length());
        etPassword.setText("1");
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if(TextUtils.isEmpty(etUsername.getText().toString())){
            ToastUtils.showShortToast(this,"请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString())){
            ToastUtils.showShortToast(this,"请输入密码");
            return;
        }
        OkGo.<String>post(HttpUrlUtils.login_url)
                .tag(this)
                .params("userName",etUsername.getText().toString())
                .params("userPwd",etPassword.getText().toString())
                .params("orgCode","027")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(LoginActivity.this);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        dealHttpData(result);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShortToast(MyApplication.getInstance(),"请检查是否连接网络！");
                        ProgressDialogUtils.dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    private void dealHttpData(final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                        LoginInfo loginInfo = new LoginInfo();
                        loginInfo.setToken(jsonObject.getString("token"));

                        Gson gson = new Gson();
                        LoginInfo.UserInfo userInfo = gson.fromJson(jsonObject.getString("data"),LoginInfo.UserInfo.class);
                        loginInfo.setUserInfo(userInfo);

                        JSONArray array = new JSONArray(jsonObject.getString("menuList"));
                        List<LoginInfo.menuInfo> menuInfoList = new ArrayList<LoginInfo.menuInfo>();
                        for(int i=0;i<array.length();i++){
                            LoginInfo.menuInfo menuInfo = gson.fromJson(array.getString(i),LoginInfo.menuInfo.class);
                            menuInfoList.add(menuInfo);
                        }
                        loginInfo.setMenuList(menuInfoList);

                        MyApplication.getInstance().setLoginInfo(loginInfo);
                        SharedPreferencesUtil.save(LoginActivity.this, "username_info", etUsername.getText().toString());

                        Message message = new Message();
                        message.what = 0x10;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        if(!TextUtils.isEmpty(jsonObject.getString("error"))){
                            Message message = new Message();
                            message.what = 0x11;
                            handler.sendMessage(message);
                            Looper.prepare();
                            ToastUtils.showShortToast(MyApplication.getInstance(),String.valueOf(Html.fromHtml(jsonObject.getString("error"))));
                            Looper.loop();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x10:
                    ToastUtils.showShortToast(MyApplication.getInstance(),"登录成功");
                    ProgressDialogUtils.dismissLoading();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
                case 0x11:
                    ProgressDialogUtils.dismissLoading();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

}
