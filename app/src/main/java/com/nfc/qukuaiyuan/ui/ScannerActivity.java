package com.nfc.qukuaiyuan.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.zxing.Result;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.HttpClient;
import com.nfc.qukuaiyuan.http.RequestBean;
import com.nfc.qukuaiyuan.http.rx.StringSubscriber;
import com.nfc.qukuaiyuan.http.rx.SubscriberOnNextListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import org.json.JSONException;
import org.json.JSONObject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwj on 2018/3/27.
 */

public class ScannerActivity extends ToolBarActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;
    @Bind(R.id.btn_switch)
    SwitchCompat btnSwitch;
    private RxPermissions permissions;

    @Override
    protected int getLayoutResId() {
        return R.layout.scanner_activity;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("二维码扫描");
        permissions = RxPermissions.getInstance(this);
        mScannerView = findViewById(R.id.scanner_view);
        permissions.request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // 在android 6.0之前会默认返回true
                        // 已经获取权限

                    } else {
                        // 未获取权限
                    }
                });
        mScannerView.setBorderColor(getResources().getColor(R.color.theme_color));
        mScannerView.setLaserColor(Color.GREEN);
        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mScannerView.toggleFlash();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        showToast(result.getText() + "==" + result.getBarcodeFormat().toString());
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        String resultText = result.getText();
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra("code",resultText);
        startActivity(intent);
    }

}
