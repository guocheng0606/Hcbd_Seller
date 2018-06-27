package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.TableInfo;
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

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.MyHolder>{
    private Context mContext;
    private List<TableInfo> list;
    public TableListAdapter(Context mContext,List<TableInfo> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_table_layout, null);
        MyHolder myholder = new MyHolder(view);
        return myholder;
    }

    public List<TableInfo> getAllData(){
        return list;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getCode());
        switch (list.get(position).getState()){
            case "2":
                holder.iv.setImageResource(R.drawable.ic_into_seat_nol);
                break;
            case "4":
                holder.iv.setImageResource(R.drawable.ic_reserve_seat_nol);
                break;
            default:
                holder.iv.setImageResource(R.drawable.ic_empty_seat_nol);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list.get(position).getState()){
                    case "2":
                        holder.iv.setImageResource(R.drawable.ic_into_seat_sel);
                        break;
                    case "4":
                        holder.iv.setImageResource(R.drawable.ic_reserve_seat_sel);
                        break;
                    default:
                        holder.iv.setImageResource(R.drawable.ic_empty_seat_sel);
                        break;
                }

                View contentView = LayoutInflater.from(mContext).inflate(R.layout.popup_table_edit_layout,null);
                handleLogic(contentView,list.get(position));
                popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                // 设置好参数之后再show
                popupWindow.showAsDropDown(view);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        switch (list.get(position).getState()){
                            case "2":
                                holder.iv.setImageResource(R.drawable.ic_into_seat_nol);
                                break;
                            case "4":
                                holder.iv.setImageResource(R.drawable.ic_reserve_seat_nol);
                                break;
                            default:
                                holder.iv.setImageResource(R.drawable.ic_empty_seat_nol);
                                break;
                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0: list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_name)
        TextView tv_name;
        public MyHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    PopupWindow popupWindow = null;
    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView, final TableInfo tableInfo){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
                switch (v.getId()){
                    case R.id.tv_menu1:
                        httpTableEdit(tableInfo,"2");
                        break;
                    case R.id.tv_menu2:
                        httpTableEdit(tableInfo,"4");
                        break;
                    case R.id.tv_menu3:
                        httpToPay(tableInfo);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_menu2).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_menu3).setOnClickListener(listener);
    }

    private void httpToPay(final TableInfo tableInfo) {
        OkGo.<String>post(HttpUrlUtils.get_to_pay_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("code", tableInfo.getCode())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                if(Integer.parseInt(jsonObject.getString("data"))>1){
                                    ToastUtils.showShortToast(MyApplication.getInstance(),"不能结算");
                                }else {
                                    httpTableEdit(tableInfo,"1");
                                }
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
                });
    }

    private void httpTableEdit(TableInfo tableInfo,String type){
        OkGo.<String>post(HttpUrlUtils.get_table_operate_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", tableInfo.getId())
                .params("state", type)
                .params("code", type.equals("1") ? tableInfo.getCode() : "")
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

                                MessageEvent messageEvent = new MessageEvent(MessageEvent.EVENT_TABLE_UPDATE);
                                EventBus.getDefault().post(messageEvent);
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
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }


}