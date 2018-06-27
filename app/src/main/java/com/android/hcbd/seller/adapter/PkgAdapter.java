package com.android.hcbd.seller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.hcbd.seller.MyApplication;
import com.android.hcbd.seller.R;
import com.android.hcbd.seller.entity.PkgEditInfo;
import com.android.hcbd.seller.entity.PkgInfo;
import com.android.hcbd.seller.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/11.
 */

public class PkgAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private List<PkgInfo> pkgInfoList;
    private List<PkgEditInfo> list;
    public PkgAdapter(Context mContext, List<PkgInfo> pkgInfoList){
        this.mContext = mContext;
        this.pkgInfoList = pkgInfoList;
        if(this.list == null){
            this.list = new ArrayList<>();
            this.list.add(null);
        }
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

    public void setData(List<PkgEditInfo> list){
        this.list = list;
    }

    public List<PkgEditInfo> getAllData(){
        return list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_pkg_edit_layout,null);
        holder = new ViewHolder(view);
        //这里不需要复用
        /*if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_pkg_edit_layout,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }*/
        if (position == getCount()-1){
            holder.iv_add.setSelected(true);
        }else{
            holder.iv_add.setSelected(false);
        }

        if(list.get(position) != null){
            holder.tv_category.setText(list.get(position).getPkg().getName());
            holder.et_num.setText(""+list.get(position).getNum());
        }else{
            holder.tv_category.setText("");
            holder.et_num.setText("");
        }
        final ViewHolder finalHolder = holder;
        holder.ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(position,finalHolder.tv_category,finalHolder.et_num);
            }
        });
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalHolder.ll_category.setFocusable(true);
                finalHolder.ll_category.setFocusableInTouchMode(true);

                if(finalHolder.iv_add.isSelected()){
                    if(TextUtils.isEmpty(finalHolder.tv_category.getText().toString())){
                        ToastUtils.showLongToast(MyApplication.getInstance(),"请选择套餐内容");
                        return;
                    }
                    if(TextUtils.isEmpty(finalHolder.et_num.getText().toString())){
                        ToastUtils.showLongToast(MyApplication.getInstance(),"请输入选择数量");
                        return;
                    }
                    finalHolder.iv_add.setSelected(false);
                    list.add(null);
                }else{
                    list.remove(position);
                }
                notifyDataSetChanged();
            }
        });
        holder.et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable.toString())){
                    PkgEditInfo pkgEditInfo = list.get(position);
                    if(pkgEditInfo == null)
                        pkgEditInfo = new PkgEditInfo();
                    pkgEditInfo.setNum(Integer.parseInt(editable.toString()));
                    PkgInfo info = pkgEditInfo.getPkg();
                    if(info == null)
                        info = new PkgInfo();
                    info.setNum(Integer.parseInt(editable.toString()));
                    //list.set(position,info);
                }

            }
        });
        return view;
    }

    class ViewHolder{
        @BindView(R.id.ll_category)
        LinearLayout ll_category;
        @BindView(R.id.tv_category)
        TextView tv_category;
        @BindView(R.id.et_num)
        EditText et_num;
        @BindView(R.id.iv_add)
        ImageView iv_add;
        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }
    }

    public void showSelectedDialog(final int p, final TextView tv, final EditText et) {
        final String[] items = new String[pkgInfoList.size()];
        for(int i=0;i<pkgInfoList.size();i++){
            items[i] = pkgInfoList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  //先得到构造器
        builder.setTitle("请选择"); //设置标题
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tv.setText(items[which]);
                et.setText(""+pkgInfoList.get(which).getNum());

                PkgEditInfo pkgEditInfo = list.get(p);
                if(pkgEditInfo == null)
                    pkgEditInfo = new PkgEditInfo();
                pkgEditInfo.setNum(pkgInfoList.get(which).getNum());

                PkgInfo info = new PkgInfo();
                info.setId(pkgInfoList.get(which).getId());
                info.setName(items[which]);
                info.setNum(pkgInfoList.get(which).getNum());

                pkgEditInfo.setPkg(info);
                list.set(p,pkgEditInfo);
            }
        });
        builder.create().show();
    }


}
