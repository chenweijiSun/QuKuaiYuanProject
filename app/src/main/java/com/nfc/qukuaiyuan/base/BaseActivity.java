package com.nfc.qukuaiyuan.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.model.entity.UserInfo;
import com.nfc.qukuaiyuan.utils.GsonUtil;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.StringUtils;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import com.nfc.qukuaiyuan.widget.dialog.CommonDialogFragment;
import com.nfc.qukuaiyuan.widget.dialog.DialogFragmentHelper;


import butterknife.ButterKnife;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xybcoder on 2016/3/1.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements OnClickListener {

    protected String TAG = this.getClass().getSimpleName();
    protected T presenter;
    private DialogFragment mDialogFragment;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        //debug模式下 启用严格模式
        if (BuildConfig.DEBUG&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        this.init();
    }
    protected abstract int getLayoutResId();

    public <VT extends View> VT findViewById(int id, OnClickListener listener) {
        VT view= findViewById(id);
        if(view!=null){
            view.setOnClickListener(listener);
        }
        return view;
    }
    public void setOnClickListener(int...resId){
        for(int i=0;i<resId.length;i++){
            findViewById(resId[i],this);
        }
    }
    public void setOnClickListener(View...views){
        for(int i=0;i<views.length;i++){
            views[i].setOnClickListener(this);
        }
    }
    protected abstract void init();

    public void onClick(View view) {};
    public void startActivity(Class<?> mClass, boolean isFinish, Bundle bundle) {
        if(null != bundle) {
            Intent mIntent = new Intent(this, mClass);
            mIntent.putExtras(bundle);
            this.startActivity(mIntent);
        }else {
            this.startActivity(new Intent(this, mClass));
        }
        if(isFinish) {
            finish();
        }
    }

    public void startActivityForResult(Class<?> mClass, boolean isFinish, Bundle bundle, int requestCode) {
        if(null != bundle) {
            Intent mIntent = new Intent(this, mClass);
            mIntent.putExtras(bundle);
            this.startActivityForResult(mIntent, requestCode);
        }else {
            this.startActivityForResult(new Intent(this, mClass), requestCode);
        }
        if(isFinish) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //在Action Bar的最左边，就是Home icon和标题的区域
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //资源释放
        if(presenter!=null){
            presenter.release();
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    };


    private void showProgress(String messgae){
        if(mDialogFragment!=null){
            return;
        }
        mDialogFragment = DialogFragmentHelper.showProgress(getSupportFragmentManager(), messgae, true, new CommonDialogFragment.OnDialogCancelListener() {
            @Override
            public void onCancel() {
                hideProgressDialog();
                if(presenter!=null){
                    presenter.release();
                }
            }
        });
    }
    public void showProgressDialog(int resId){
        showProgress(getResources().getString(resId));
    }
    public void showProgressDialog(String message){
        showProgress(message);
    }
    public void showProgressDialog(){
        showProgress("请稍候...");
    }
    public void hideProgressDialog(){
        if(mDialogFragment!=null){
            mDialogFragment.dismiss();
            mDialogFragment=null;
        }
    }

    public void showToast(int resId){
        showToast(getResources().getString(resId));
    }

    public void showToast(String text) {
        if(StringUtils.isEmpty(text)){
            return;
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (toast == null) {
                    View toastView=getLayoutInflater().inflate(R.layout.toast_layout,null);
                    toast = new Toast(getApplicationContext());
                    toast.setView(toastView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                }
                TextView tv= (TextView) toast.getView();
                tv.setText(text);
                toast.show();
            }
        });
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();

    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                Glide.get(this).clearMemory();
                break;
            default:
                break;
        }
        Glide.get(this).trimMemory(level);
    }

    protected String checkSuccess(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("code")==0){
                return jsonObject.getString("result");
            }else{
                String msg = jsonObject.optString("msg");
                showToast(msg);
            }
        } catch (JSONException e) {
        }
        return null;
    }

    protected boolean checkSuccessReturnBoolean(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("code")==0){
                return true;
            }else{
                String msg = jsonObject.optString("msg");
                showToast(msg);
            }
        } catch (JSONException e) {
        }
        return false;
    }


    protected String checkUserSuccess(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("code")==0){

                JSONObject result = jsonObject.getJSONObject("result");
                String token = result.getString("token");
                BaseApplication.getInstance().saveToken(token);
                return result.getString("user");
            }else{
                String msg = jsonObject.optString("msg");
                showToast(msg);
            }
        } catch (JSONException e) {
        }
        return null;
    }


    protected JSONObject checkSuccessReturnJson(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("code")==0){
                return jsonObject.getJSONObject("result");
            }else{
                String msg = jsonObject.optString("msg");
                showToast(msg);
            }
        } catch (JSONException e) {
        }
        return null;
    }


}
