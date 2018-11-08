package com.nfc.qukuaiyuan.http.rx;

import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.base.IBaseRefreshView;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import rx.Subscriber;


/**
 * Created by cwj on 16/9/5.
 */
public class MySubscriber<T> extends Subscriber<T> implements ProgressCancelListener{

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private IBaseRefreshView iView;

    public MySubscriber( SubscriberOnNextListener<T> mSubscriberOnNextListener) {
        this(null,mSubscriberOnNextListener);
    }

    public MySubscriber(IBaseRefreshView iView, SubscriberOnNextListener<T> mSubscriberOnNextListener){
        this.iView=iView;
        this.mSubscriberOnNextListener=mSubscriberOnNextListener;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {
        if(iView!=null){
            iView.stopRefresh();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(iView!=null){
            iView.stopRefresh();
        }
        if(BuildConfig.DEBUG){
            JUtils.Toast(e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
        if(iView!=null){
            iView.stopRefresh();
        }
    }
}
