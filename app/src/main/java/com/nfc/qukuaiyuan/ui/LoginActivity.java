package com.nfc.qukuaiyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.model.entity.UserInfo;
import com.nfc.qukuaiyuan.utils.GsonUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends ToolBarActivity {

    @Bind(R.id.iv_touxiang)
    ImageView ivTouxiang;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_passowrd)
    EditText etPassowrd;
    @Bind(R.id.bottom)
    TextView bottom;
    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("登录");
    }


    @Override
    protected void onResume() {
        super.onResume();
        String username = JUtils.getSharedPreference().getString(Constant.LOGIN_USERNAME_CACHE);
        if(username!=null){
            etPhone.setText(username);
        }
    }

    @Override
    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }

    private void login() throws JSONException {
        String phone = etPhone.getText().toString().trim();
        String pwd = etPassowrd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空");
            return;
        }

        showProgressDialog("请稍候...");
        JSONObject object=new JSONObject();
        object.put("act","user.user_login");
        object.put("appid", Constant.APP_ID);
        object.put("mobile",phone);
        object.put("password",pwd);
        object.put("sessionkey",Constant.SESSION_KEY);
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign",sign);

        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("登录失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                JUtils.Log("cwj",response);
                String result = checkUserSuccess(response);
                if(result!=null){
                    UserInfo userInfo = GsonUtil.GsonToBean(result, UserInfo.class);
                    BaseApplication.getInstance().saveUser(userInfo);
                    JUtils.getSharedPreference().putString(Constant.LOGIN_USERNAME_CACHE,phone);
                    JUtils.getSharedPreference().putString(Constant.LOGIN_PAWSSWORD_CACHE,pwd);
                    showToast("登录成功");
                    finish();
                }
            }
        });
    }
}
