package com.nfc.qukuaiyuan.http.rx;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.nfc.qukuaiyuan.R;


/**
 * Created by cwj on 16/9/5.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private AlertDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){

        if (pd == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.loadingDialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.loading_dialog, null);
            ImageView iv= (ImageView) view.findViewById(R.id.loading);
//            Glide.with(context).load(R.mipmap.loading).dontAnimate().into(iv);
            pd = builder.create();
            pd.setCanceledOnTouchOutside(false);
            pd.setView(view);
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
