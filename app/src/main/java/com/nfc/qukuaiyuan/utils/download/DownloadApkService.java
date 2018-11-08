package com.nfc.qukuaiyuan.utils.download;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Apk下载服务
 */
public class DownloadApkService extends Service {
    private static final String TAG = "DownloadApkService";
    public static String DOWNLOADPATH = "/apk/";
    public  String appName="member.apk";
    public static final String DOWNLOAD_URL="download_url";
    private ApkInstallReceiver mReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downLoadUrl = intent.getStringExtra(DOWNLOAD_URL);
        if(downLoadUrl.indexOf("/")>0){
            appName=downLoadUrl.substring(downLoadUrl.lastIndexOf("/")+1,downLoadUrl.length());
        }else{
            appName="NewVsersion.apk";
        }
        mReceiver = new ApkInstallReceiver();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if (DownLoadUtils.getInstance(this).canDownload()){
            downloadApk(this,downLoadUrl,"梦享生活正在下载", appName);
        }else {
            DownLoadUtils.getInstance(this).skipToDownloadManager();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }


    /**
     * 下载APK文件
     * @param context
     * @param url
     * @param title
     * @param appName
     */
    private  void downloadApk(Context context, String url, String title,final String appName) {

        //获取存储的下载ID
        long downloadId = ShareUtil.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if(downloadId != -1) {
            //存在downloadId
            DownLoadUtils downLoadUtils = DownLoadUtils.getInstance(context);
            //获取当前状态
            int status = downLoadUtils.getDownloadStatus(downloadId);
            if(DownloadManager.STATUS_SUCCESSFUL == status) {
                //状态为下载成功
                //获取下载路径URI
                Uri downloadUri = downLoadUtils.getDownloadUri(downloadId);
                if(null != downloadUri) {
                    //存在下载的APK，如果两个APK相同，启动更新界面。否之则删除，重新下载。
                    if(compare(getApkInfo(context,downloadUri.getPath()),context)) {
                        mReceiver.installApk(context);
                        return;
                    } else {
                        //删除下载任务以及文件
                        downLoadUtils.getDownloadManager().remove(downloadId);
                    }
                }
                start(context, url, title,appName);
            } else if(DownloadManager.STATUS_FAILED == status) {
                //下载失败,重新下载
                start(context, url, title,appName);
            }else {
                Log.d(context.getPackageName(), "apk is already downloading");
            }
        } else {
            //不存在downloadId，没有下载过APK
            start(context, url, title,appName);
        }
    }
    /**
     * 开始下载
     * @param context
     * @param url
     * @param title
     * @param appName
     */
    private static void start(Context context, String url, String title,String appName) {

        if(hasSDKCard()) {
            long id = DownLoadUtils.getInstance(context).download(url,
                    title, "下载完成后点击打开", appName);
            ShareUtil.putLong(DownloadManager.EXTRA_DOWNLOAD_ID,id);
        } else {
            Toast.makeText(context,"手机未安装SD卡，下载失败",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 是否存在SD卡
     */
    private static boolean hasSDKCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 比较两个APK的信息
     * @param apkInfo
     * @param context
     * @return
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {

        if(null == apkInfo) {
            return false;
        }
        String localPackageName = context.getPackageName();
        if(localPackageName.equals(apkInfo.packageName)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackageName, 0);
                //比较当前APK和下载的APK版本号
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    //如果下载的APK版本号大于当前安装的APK版本号，返回true
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取APK程序信息
     * @param context
     * @param path
     * @return
     */
    private static PackageInfo getApkInfo(Context context, String path) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if(null != pi) {
            return pi;
        }
        return null;
    }


    /**
     * 下载监听广播
     */
    public class ApkInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installApk(context);
                DownloadApkService.this.stopSelf();
            }
        }

        /**
         * 安装apk
         */
        public void installApk(Context context) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DownloadApkService.DOWNLOADPATH + appName);
            if (!file.exists()) {
                Log.e(TAG, "file is not exists");
                return;
            }
            Log.e(TAG, "file:"+file.getAbsolutePath());

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            Uri downloadUri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                downloadUri = Uri.fromFile(file);
            } else {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                downloadUri = FileProvider.getUriForFile(context,  "com.dreamlife.pro.fileprovider", file);
                grantUriPermission(context, downloadUri, intent);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(downloadUri, type);
            context.startActivity(intent);
        }

    }
    private  void grantUriPermission (Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

}
