/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
package com.nfc.qukuaiyuan.ui;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.R;
import com.nfc.qukuaiyuan.adapter.QueryRecordAdapter;
import com.nfc.qukuaiyuan.base.BaseApplication;
import com.nfc.qukuaiyuan.base.ToolBarActivity;
import com.nfc.qukuaiyuan.http.okhttp.CallBackUtil;
import com.nfc.qukuaiyuan.http.okhttp.OkhttpUtil;
import com.nfc.qukuaiyuan.model.entity.QueryRecordInfo;
import com.nfc.qukuaiyuan.utils.MD5Util;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * @author chenweiji
 * @version Id: QueryRecordActivity.java, v 0.1 2018/11/8 16:02 chenweiji Exp $$
 */
public class QueryRecordActivity extends ToolBarActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    private QueryRecordAdapter adapter;
    private Gson mGson;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_query_record;
    }

    @Override
    protected void init() {
        mGson = new Gson();
        initTitleAndCanBack("查询记录");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new QueryRecordAdapter(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        try {
            doQuery(null, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doQuery(String code, String uid) throws JSONException {
        showProgressDialog("请稍候...");
        JSONObject object = new JSONObject();
        object.put("act", "nfc.user_select");
        object.put("appid", Constant.APP_ID);
        object.put("code", code);
        object.put("sessionkey", Constant.SESSION_KEY);
        object.put("time", System.currentTimeMillis());
        object.put("token", BaseApplication.getInstance().getUserToken());
        object.put("uid", uid);
        String sign = MD5Util.getMD5(Constant.SECRET + object.toString() + Constant.SECRET);
        object.put("sign", sign);
        JUtils.Log("cwj", object.toString());
        OkhttpUtil.okHttpPostJson(Constant.BASE_URL, object.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                hideProgressDialog();
                showToast("查询失败");
            }

            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                String result = checkSuccess(response);
                JUtils.Log("cwj", result);
                if (result != null) {
                    List<QueryRecordInfo> list = fromJsonList(result, QueryRecordInfo.class);
                    adapter.addMoreData(list);

                } else {
                    showToast("查询失败");
                }
            }
        });
    }

    public <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(mGson.fromJson(elem, cls));
        }
        return mList;
    }
}