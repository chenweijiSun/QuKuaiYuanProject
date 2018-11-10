package com.nfc.qukuaiyuan.ui;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.utils.NfcUtil;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NFCActivity extends ToolBarActivity {
    @Bind(R.id.btn_write)
    Button btnWrite;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_nfc;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("NFC扫描");
        NfcUtil nfcUtils = new NfcUtil(this);



        if(BuildConfig.DEBUG){
            btnWrite.setVisibility(View.VISIBLE);
        }else{
            btnWrite.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        NfcUtil.mNfcAdapter.enableForegroundDispatch(this, NfcUtil.mPendingIntent, NfcUtil.mIntentFilter, NfcUtil.mTechList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        NfcUtil.mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当该Activity接收到NFC标签时，运行该方法
        //调用工具方法，读取NFC数据
        try {
            String str = NfcUtil.readNfcTag(intent);
            JUtils.Log("cwj", "NFC-->" + str);
            Intent intent1 = new Intent(this, ScanResultActivity.class);
            intent1.putExtra("uid", str);
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("读取失败");
        }
    }

    @OnClick(R.id.btn_write)
    public void onClick() {
        startActivity(new Intent(this,NFCWriteActivity.class));
        finish();
    }



}
