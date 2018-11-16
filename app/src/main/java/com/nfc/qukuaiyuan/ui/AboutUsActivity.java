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
package com.nfc.qukuaiyuan.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

/**
 * @author chenweiji
 * @version Id: AboutUsActivity.java, v 0.1 2018/11/8 16:29 chenweiji Exp $$
 */
public class AboutUsActivity extends ToolBarActivity {
    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_about_us;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("关于我们");
        String versionName = JUtils.getAppVersionName();
        if(!TextUtils.isEmpty(versionName)){
            tvVersion.setText("版本号："+versionName);
        }
    }


}