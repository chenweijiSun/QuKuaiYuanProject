package com.nfc.qukuaiyuan.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;


import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 2018/3/21.
 */

public abstract class RecyBaseAdapter<T,M extends RecyHolder> extends Adapter<M> {
    protected List<T> data;
    protected RecyItemClickListener itemClickListener;
    protected Context context;

    public RecyBaseAdapter(Context context) {
        this.context=context;
        data=new ArrayList<>();
    }

    public RecyBaseAdapter( Context context,List<T> data) {
        this(context);
        if(data==null){
            JUtils.LogError(getClass().toString()+": data is null");
            return;
        }
        this.data.addAll(data);
    }

    public RecyBaseAdapter(Context context, List<T> data, RecyItemClickListener itemClickListener) {
        this(context,data);
        this.itemClickListener = itemClickListener;
    }


    public void setOnItemClickListener(RecyItemClickListener itemClickListener) {
        this.itemClickListener= itemClickListener;
    }

    public T getItem(int position){
        if(data==null){
            return null;
        }else {
            return data.get(position);
        }
    }
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        if(data==null){
            JUtils.LogError(getClass().toString()+": data is null");
            return;
        }
        this.data = data;
        notifyDataSetChanged();
    }
    public void addMoreData(List<T> list){
        if(data==null){
            data=new ArrayList<>();
        }
        data.addAll(list);
        notifyDataSetChanged();
    }
    public void addMoreData(T t){
        if(data==null){
            data=new ArrayList<>();
        }
        data.add(t);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        if(data!=null){
            data.clear();
            notifyDataSetChanged();
        }
    }
}
