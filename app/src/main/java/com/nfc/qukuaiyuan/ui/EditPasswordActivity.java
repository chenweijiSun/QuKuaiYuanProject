package com.nfc.qukuaiyuan.ui;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class EditPasswordActivity extends ToolBarActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.act_edit_password;
    }

    @Override
    protected void init() {

        initTitleAndCanBack("修改密码");
    }
}
