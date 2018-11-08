package com.nfc.qukuaiyuan.http;

import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.util.Map;
import java.util.TreeMap;

public class RequestBean {
	private Map<String,String> params; // 参数列表

	public RequestBean(){
		this.params = new TreeMap<>();
		// 默认必须添加的参数
		this.addParams("source", Constant.REQUEST_SOURCE);
		this.addParams("deviceId", BaseApplication.getInstance().getDeviceId());
		Integer userId = BaseApplication.getInstance().getUserId();
		if(userId!=-1){
			this.addParams("userId", userId);
		}
		String token = BaseApplication.getInstance().getUserToken();
		if(token!=null){
			this.addParams("token", token);
		}
	}

	/**
	 * 添加请求参数
	 * 
	 * @param key
	 * @param value
	 */
	public RequestBean addParams(String key, String value) {
		params.put(key,value);
		return this;
	}
	public RequestBean addParams(String key, boolean value) {
		params.put(key,String.valueOf(value));
		return this;
	}
	public RequestBean addParams(String key, int value) {
		params.put(key, String.valueOf(value));
		return this;
	}
	public Map<String,String> getParams(){

		if(BuildConfig.DEBUG){
			String s = JUtils.transMapToString(params);
			JUtils.Log("cwj_http_header",s);
		}
		return params;
	}

}
