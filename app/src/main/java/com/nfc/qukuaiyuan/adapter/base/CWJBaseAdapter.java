package com.nfc.qukuaiyuan.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ,基于butterknife的基础的适配器 by cwj
 */
public abstract class CWJBaseAdapter<E,T extends BaseHoler> extends BaseAdapter {

	public List<E> mDatas;

	public Context mContext;

	public CWJBaseAdapter(Context context) {
		super();
		this.mContext = context;
		this.mDatas = new ArrayList<E>();
	}

	@Override
	public int getCount() {
		if(mDatas ==null){
			return 0;
		}else {
			return mDatas.size();
		}
	}

	@Override
	public E getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T holder = null;
        E item = this.getItem(position);
        if(convertView==null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
            holder= bindHoler();
            holder.setConvertView(convertView);
			convertView.setTag(holder);
		}else{
            holder= (T) convertView.getTag();
		}
		bindData(holder,position,item);
		return convertView;
	}
	public abstract T bindHoler();
	public abstract int getLayoutResId();
	public abstract void bindData(T holder, int position,E item);

	public List<E> getData() {
		return this.mDatas;
	}

	public void addNewData(List<E> data) {
		if(data != null) {
			this.mDatas.addAll(0, data);
			this.notifyDataSetChanged();
		}

	}

	public void addMoreData(List<E> data) {
		if(data != null) {
			this.mDatas.addAll(this.mDatas.size(), data);
			this.notifyDataSetChanged();
		}

	}

	public void setData(List<E> data) {
		if(data != null) {
			this.mDatas = data;
		} else {
			this.mDatas.clear();
		}
		this.notifyDataSetChanged();
	}

	public void clear() {
		this.mDatas.clear();
		this.notifyDataSetChanged();
	}

	public void removeItem(int position) {
		this.mDatas.remove(position);
		this.notifyDataSetChanged();
	}

	public void removeItem(E model) {
		this.mDatas.remove(model);
		this.notifyDataSetChanged();
	}

	public void addItem(int position, E model) {
		this.mDatas.add(position, model);
		this.notifyDataSetChanged();
	}

	public void addFirstItem(E model) {
		this.addItem(0, model);
	}

	public void addLastItem(E model) {
		this.addItem(this.mDatas.size(), model);
	}

	public void setItem(int location, E newModel) {
		this.mDatas.set(location, newModel);
		this.notifyDataSetChanged();
	}

	public void setItem(E oldModel, E newModel) {
		this.setItem(this.mDatas.indexOf(oldModel), newModel);
	}

	public void moveItem(int fromPosition, int toPosition) {
		Collections.swap(this.mDatas, fromPosition, toPosition);
		this.notifyDataSetChanged();
	}

}
