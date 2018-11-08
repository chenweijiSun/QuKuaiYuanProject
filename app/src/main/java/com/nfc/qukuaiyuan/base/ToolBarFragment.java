package com.nfc.qukuaiyuan.base;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cwj on 16/9/1.
 */
public abstract  class ToolBarFragment<T extends BasePresenter> extends BaseFragment {
    protected T presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initTitleAndCanBack(CharSequence title){
        ((ToolBarActivity)getActivity()).initTitleAndCanBack(title);

    }
    public void initTitle(CharSequence title){
        ((ToolBarActivity)getActivity()).initTitle(title);

    }
    public void initTitle(int resId){
        ((ToolBarActivity)getActivity()).initTitle(resId);
    }

    protected void hideOrShowToolBar() {
        ((ToolBarActivity)getActivity()).hideOrShowToolBar();
    }

    @Override
    protected void ui(int what, Message msg) {

    }

    protected void hideToolBar(){
        ((ToolBarActivity)getActivity()).hideToolBar();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.release();
        }
    }
}
