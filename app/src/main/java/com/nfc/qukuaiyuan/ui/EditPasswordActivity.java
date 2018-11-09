package com.nfc.qukuaiyuan.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

public class EditPasswordActivity extends ToolBarActivity {
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_sms)
    TextView btnSend;
    @Bind(R.id.et_passowrd)
    EditText etPassowrd;
    @Bind(R.id.et_passowrd_sure)
    EditText etPassowrdSure;
    @Bind(R.id.btn_save)
    TextView btnSave;
    private String vCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_edit_password;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("修改密码");
    }


    @Override
    @OnClick({R.id.btn_sms, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sms:
                try {
                    doSendSms();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_save:
                try {
                    doSave();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void doSave() throws JSONException {
        String phone = BaseApplication.getInstance().getUserInfo().getMobile();
        String code = etCode.getText().toString().trim();
        String pwd = etPassowrd.getText().toString().trim();
        String pwdSure = etPassowrdSure.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空");
            return;
        }
        if(pwd.length()<6){
            showToast("密码长度不符");
            return;
        }
        if (TextUtils.isEmpty(pwdSure)) {
            showToast("确认密码不能为空");
            return;
        }
        if(!TextUtils.equals(pwd,pwdSure)){
            showToast("2次输入密码不一致");
            return;
        }

        showProgressDialog("请稍候...");
        JSONObject object=new JSONObject();
        object.put("act","user.update_pw");
        object.put("appid", Constant.APP_ID);
        object.put("mobile",phone);
        object.put("new_password",pwd);
        object.put("ocde",code);
        object.put("re_new_password",pwdSure);
        object.put("sessionkey",Constant.SESSION_KEY);
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign",sign);

        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("修改密码失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                if(checkSuccessReturnBoolean(response)){
                    showToast("修改密码成功");
                    finish();
                }else{
                    showToast("修改密码失败");
                }
            }
        });
    }

    private void doSendSms() throws JSONException {
        String phone = BaseApplication.getInstance().getUserInfo().getMobile();
        vCode=null;
        showProgressDialog("请稍候...");
        JSONObject object=new JSONObject();
        object.put("act","user.get_mobile_code");
        object.put("appid", Constant.APP_ID);
        object.put("mobile",phone);
        object.put("sessionkey",Constant.SESSION_KEY);
        object.put("time", System.currentTimeMillis());
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign",sign);
        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("发送验证码失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                JSONObject jsonObject=checkSuccessReturnJson(response);
                try {
                    if(jsonObject!=null){
                        vCode=jsonObject.getString("code");
                        if(BuildConfig.DEBUG){
                            etCode.setText(vCode);
                        }
                        onSendSucc();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("发送验证码失败");
                }
            }
        });
    }

    private void onSendSucc(){
        countdown_time = System.currentTimeMillis();
        enableVreifyCodeButton();
    }

    private long countdown_time = 0;
    private Runnable runnable;
    @SuppressLint("StringFormatMatches")
    private void enableVreifyCodeButton() {
        long remainTime = System.currentTimeMillis() - countdown_time;
        remainTime = 60 - remainTime / 1000;
        if (remainTime <= 0) {
            btnSend.setEnabled(true);
            btnSend.setText(getString(R.string.login_get_vcode));
            return;
        }
        btnSend.setEnabled(false);
        btnSend.setText(getString(R.string.login_get_vcode_count_down, remainTime));
        runnable = new Runnable() {

            @Override
            public void run() {
                enableVreifyCodeButton();
            }
        };
        mHandler.postDelayed(runnable, 1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
