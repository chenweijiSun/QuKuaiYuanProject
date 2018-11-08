package com.nfc.qukuaiyuan.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;

public class ApkDownload extends Thread {

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_CANCEL = 3;
    private static final String TAG = "CWJ_APK_DOWNLOAD";
    private static int progress;

    private Down_handler handler;

    private static int length;
    private static int count;

    private Context context;
    private static File apkFile;
    private String apkUrl;
    private String saveFileName;

    public ApkDownload(Context context) {
        this.context = context;
        handler = new Down_handler(context);
    }

    public ApkDownload(Context context, String apkUrl) {
        this.context = context;
        this.apkUrl=apkUrl;

        if(TextUtils.isEmpty(apkUrl)){
            Toast.makeText(context, "下载链接不能为空", Toast.LENGTH_LONG).show();
            return ;
        }
        if(apkUrl.indexOf("/")>0){
            saveFileName=apkUrl.substring(apkUrl.lastIndexOf("/")+1,apkUrl.length());
        }else{
            saveFileName="NewVsersion.apk";
        }

        Notification.Builder builder=notificationInit();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        handler = new Down_handler(context, builder, notificationManager);
    }

    private class Down_handler extends Handler {
        WeakReference<Context> mContextReference;
        Notification.Builder builder;
        NotificationManager notificationManager;

        Down_handler(Context context) {
            mContextReference = new WeakReference<Context>(context);
        }

        Down_handler(Context context, Notification.Builder builder, NotificationManager notificationManager) {
            mContextReference = new WeakReference<Context>(context);
            this.builder = builder;
            this.notificationManager = notificationManager;
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = mContextReference.get();
            switch (msg.what) {
                case DOWN_UPDATE:
                    builder.setProgress(length, count, false)
                            .setContentText("下载进度:" + progress + "%");
                    notificationManager.notify(1112, builder.build());
                    break;
                case DOWN_OVER:
                    builder.setTicker("下载完成");
                    notificationManager.notify(1115, builder.build());
                    notificationManager.cancel(1115);
                    length = 0;
                    count = 0;

                    if (checkApk()) {
                        Log.i(TAG, "APK路径:" + apkFile);
                        if (!apkFile.exists()) {
                            return;
                        }
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
//                                "application/vnd.android.package-archive");
//                        context.startActivity(i);

                        openFile(context,apkFile);
                    }
                    break;
                case DOWN_CANCEL:
                    builder.setTicker("下载取消");
                    notificationManager.notify(1115, builder.build());
                    notificationManager.cancel(1115);
                    break;
                default:
                    break;
            }
        }

    }

    public void run() {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            url = new URL(apkUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, String.format("ApkDownloadUrl:%s", apkUrl));
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            length = conn.getContentLength();
            is = conn.getInputStream();
        } catch (FileNotFoundException e0) {
//            e0.printStackTrace();
            try {
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                String location = new String(conn.getHeaderField("Location").getBytes("ISO-8859-1"), "UTF-8").replace(" ", "");
                url = new URL(location);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                length = conn.getContentLength();
                is = conn.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }


        try {
            File file = getCacheDirectory();
            if (!file.exists()) {
                file.mkdir();
            }

            apkFile = new File(file, saveFileName);
            FileOutputStream fos = new FileOutputStream(apkFile);
            long tempFileLength = file.length();
            byte buf[] = new byte[1024];
            int times = 0; //这很重要
            int numread;
            do {
                numread = is.read(buf);
                count += numread;
                progress = (int) (((float) count / length) * 100);
                if ((times == 512) || (tempFileLength == length)) {
                    handler.sendEmptyMessage(DOWN_UPDATE);
                    times = 0;
                }
                times++;
                if (numread <= 0) {
                    handler.sendEmptyMessage(DOWN_OVER);
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            fos.flush();
            fos.close();
            is.close();
            conn.disconnect();

        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        handler.sendEmptyMessage(DOWN_CANCEL);
        super.interrupt();
    }

    //检查当前app与下载的apk包名是否一致
    private  boolean checkApk() {
        String apkName = getAPKPackageName(apkFile.toString());
        String appName = context.getPackageName();
        if (apkName.equals(appName)) {
            Log.i(TAG, "apk检验:包名相同,安装apk");
            return true;
        } else {
            Log.i(TAG, String.format("apk检验:包名不同。该app包名:%s，apk包名:%s", appName, apkName));
            Toast.makeText(context, "apk检验:包名不同,不进行安装", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    //获取apk的包名
    private String getAPKPackageName(String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;
        }
        return null;
    }
    //获取app应用名称
    private String getAppName() {
        String appName = "";
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appName = pi.applicationInfo.loadLabel(context.getPackageManager()).toString();
            if (appName == null || appName.length() <= 0) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }
    //Notification初始化
    private  Notification.Builder notificationInit() {
        Intent intent = new Intent(context, context.getClass());
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

//         .setContentIntent(pIntent)

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("开始下载")
                .setContentTitle(getAppName())
                .setContentText("正在更新")
                .setWhen(System.currentTimeMillis());
        return builder;
    }
    //获取apk存储路径
    public File getCacheDirectory() {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission()) {
            appCacheDir = context.getExternalCacheDir();
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }
    //判断是否有读写条件
    private  boolean hasExternalStoragePermission() {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * 打开文件
     * @param file
     */
    public static void openFile(Context context, File file){

        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = "application/vnd.android.package-archive";
            //设置intent的data和Type属性。android 7.0以上crash,改用provider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String packageName = context.getApplicationContext().getPackageName();
                String authority =  new StringBuilder(packageName).append(".fileprovider").toString();
                Uri fileUri = FileProvider.getUriForFile(context, authority, file);//android 7.0以上
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, type);
                grantUriPermission(context, fileUri, intent);
            }else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            //跳转
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static void grantUriPermission (Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}
