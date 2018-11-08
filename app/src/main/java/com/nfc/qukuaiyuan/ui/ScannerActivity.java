package com.nfc.qukuaiyuan.ui;

import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;


import com.google.zxing.Result;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.HttpClient;
import com.nfc.qukuaiyuan.http.RequestBean;
import com.nfc.qukuaiyuan.http.rx.StringSubscriber;
import com.nfc.qukuaiyuan.http.rx.SubscriberOnNextListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwj on 2018/3/27.
 */

public class ScannerActivity extends ToolBarActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView mScannerView;
    private RxPermissions permissions;

    @Override
    protected int getLayoutResId() {
        return R.layout.scanner_activity;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("扫码下单");
        permissions = RxPermissions.getInstance(this);
        mScannerView=findViewById(R.id.scanner_view);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.scanner) {
            mScannerView.setFlash(true);
            boolean flash = mScannerView.getFlash();
            if(flash){
                mScannerView.setFlash(true);
            }else {
                mScannerView.setFlash(false);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

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
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        showToast(result.getText()+"=="+result.getBarcodeFormat().toString());
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        String resultText = result.getText();
        try {
            JSONObject object=new JSONObject(resultText);
            String bId = object.getString("bId");
            String token = object.getString("token");
            showProgressDialog();
            scanOrder(bId,token);
        } catch (JSONException e) {
//            e.printStackTrace();
            showToast("解析异常");
            finish();
        }
    }

    public void scanOrder(String bId,String token){
        RequestBean bean=new RequestBean();
        bean.addParams("bId",bId);
        bean.addParams("token",token);
        HttpClient.getGankRetrofitInstance().doRegister(bean.getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StringSubscriber<String>(new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String data) {
                        hideProgressDialog();

                    }

                    @Override
                    public void onStatus(int status, String message) {
                        if(status==StringSubscriber.SUBSCRIBER_STATU_FAIL){
                            showToast(message);
                        }
                    }
                }));

    }

}
