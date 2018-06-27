package com.android.hcbd.seller.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.imageloader.GlideImageLoader;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.rl_sel_pic)
    RelativeLayout rlSelPic;
    @BindView(R.id.btn_complete)
    Button btnComplete;

    public static final int IMAGE_PICKER = 0x01;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String[] items;
    private int typeId = -1;
    private String path = "";       //图片的路径
    private GoodsInfo goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_edit);
        ButterKnife.bind(this);

        goodsInfo = getIntent().getParcelableExtra("info");
        if (goodsInfo == null) {
            tvTitle.setText(getResources().getText(R.string.txt_right2));
        }else {
            tvTitle.setText(getResources().getText(R.string.goods_edit1));
            etName.setText(goodsInfo.getName());
            etPrice.setText(CommonUtils.subZeroAndDot(""+goodsInfo.getAmt()));
            for(int i=0;i<MyApplication.getInstance().getLeftTypeList().size();i++){
                if(Integer.parseInt(MyApplication.getInstance().getLeftTypeList().get(i).get(0)) == goodsInfo.getTypeId()){
                    tvCategory.setText(MyApplication.getInstance().getLeftTypeList().get(i).get(1));
                    break;
                }
            }
            etRemark.setText(goodsInfo.getRemark());
            Glide.with(this).load(HttpUrlUtils.BASEURL+goodsInfo.getUrl()).into(ivPhoto);
            typeId = goodsInfo.getTypeId();
        }

        items = new String[MyApplication.getInstance().getLeftTypeList().size()];
        for (int i = 0; i < MyApplication.getInstance().getLeftTypeList().size(); i++) {
            items[i] = MyApplication.getInstance().getLeftTypeList().get(i).get(1);
        }

        ivBack.setOnClickListener(this);
        rlSelPic.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        llCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.rl_sel_pic:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.ll_category:
                showSelectedDialog(items, tvCategory);
                break;
            case R.id.btn_complete:
                httpComplete();
                break;
        }
    }

    private void httpComplete() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtils.showLongToast(this, "请输入商品名称");
            return;
        }
        if (TextUtils.isEmpty(etPrice.getText().toString())) {
            ToastUtils.showLongToast(this, "请输入商品价格");
            return;
        }
        if (TextUtils.isEmpty(tvCategory.getText().toString())) {
            ToastUtils.showLongToast(this, "请选择商品分类");
            return;
        }
        /*if(TextUtils.isEmpty(etRemark.getText().toString())){
            ToastUtils.showLongToast(this, "请输入商品详情");
            return;
        }*/
        if (goodsInfo == null && TextUtils.isEmpty(path)) {
            ToastUtils.showLongToast(this, "请添加商品图片");
            return;
        }

        HttpParams params = new HttpParams();

        params.put("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode());
        params.put("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode());
        params.put("token", MyApplication.getInstance().getLoginInfo().getToken());
        params.put("name", etName.getText().toString());
        params.put("remark", TextUtils.isEmpty(etRemark.getText().toString()) ? "" : etRemark.getText().toString());
        params.put("type.id", typeId);
        params.put("amt", etPrice.getText().toString());
        if(goodsInfo == null){
            params.put("upload", FileUtils.getFileByPath(path));
            params.put("oid", "");
        }else{
            if(TextUtils.isEmpty(path)){
                params.put("upload", "");
            }else{
                params.put("upload", FileUtils.getFileByPath(path));
            }
            params.put("oid", ""+goodsInfo.getId());
        }

        OkGo.<String>post(HttpUrlUtils.get_goods_edit_url)
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(GoodsEditActivity.this);
                    }

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
                                MessageEvent messageEvent = new MessageEvent(MessageEvent.EVENT_GOODS_UPDATE);
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

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = images.get(0).path;
                LogUtils.LogShow("path = " + images.get(0).path);
                new GlideImageLoader().displayImage(GoodsEditActivity.this, images.get(0).path, ivPhoto, 0, 0);

            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showSelectedDialog(final String[] items, final TextView tv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请选择"); //设置标题
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tv.setText(items[which]);
                typeId = Integer.parseInt(MyApplication.getInstance().getLeftTypeList().get(which).get(0));
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
