package com.nfc.qukuaiyuan.ui;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.utils.NfcUtil;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.OnClick;

public class NFCWriteActivity extends ToolBarActivity {
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.et_nfc)
    EditText etNfc;
    @Bind(R.id.btn_write)
    TextView btnWrite;
    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_nfc_write;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("写入数据到NFC");
        NfcUtil nfcUtils = new NfcUtil(this);

    }

    @OnClick(R.id.btn_write)
    public void onClick() {

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
            JUtils.Log("cwj","NFC-->"+str);
            tvResult.setText("读出->>"+str);
            write(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write(Intent intent){
        String data = etNfc.getText().toString().trim();
        if(TextUtils.isEmpty(data)){
            showToast("写入的数据不能为空");
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NfcUtil.writeNFCToTagByte(data,intent);
                tvResult.setText("写入->>"+data);
            }else{
                showToast("您的安卓版本过低不能写入数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("写入失败");
        }
    }
}
