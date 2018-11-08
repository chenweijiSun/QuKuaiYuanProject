package com.nfc.qukuaiyuan.adapter.base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by cwj on 16/9/1.
 */
public class BaseHoler {
    private View convertView;

    public void setConvertView(View convertView) {
        this.convertView = convertView;
        ButterKnife.bind(this, convertView);
    }

    public View getConvertView() {
        return convertView;
    }
}
