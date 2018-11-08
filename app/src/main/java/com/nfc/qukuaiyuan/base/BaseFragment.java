package com.nfc.qukuaiyuan.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by cwj on 16/7/17.
 */
public  abstract  class BaseFragment<T extends BasePresenter> extends Fragment implements OnClickListener {
    public BaseActivity mActivity;
    public LayoutInflater mInflater;
    public Context mContext;
    protected T presenter;

    /**
     * Notice:Never use this constructor<br />
     * 只是防止Fragment重载崩溃
     */
    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity =(BaseActivity)activity;
        } catch (Exception e) {
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view;
        if(getLayoutResId()>0){
            view= inflater.inflate(getLayoutResId(), container, false);
        }else{
            view=super.onCreateView(inflater, container, savedInstanceState);
        }
        ButterKnife.bind(this, view);
        init();
        //想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法，否则Toolbar没有菜单
        setHasOptionsMenu(true);
        return view;
    }
    /**
     * 创建View
     */
    protected abstract int getLayoutResId();
    protected abstract void init();
    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    protected  void onUserVisible(){}
    protected <VT extends View> VT findViewById(int id) {
        if(getView()!=null)
            return  (VT)getView().findViewById(id);
        else
            return null;
    }
    protected <VT extends View> VT findViewById(int id, OnClickListener listener){
        VT view=findViewById(id);
        if(view!=null){
            view.setOnClickListener(listener);
        }
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.release();
        }
    }

    @Override
    public void onClick(View v) {

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
    /**
     * 主线程执行命令
     *
     * @param what
     */
    public final void call(int what) {
        call(what, new Message());
    }

    public final void callDelayed(int what, long mills) {
        call(what, new Message(), mills);
    }

    public final void call(int what, Message msg) {
        call(what, msg, 0);
    }

    public final void call(int what, Message msg, long mills) {
        msg.what = what;
        mHandler.sendMessageDelayed(msg, mills);
    }

    /**
     * 统一操作UI入口
     *
     * @param what
     */
    protected abstract void ui(int what, Message msg);

    @SuppressLint("HandlerLeak")
    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ui(msg.what, msg);
        }
    };
    public void setOnClickListener(int...resId){
        for(int i=0;i<resId.length;i++){
            try {
                findViewById(resId[i],this);
            } catch (Exception e) {
            }
        }
    }
    public void setOnClickListener(View...views){
        for(int i=0;i<views.length;i++){
            try {
                views[i].setOnClickListener(this);
            } catch (Exception e) {

            }
        }
    }
    public void setRootViewClickListener(View root, int...resId){
        for(int i=0;i<resId.length;i++){
            try {
                root.findViewById(resId[i]).setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void hideToolBar(){
        if(getActivity() instanceof  ToolBarActivity){
            ((ToolBarActivity)getActivity()).hideToolBar();
        }
    }
    protected void hideOrShowToolBar() {
        if(getActivity() instanceof  ToolBarActivity){
            ((ToolBarActivity)getActivity()).hideOrShowToolBar();
        }
    }

    public void showToast(int resId){
        mActivity.showToast(resId);
    }

    public void showToast(String text) {
        mActivity.showToast(text);

    }

}
