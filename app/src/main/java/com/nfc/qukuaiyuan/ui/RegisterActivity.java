package com.nfc.qukuaiyuan.ui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegularUtils;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.HttpClient;
import com.nfc.qukuaiyuan.http.RequestBean;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.http.rx.StringSubscriber;
import com.nfc.qukuaiyuan.http.rx.SubscriberOnNextListener;
import com.nfc.qukuaiyuan.model.entity.UserInfo;
import com.nfc.qukuaiyuan.utils.GsonUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.StringUtils;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends ToolBarActivity {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.btn_sms)
    TextView btnSend;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_passowrd)
    EditText etPassowrd;
    @Bind(R.id.et_passowrd_sure)
    EditText etPassowrdSure;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.bottom)
    TextView bottom;

    private String vCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_register;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("注册");
    }

    @Override
    @OnClick({R.id.btn_sms, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sms:
                try {
                    doSendSms();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                try {
                    register();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    private void register() throws JSONException {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String pwd = etPassowrd.getText().toString().trim();
        String pwdSure = etPassowrdSure.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }
        if(!RegularUtils.isMobileExact(phone)){
            showToast("手机号格式不对");
            return;
        }
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
        object.put("act","user.add_app_user");
        object.put("appid", Constant.APP_ID);
        object.put("code",code);
        object.put("mobile",phone);
        object.put("name",name);
        object.put("password",pwd);
        object.put("re_password",pwdSure);
        object.put("sessionkey",Constant.SESSION_KEY);
        object.put("time", System.currentTimeMillis());
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign",sign);
        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("注册失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                JUtils.Log("cwj",response);
                boolean result = checkSuccessReturnBoolean(response);
                if(result){
                    JUtils.getSharedPreference().putString(Constant.LOGIN_USERNAME_CACHE,phone);
                    showToast("注册成功");
                    finish();
                }else{
                    showToast("注册失败");
                }
            }
        });


    }


    private void doSendSms() throws JSONException {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return;
        }
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
                JUtils.Log("cwj",response);
                JSONObject jsonObject=checkSuccessReturnJson(response);
                try {
                    if(jsonObject!=null){
                        vCode=jsonObject.getString("code");
                        if(BuildConfig.DEBUG){
                            etCode.setText(vCode);
                        }
                        onSendSucc();
                    }else{
                        showToast("发送验证码失败");
                    }

                } catch (JSONException e) {
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
