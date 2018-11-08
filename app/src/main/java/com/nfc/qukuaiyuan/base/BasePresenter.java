package com.nfc.qukuaiyuan.base;

import com.nfc.qukuaiyuan.Constant;

import rx.Subscription;

/**
 * 基础presenter
 * Created by cwj
 */
public abstract class BasePresenter<T extends IBaseView>  {

    protected Subscription subscription;
    protected T iView;

    public BasePresenter(T iView) {
        this.iView = iView;
    }

    public void needLogin(){
        if(BaseApplication.getInstance().getUserInfo()==null){
            iView.onFail(Constant.LOGIN_FIRST);
            return;
        }
    }

    public void release(){
        if(subscription!=null){
            subscription.unsubscribe();
        }
    }
}
