package com.nfc.qukuaiyuan.ui;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;

import butterknife.Bind;
import butterknife.OnClick;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.model.entity.UserInfo;
import com.nfc.qukuaiyuan.ui.nfc.NFCTestActivity;
import com.nfc.qukuaiyuan.utils.GsonUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ToolBarActivity {


    @Bind(R.id.iv_nfc)
    ImageView ivNfc;
    @Bind(R.id.iv_erweima)
    ImageView ivErweima;
    @Bind(R.id.iv_personal)
    ImageView ivPersonal;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.bottom)
    RadioGroup bottom;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initTitle("MEGANFC");
    }

    @Override
    @OnClick({R.id.iv_nfc, R.id.iv_erweima, R.id.iv_personal, R.id.iv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nfc:
//                startActivity(new Intent(this, NFCTestActivity.class));
                startActivity(new Intent(this, NFCActivity.class));
                break;
            case R.id.iv_erweima:
                Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_personal:{
                if(BaseApplication.getInstance().getUserInfo()==null){
//                    startActivity(new Intent(this, LoginActivity.class));
                    autoLogin();
                }else{
                    startActivity(new Intent(this, PersonalActivity.class));
                }
            }
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    private void autoLogin(){
        String name = JUtils.getSharedPreference().getString(Constant.LOGIN_USERNAME_CACHE, null);
        String pwd = JUtils.getSharedPreference().getString(Constant.LOGIN_PAWSSWORD_CACHE, null);
        try {
            userLogin(name,pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void userLogin(String phone,String pwd) throws JSONException {
        if(phone==null || pwd==null){
            startActivity(new Intent(this, LoginActivity.class));
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
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                String result = checkUserSuccess(response);
                if(result!=null){
                    UserInfo userInfo = GsonUtil.GsonToBean(result, UserInfo.class);
                    BaseApplication.getInstance().saveUser(userInfo);
                    startActivity(new Intent(MainActivity.this, PersonalActivity.class));
                }
            }
        });
    }
}
