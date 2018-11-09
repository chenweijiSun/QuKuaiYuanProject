package com.nfc.qukuaiyuan.ui;

import android.os.Bundle;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class LoginActivity extends ToolBarActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("登录");
    }
}
