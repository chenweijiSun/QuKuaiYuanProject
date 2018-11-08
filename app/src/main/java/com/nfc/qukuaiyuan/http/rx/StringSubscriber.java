package com.nfc.qukuaiyuan.http.rx;


import com.google.gson.reflect.TypeToken;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.utils.GsonUtil;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import rx.Subscriber;


/**
 * Created by cwj on 16/9/5.
 */
public class StringSubscriber<T> extends Subscriber<String> implements ProgressCancelListener{

    public static int SUBSCRIBER_STATU_FAIL=10033;
    public static int SUBSCRIBER_STATU_CANCEL=10034;
    public static int SUBSCRIBER_STATU_COMPLETED=10035;
    private SubscriberOnNextListener mSubscriberOnNextListener;

    public StringSubscriber(SubscriberOnNextListener<T> mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener=mSubscriberOnNextListener;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {
        mSubscriberOnNextListener.onStatus(SUBSCRIBER_STATU_COMPLETED,null);
    }

    @Override
    public void onError(Throwable e) {
        String s=null;
        if(BuildConfig.DEBUG){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String time = df.format(System.currentTimeMillis());
            s = "http返回异常("+time+"): " + e.getMessage();
            JUtils.Log("cwj_http",s);
            JUtils.ToastLong(s);
        }else{
            s="请求异常";
        }
        mSubscriberOnNextListener.onStatus(SUBSCRIBER_STATU_FAIL,s);
    }

    @Override
    public void onNext(String s) {
        try {
            JUtils.jsonLog("cwj_http",s);
            Type type = new TypeToken<T>() {}.getType();
            mSubscriberOnNextListener.onNext(GsonUtil.jsonStringToObject(s,type));
        } catch (Exception e) {
            e.printStackTrace();
            mSubscriberOnNextListener.onStatus(SUBSCRIBER_STATU_FAIL,"请求异常");
            if(BuildConfig.DEBUG){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String time = df.format(System.currentTimeMillis());
                String error = "http解析异常("+time+"): " + e.getMessage() ;
                JUtils.Log("cwj_http",error);
                JUtils.Toast(error);
            }

        }

    }



    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
        mSubscriberOnNextListener.onStatus(SUBSCRIBER_STATU_CANCEL,"取消请求");
    }
}
