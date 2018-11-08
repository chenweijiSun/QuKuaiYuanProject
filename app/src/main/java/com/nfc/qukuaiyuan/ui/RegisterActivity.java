package com.nfc.qukuaiyuan.ui;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class RegisterActivity extends ToolBarActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.act_register;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("注册");

    }
}
