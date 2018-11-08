package com.nfc.qukuaiyuan.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by cwj on 2018/3/21.
 */

public abstract   class RecyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyItemClickListener listener;

    public RecyHolder(View view,RecyItemClickListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
