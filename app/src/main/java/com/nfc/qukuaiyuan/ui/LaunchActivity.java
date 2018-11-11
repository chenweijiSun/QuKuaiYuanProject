package com.nfc.qukuaiyuan.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseActivity;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by cwj on 16/7/17.
 */
public class LaunchActivity extends BaseActivity implements View.OnClickListener{
    private static final int sleepTime = 3000;

    @Override
    protected int getLayoutResId() {
        return R.layout.launch_activity;
    }

    @Override
    protected void init() {
        StatusBarCompat.translucentStatusBar(this);
        Integer time = 2000;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(0,0);

            }
        }, time);
//        mHandler.sendEmptyMessageDelayed(0,sleepTime);

    }

//
//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            if(msg.what==0){
//                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
//                finish();
//                overridePendingTransition(0,0);
//            }else if(msg.what==1){
//                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
//                finish();
//                overridePendingTransition(0,0);
//            }else if(msg.what==2){
//
//            }
//        };
//    };

    @Override
    public void onClick(View view) {
        home();
    }
    public void home(){
//        mHandler.removeMessages(0);
        JUtils.getSharedPreference().putBoolean(Constant.FIRST_LAUNCH,true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }



    @Override
    protected void onDestroy() {
//        mHandler.removeMessages(0);
        super.onDestroy();
    }
}
