package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.hcbd.seller.R;

import java.util.List;

/**
 * 描述：    左侧Adapter
 */
public class LeftAdapter extends BaseAdapter {
    //标题
    private List<List<String>> leftList;
    //标志
    private List<Boolean> flagArray;
    private LayoutInflater inflater;

    public LeftAdapter(Context mContext, List<List<String>> leftList, List<Boolean> flagArray) {
        this.leftList = leftList;
        this.flagArray = flagArray;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leftList == null ? 0 : leftList.size();
    }

    @Override
    public Object getItem(int position) {
        return leftList == null ? null : leftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.lv_item_left, parent, false);
            //绑定
            holder.lv_left_item_text = (TextView) convertView.findViewById(R.id.lv_left_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.lv_left_item_text.setText(leftList.get(position).get(1));
        //根据标志位，设置背景颜色
        if (flagArray.get(position)) {
            holder.lv_left_item_text.setTextColor(0xFF111111);
            holder.lv_left_item_text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.lv_left_item_text.setTextColor(0xFF666666);
            holder.lv_left_item_text.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        return convertView;
    }

    class ViewHolder {
        private TextView lv_left_item_text;
    }
}