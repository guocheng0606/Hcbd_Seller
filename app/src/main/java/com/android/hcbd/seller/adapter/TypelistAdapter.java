package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.event.MessageEvent;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.android.hcbd.seller.utils.LogUtils;
import com.android.hcbd.seller.utils.ProgressDialogUtils;
import com.android.hcbd.seller.utils.ToastUtils;
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

public class TypelistAdapter extends RecyclerView.Adapter<TypelistAdapter.MyHolder>{
    private Context mContext;
    private List<List<String>> list;
    public TypelistAdapter(Context mContext, List<List<String>> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_type_list_layout, null);
        MyHolder myholder = new MyHolder(view);
        return myholder;
    }

    public List<List<String>> getAllData(){
        return list;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.tv_order.setText(""+(position+1));
        holder.tv_name.setText(list.get(position).get(1));
        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setCancelable(false)
                        .setTitle("删除提示")
                        .setMessage("确认删除吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData(list.get(position).get(0),list.get(position).get(1));
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != itemClickListener) {
                    itemClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    private void deleteData(String id,String name){
        OkGo.<String>post(HttpUrlUtils.get_type_edit_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", id)
                //.params("name", name)
                .params("operate", "delete")
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

                                EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_GOODS_TYPE_UPDATE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                if(!TextUtils.isEmpty(jsonObject.getString("error"))){
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
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0: list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_order)
        TextView tv_order;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_del)
        ImageView iv_del;
        public MyHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View v, int position);
    }

    private onRecyclerViewItemClickListener itemClickListener = null;

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}