package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.OrderItemInfo;
import com.android.hcbd.seller.utils.CommonUtils;
import com.android.hcbd.seller.utils.HttpUrlUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guocheng on 2018/4/28.
 */

public class OrderMenuAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderItemInfo> list;
    public OrderMenuAdapter(Context mContext,List<OrderItemInfo> list){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_menu_layout,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(mContext).load(HttpUrlUtils.BASEURL+list.get(i).getMenu().getUrl()).into(holder.iv);
        holder.tv_name.setText(""+list.get(i).getMenu().getName());
        holder.tv_num.setText("X"+list.get(i).getItem().getNum());
        holder.tv_price.setText("￥"+ CommonUtils.subZeroAndDot(String.valueOf(list.get(i).getItem().getAmt())));
        return view;
    }

    class ViewHolder{
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_num)
        TextView tv_num;
        @BindView(R.id.tv_price)
        TextView tv_price;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

}
