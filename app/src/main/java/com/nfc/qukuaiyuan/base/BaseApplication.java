package com.nfc.qukuaiyuan.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.blankj.utilcode.utils.SPUtils;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.model.entity.UserInfo;
import com.nfc.qukuaiyuan.utils.DeviceUtils;
import com.nfc.qukuaiyuan.utils.jutils.JFileManager;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;
import com.socks.library.KLog;


/**
 * Created by cwj on 16/7/16.
 */
public class BaseApplication extends Application {
    private static BaseApplication app;
    private String deviceId;
    private UserInfo userInfo;
    public String weChatPayMode;//"recharge","purchase"

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //初始化传入App文件目录列表。会在app的目录下会生成(Dir枚举)数个文件夹
        /**
         * 根据枚举类型获取目录。Folder对象提供本目录下多种文件存取操作
         * JFileManager.Folder folder = JFileManager.getInstance().getFolder(JFileManager.Dir.Image);
         */
        JFileManager.getInstance().init(this, JFileManager.Dir.values());
        /**
         * JUtil工具类初始化
         */
        JUtils.initialize(this);
        KLog.init(BuildConfig.DEBUG, Constant.LOG_USER);
    }
    public static Context getContext() {
        return app.getApplicationContext();
    }
    public static BaseApplication getInstance() {
        return app;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public String getDeviceId(){
        if(deviceId==null){
            deviceId= DeviceUtils.getUniqueId(getContext());
        }
        return deviceId;
    }

    public void saveUser(UserInfo info){
        this.userInfo=info;
        if(info!=null){
            SPUtils sharedPreference = JUtils.getSharedPreference();
            sharedPreference.putString(Constant.LOGIN_USERID_CACHE,info.getUser_id());
            sharedPreference.putString(Constant.LOGIN_USERNAME_CACHE,info.getMobile());
        }
    }

    public String getUserId(){
        if(userInfo!=null){
            return userInfo.getUser_id();
        }else{
            SPUtils sharedPreference = JUtils.getSharedPreference();
            return sharedPreference.getString(Constant.LOGIN_USERID_CACHE,null);
        }
    }


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getUserToken(){
        SPUtils sharedPreference = JUtils.getSharedPreference();
        return sharedPreference.getString(Constant.LOGIN_USER_TOKEN,null);
    }

    public void logout(){
        SPUtils sharedPreference = JUtils.getSharedPreference();
        sharedPreference.clear();
        sharedPreference.putBoolean(Constant.FIRST_LAUNCH,true);
        userInfo=null;
    }

    public void saveToken(String token) {
        JUtils.getSharedPreference().putString(Constant.LOGIN_USER_TOKEN,token);
    }
}
