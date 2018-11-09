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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.model.entity.UserInfo;

/**
 * @author chenweiji
 * @version Id: PersonalActivity.java, v 0.1 2018/11/9 17:34 chenweiji Exp $$
 */
public class PersonalActivity extends ToolBarActivity {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.btn_query)
    TextView btnQuery;
    @Bind(R.id.btn_edit_pwd)
    TextView btnEditPwd;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_personal;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("个人中心");
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        tvName.setText(userInfo.getName());
        tvPhone.setText(userInfo.getMobile());
    }

    @Override
    @OnClick({R.id.btn_query, R.id.btn_edit_pwd,R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                startActivity(new Intent(this,QueryRecordActivity.class));
                break;
            case R.id.btn_edit_pwd:
                startActivity(new Intent(this,EditPasswordActivity.class));
                break;
            case R.id.btn_logout:
                showToast("退出成功");
                BaseApplication.getInstance().logout();
                finish();
                break;
        }
    }
}