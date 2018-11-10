/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
package com.nfc.qukuaiyuan.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chenweiji
 * @version Id: ScanResultActivity.java, v 0.1 2018/11/8 16:42 chenweiji Exp $$
 */
public class ScanResultActivity extends ToolBarActivity {
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_scan_result;
    }

    @Override
    protected void init() {
        initTitleAndCanBack("查询结果");
        initWebView();
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        String uid = intent.getStringExtra("uid");
        try {
            if (code != null) {
                doQueryCode(code);
            } else if (uid != null) {
                doQueryUid(uid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void doQueryUid(String uid) throws JSONException {
        showProgressDialog("请稍候...");
        JSONObject object = new JSONObject();
        object.put("act", "nfc.get_nfc_data");
        object.put("appid", Constant.APP_ID);
        object.put("sessionkey", Constant.SESSION_KEY);
        object.put("time", System.currentTimeMillis());
        object.put("token", BaseApplication.getInstance().getUserToken());
        object.put("uid", uid);
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign", sign);
        JUtils.Log("cwj", object.toString());
        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("查询失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                JUtils.Log("cwj", response);
                JSONObject result = checkSuccessReturnJson(response);
                if(result!=null){
                    String url = result.optString("url");
                    webView.loadUrl(url);
                }else {
                    showToast("加载失败，请重试");
                }
            }
        });
    }

    private void doQueryCode(String code) throws JSONException {
        showProgressDialog("请稍候...");
        JSONObject object = new JSONObject();
        object.put("act", "nfc.get_code_data");
        object.put("appid", Constant.APP_ID);
        object.put("code", code);
        object.put("sessionkey", Constant.SESSION_KEY);
        object.put("time", System.currentTimeMillis());
        object.put("token", BaseApplication.getInstance().getUserToken());
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign", sign);
        JUtils.Log("cwj", object.toString());
        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("查询失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                JUtils.Log("cwj", response);
                JSONObject result = checkSuccessReturnJson(response);
                String url = result.optString("url");
                webView.loadUrl(url);
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView(){
        webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings=webView.getSettings();
        //允许使用js
        webSettings.setJavaScriptEnabled(true);

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

    }
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen","拦截url:"+url);

            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient=new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen","网页标题:"+title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * JS调用android的方法
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void  getClient(String str){
        Log.i("ansen","html调用客户端:"+str);
    }


}