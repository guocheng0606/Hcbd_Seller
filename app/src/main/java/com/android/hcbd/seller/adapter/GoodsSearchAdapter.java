package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.GoodsInfo;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.ui.activity.GoodsEditActivity;
import com.android.hcbd.seller.ui.activity.PkgGoodsEditActivity;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guocheng on 2018/4/12.
 */

public class GoodsSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsInfo> list;
    public GoodsSearchAdapter(Context mContext,List<GoodsInfo> list){
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list == null ? null : list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_customize_item_right, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final GoodsInfo goodsInfo = list.get(i);
        Glide.with(mContext).load(HttpUrlUtils.BASEURL+goodsInfo.getUrl()).into(holder.iv_image);
        holder.tv_name.setText(goodsInfo.getName());
        holder.tv_price.setText("￥"+ CommonUtils.subZeroAndDot(String.valueOf(goodsInfo.getAmt())));

        if(goodsInfo.getState().equals("1")){
            holder.tv_edit1.setText("下架");
            holder.tv_isoff.setVisibility(View.GONE);
        }else{
            holder.tv_edit1.setText("上架");
            holder.tv_isoff.setVisibility(View.VISIBLE);
        }

        holder.tv_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(goodsInfo);
            }
        });
        holder.tv_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goodsInfo.getTypeId() != 9){
                    Intent intent = new Intent(mContext, GoodsEditActivity.class);
                    intent.putExtra("info",goodsInfo);
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, PkgGoodsEditActivity.class);
                    intent.putExtra("info",goodsInfo);
                    mContext.startActivity(intent);
                }
            }
        });
        return view;
    }

    class ViewHolder{
        @BindView(R.id.iv_image)
        ImageView iv_image;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_edit1)
        TextView tv_edit1;
        @BindView(R.id.tv_edit2)
        TextView tv_edit2;
        @BindView(R.id.tv_isoff)
        TextView tv_isoff;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    private void showEditDialog(final GoodsInfo goodsInfo) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(goodsInfo.getState().equals("1") ? "确认下架该商品吗？":"确认上架该商品吗？")
                .setNegativeButton(mContext.getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(mContext.getResources().getText(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OkGo.<String>post(HttpUrlUtils.get_goods_operate_url)
                                .tag(this)
                                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                                .params("id", goodsInfo.getId())
                                .params("state", goodsInfo.getState().equals("1") ? "2" : "1")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onStart(Request<String, ? extends Request> request) {
                                        super.onStart(request);
                                        ProgressDialogUtils.showLoading(mContext);
                                    }

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        String result = response.body();
                                        LogUtils.LogShow(result);
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(result);
                                            if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                                ToastUtils.showLongToast(MyApplication.getInstance(),jsonObject.getString("data"));

                                                EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_GOODS_UPDATE));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            try {
                                                if(!TextUtils.isEmpty(jsonObject.getString("error"))){
                                                    ProgressDialogUtils.dismissLoading();
                                                    ToastUtils.showLongToast(MyApplication.getInstance(),jsonObject.getString("error"));
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
                                        //ProgressDialogUtils.dismissLoading();
                                    }
                                });
                    }
                }).create().show();
    }

}
