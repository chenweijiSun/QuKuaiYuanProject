package com.nfc.qukuaiyuan.ui.nfc;

import java.nio.charset.Charset;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.widget.Toast;

import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseNfcActivity;

/**
 * Created by gc on 2016/12/8.
 */
public class ReadMUActivity extends BaseNfcActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_read_mu;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.indexOf("MifareUltralight") >= 0) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            Toast.makeText(this, "不支持MifareUltralight数据格式", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = readTag(tag);
        if (data != null)
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    public String readTag(Tag tag) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            byte[] data = ultralight.readPages(4);
            return new String(data, Charset.forName("GB2312"));
        } catch (Exception e) {
        } finally {
            try {
                ultralight.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
