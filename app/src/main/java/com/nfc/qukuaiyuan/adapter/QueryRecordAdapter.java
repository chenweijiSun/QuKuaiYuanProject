/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
package com.nfc.qukuaiyuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.blankj.utilcode.utils.TimeUtils;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.adapter.base.RecyBaseAdapter;
import com.nfc.qukuaiyuan.adapter.base.RecyHolder;
import com.nfc.qukuaiyuan.adapter.base.RecyItemClickListener;
import com.nfc.qukuaiyuan.model.entity.QueryRecordInfo;

import java.text.SimpleDateFormat;

/**
 * @author chenweiji
 * @version Id: QueryRecordAdapter.java, v 0.1 2018/11/8 16:19 chenweiji Exp $$
 */
public class QueryRecordAdapter extends RecyBaseAdapter<QueryRecordInfo, QueryRecordAdapter.ViewHolder> {
    public QueryRecordAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_query_record, parent, false);
        return new ViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QueryRecordInfo item = getItem(position);
        holder.tvName.setText(item.getGoods_name());
        if(!TextUtils.isEmpty(item.getTime())){
            holder.tvDate.setText(TimeUtils.milliseconds2String(Long.parseLong(item.getTime()),TimeUtils.DEFAULT_SDF));
        }
        holder.tvReal.setText(item.getFact());
    }

    static class ViewHolder extends RecyHolder{
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_real)
        TextView tvReal;
        public ViewHolder(View view, RecyItemClickListener listener) {
            super(view, listener);
        }
    }
}