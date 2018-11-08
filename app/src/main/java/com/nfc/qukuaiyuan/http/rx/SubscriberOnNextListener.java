package com.nfc.qukuaiyuan.http.rx;

/**
 * Created by cwj on 16/9/5.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onStatus(int status, String message);
}
