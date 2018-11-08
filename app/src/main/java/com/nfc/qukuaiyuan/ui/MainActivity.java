package com.nfc.qukuaiyuan.ui;


import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class MainActivity extends ToolBarActivity {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initTitle("MEGANFC");
    }
}
