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
import com.android.hcbd.seller.adapter.PkgAdapter;
import com.android.hcbd.seller.base.BaseActivity;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.entity.PkgEditInfo;
import com.android.hcbd.seller.entity.PkgInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.imageloader.GlideImageLoader;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.android.hcbd.seller.views.NoScrollListView;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lany.state.StateLayout;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PkgGoodsEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.rl_sel_pic)
    RelativeLayout rlSelPic;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.listView)
    NoScrollListView listView;

    public static final int IMAGE_PICKER = 0x02;
    private List<PkgInfo> pkgInfoList = new ArrayList<>();
    private PkgAdapter adapter;
    private String[] items;
    private int typeId = -1;
    private String path = "";       //图片的路径
    private GoodsInfo goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkg_goods_edit);
        ButterKnife.bind(this);

        goodsInfo = getIntent().getParcelableExtra("info");
        stateLayout.showLoading();
        getHttpPkgData();
        adapter = new PkgAdapter(PkgGoodsEditActivity.this,pkgInfoList);
        listView.setAdapter(adapter);
        if (goodsInfo == null) {
            tvTitle.setText(getResources().getText(R.string.txt_right4));
        }else {
            tvTitle.setText(getResources().getText(R.string.txt_right5));
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
        initListener();
    }

    private void httpToEdit() {
        OkGo.<String>post(HttpUrlUtils.get_pkg_goods_to_edit_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", goodsInfo.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if(!TextUtils.isEmpty(jsonObject.getString("itemList"))){
                                Gson gson = new Gson();

                                List<PkgEditInfo> list = new ArrayList<>();
                                JSONArray array = new JSONArray(jsonObject.getString("itemList"));
                                for(int i=0;i<array.length();i++){
                                    PkgEditInfo info = gson.fromJson(array.getString(i),PkgEditInfo.class);
                                    list.add(info);
                                }
                                stateLayout.showContent();
                                adapter.setData(list);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        rlSelPic.setOnClickListener(this);
    }


    private void getHttpPkgData() {
        OkGo.<String>post(HttpUrlUtils.get_pkg_list_url)
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
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                        pkgInfoList.clear();
                                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            PkgInfo pkgInfo = gson.fromJson(jsonArray.getString(i), PkgInfo.class);
                                            pkgInfoList.add(pkgInfo);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(goodsInfo == null){
                                                    stateLayout.showContent();
                                                    adapter.notifyDataSetChanged();
                                                }else{
                                                    httpToEdit();
                                                }
                                            }
                                        });


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.ll_category:
                showSelectedDialog(items, tvCategory);
                break;
            case R.id.rl_sel_pic:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.btn_complete:
                httpComplete();
                break;
        }

    }

    private void httpComplete() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtils.showLongToast(this, "请输入套餐名称");
            return;
        }
        if (TextUtils.isEmpty(etPrice.getText().toString())) {
            ToastUtils.showLongToast(this, "请输入套餐价格");
            return;
        }
        if (TextUtils.isEmpty(tvCategory.getText().toString())) {
            ToastUtils.showLongToast(this, "请选择套餐分类");
            return;
        }
        /*if(TextUtils.isEmpty(etRemark.getText().toString())){
            ToastUtils.showLongToast(this, "请输入套餐详情");
            return;
        }*/
        if(adapter.getAllData().size() == 1 && adapter.getAllData().get(0) == null){
            ToastUtils.showLongToast(this, "请选择套餐内容");
            return;
        }
        if (goodsInfo == null && TextUtils.isEmpty(path)) {
            ToastUtils.showLongToast(this, "请添加图片");
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

            List<String> pkgIds = new ArrayList<>();
            List<String> nums = new ArrayList<>();
            for(int i=0;i<adapter.getAllData().size();i++){
                if(adapter.getAllData().get(i) != null){
                    pkgIds.add(""+adapter.getAllData().get(i).getPkg().getId());
                    nums.add(""+adapter.getAllData().get(i).getNum());
                }
            }
            params.put("itemsIds","");
            params.putUrlParams("pkgIds",pkgIds);
            params.putUrlParams("nums", nums);

            for(int i=0;i<nums.size();i++){
                LogUtils.LogShow("pkgIds = "+pkgIds.get(i));
                LogUtils.LogShow("num = "+nums.get(i));
            }

        }else{
            if(TextUtils.isEmpty(path)){
                params.put("upload", "");
            }else{
                params.put("upload", FileUtils.getFileByPath(path));
            }
            params.put("oid", ""+goodsInfo.getId());

            List<String> itemsIds = new ArrayList<>();
            List<String> pkgIds = new ArrayList<>();
            List<String> nums = new ArrayList<>();
            for(int i=0;i<adapter.getAllData().size();i++){
                if(adapter.getAllData().get(i) != null){
                    pkgIds.add(""+adapter.getAllData().get(i).getPkg().getId());
                    nums.add(""+adapter.getAllData().get(i).getNum());
                    if(adapter.getAllData().get(i).getId() == 0)
                        itemsIds.add("");
                    else
                        itemsIds.add(""+adapter.getAllData().get(i).getId());

                }
            }
            params.putUrlParams("itemsIds",itemsIds);
            params.putUrlParams("pkgIds",pkgIds);
            params.putUrlParams("nums", nums);

            for(int i=0;i<nums.size();i++){
                LogUtils.LogShow("itemsIds = "+itemsIds.get(i));
                LogUtils.LogShow("pkgIds = "+pkgIds.get(i));
                LogUtils.LogShow("num = "+nums.get(i));
            }
        }




        OkGo.<String>post(HttpUrlUtils.get_pkg_goods_edit_url)
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(PkgGoodsEditActivity.this);
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
                new GlideImageLoader().displayImage(PkgGoodsEditActivity.this, images.get(0).path, ivPhoto, 0, 0);

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
