package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * 描述右侧ListViewAdapter
 */
public class RightAdapter extends CustomizeLVBaseAdapter {
    //上下文
    private Context mContext;
    //标题
    private List<List<String>> leftList;
    //内容
    private List<List<GoodsInfo>> rightList;
    private LayoutInflater inflater;

    public RightAdapter(Context mContext, List<List<String>> leftList, List<List<GoodsInfo>> rightList) {
        this.mContext = mContext;
        this.leftList = leftList;
        this.rightList = rightList;
        //系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int section, int position) {
        return rightList.get(section).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftList == null ? 0 : leftList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return rightList.get(section).size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.lv_customize_item_right, parent, false);
            //绑定
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_edit1 = (TextView) convertView.findViewById(R.id.tv_edit1);
            holder.tv_edit2 = (TextView) convertView.findViewById(R.id.tv_edit2);
            holder.tv_isoff = (TextView) convertView.findViewById(R.id.tv_isoff);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        //设置内容
        final GoodsInfo goodsInfo = rightList.get(section).get(position);
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        return convertView;
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

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        HeaderViewHolder holder = null;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.lv_customize_item_header, parent, false);
            //绑定
            holder.lv_customize_item_header_text = (TextView) convertView.findViewById(R.id.lv_customize_item_header_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //不可点击
        convertView.setClickable(false);
        //设置标题
        holder.lv_customize_item_header_text.setText(leftList.get(section).get(1));
        return convertView;
    }

    class ChildViewHolder {
        private ImageView iv_image;
        private TextView tv_name;
        private TextView tv_price;
        private TextView tv_edit1;
        private TextView tv_edit2;
        private TextView tv_isoff;
    }

    class HeaderViewHolder {
        //标题
        private TextView lv_customize_item_header_text;
    }
}

