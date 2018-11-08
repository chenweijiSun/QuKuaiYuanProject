package com.nfc.qukuaiyuan.ui;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends ToolBarActivity {


    @Bind(R.id.iv_nfc)
    ImageView ivNfc;
    @Bind(R.id.iv_erweima)
    ImageView ivErweima;
    @Bind(R.id.iv_personal)
    ImageView ivPersonal;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.bottom)
    RadioGroup bottom;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initTitle("MEGANFC");
    }

    @OnClick({R.id.iv_nfc, R.id.iv_erweima, R.id.iv_personal, R.id.iv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nfc:
                startActivity(new Intent(this, NFCActivity.class));
                break;
            case R.id.iv_erweima:
                Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_personal:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
