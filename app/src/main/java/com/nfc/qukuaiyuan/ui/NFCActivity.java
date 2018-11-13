package com.nfc.qukuaiyuan.ui;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.utils.Base64;
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
    @Bind(R.id.tv_uid_base64)
    TextView tv_uid_base64;
    @Bind(R.id.tv_uid_16)
    TextView tv_uid_16;

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
            String uid = resolveIntent(intent);
            JUtils.Log("cwj", "UID-->" + uid);
            Intent intent1 = new Intent(this, ScanResultActivity.class);
            intent1.putExtra("uid", str);
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("读取失败");
        }
    }


    private String resolveIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            return processTag(intent);
        }
        return null;
    }

    public String processTag(Intent intent) {//处理tag
        String uid = "";
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //      str+="Tech List:"+tagFromIntent.getTechList()[0]+"\n";//打印卡的技术列表
        byte[] aa = tagFromIntent.getId();
        uid += bytesToHexString(aa);//获取卡的UID
        if(BuildConfig.DEBUG){
            String encode = Base64.encode(aa);
            tv_uid_base64.setText("base64的UID->"+encode);
            tv_uid_16.setText("16进制的UID->"+uid);
        }
        return uid;
    }


    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }


    @OnClick(R.id.btn_write)
    public void onClick() {
        startActivity(new Intent(this,NFCWriteActivity.class));
        finish();
    }



}
