package com.nfc.qukuaiyuan.base;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


import com.nfc.qukuaiyuan.R;

import butterknife.Bind;

/**
 * 带Toolbar的基础Activity
 * Created by dell on 2016/4/15.
 */
public abstract class ToolBarActivity<T extends BasePresenter> extends BaseActivity {
    protected boolean isToolBarHiding = false;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.app_bar)
    protected AppBarLayout appBar;
    @Bind(R.id.toolbar_title)
    protected TextView tvToolbarTitle;
    protected T presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();

    }
    protected void initToolBar(){
        showToolBar();
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    public void initTitleAndCanBack(CharSequence title){
        showToolBar();
        if(toolbar!=null){
            tvToolbarTitle.setText(title);
            toolbar.setNavigationIcon(R.mipmap.iv_back);
        }

    }
    public void initTitle(CharSequence title){
        showToolBar();
        if(toolbar!=null){
            tvToolbarTitle.setText(title);
        }
    }
    public void initTitle(int resId){
        showToolBar();
        if(toolbar!=null){
            tvToolbarTitle.setText(resId);
        }
    }

    protected void hideOrShowToolBar() {
        if(appBar==null)
            return;
        appBar.animate()
                .translationY(isToolBarHiding ? 0 : -appBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isToolBarHiding = !isToolBarHiding;
    }

    public void hideToolBar(){
        if(appBar==null)
            return;
        appBar.setVisibility(View.GONE);
    }
    public void showToolBar(){
        if(appBar==null)
            return;
        appBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //资源释放
        if(presenter!=null){
            presenter.release();
        }
    }
}
