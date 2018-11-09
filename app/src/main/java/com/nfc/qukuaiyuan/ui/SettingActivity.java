package com.nfc.qukuaiyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

public class SettingActivity extends ToolBarActivity {
    @Bind(R.id.btn_about_us)
    TextView btnAboutUs;
    @Bind(R.id.btn_version)
    TextView btnVersion;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_setting;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("系统设置");
    }


    @Override
    @OnClick({R.id.btn_about_us, R.id.btn_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.btn_version:
                showToast("已经是最新版本");
                break;
        }
    }
}
