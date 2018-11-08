package com.nfc.qukuaiyuan.ui;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class SettingActivity extends ToolBarActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.act_setting;
    }

    @Override
    protected void init() {

        initTitleAndCanBack("系统设置");
    }
}
