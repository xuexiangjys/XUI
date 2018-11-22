package com.xuexiang.xui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 集合列表适配器
 * 
 * @author XUE
 * 
 * @param <T>
 */
public abstract class XListAdapter<T> extends BaseAdapter {
	private List<T> mData = new ArrayList<>();
	private OnListItemListener<T> mOnListItemListener;
	private Context mContext;

	public XListAdapter(Context context) {
		mContext = context;
	}

	public XListAdapter(Context context, OnListItemListener<T> callback) {
		this(context);
		mOnListItemListener = callback;
	}

	public XListAdapter(Context context, List<T> data) {
		mContext = context;
		setData(data);
	}

	public XListAdapter(Context context, T[] data) {
		mContext = context;
		setData(data);
	}

	public void setData(List<T> data) {
		if (data != null) {
			mData.clear();
			mData.addAll(data);
		} else {
			mData.clear();
		}
		notifyDataSetChanged();
	}

	public void setData(T[] data) {
		if (data != null && data.length > 0) {
			setData(Arrays.asList(data));
		}
	}

	public List<T> translateData(T[] data) {
		if (data != null && data.length > 0) {
			List<T> result = new ArrayList<>();
			result.addAll(Arrays.asList(data));
			return result;
		} else {
			return null;
		}
	}

	public void addData(List<T> data) {
		if (data != null && data.size() > 0) {
			if (mData == null) {
				mData = new ArrayList<>();
			}
			mData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(T[] data) {
		addData(Arrays.asList(data));
	}

	public void addData(T data) {
		if (data != null) {
			if (mData == null) {
				mData = new ArrayList<>();
			}
			mData.add(data);
			notifyDataSetChanged();
		}
	}

	public void removeElement(T element) {
		if (mData.contains(element)) {
			mData.remove(element);
			notifyDataSetChanged();
		}
	}

	public void removeElement(int position) {
		if (mData != null && mData.size() > position) {
			mData.remove(position);
			notifyDataSetChanged();
		}
	}

	public void removeElements(List<T> elements) {
		if (mData != null && elements != null && elements.size() > 0 && mData.size() >= elements.size()) {
			for (T element : elements) {
				if (mData.contains(element)) {
					mData.remove(element);
				}
			}
			notifyDataSetChanged();
		}
	}

	public void removeElements(T[] elements) {
		if (elements != null && elements.length > 0) {
			removeElements(Arrays.asList(elements));
		}
	}
	
	public void updateElement(T element, int position) {
		if (position >= 0 && mData.size() > position) {
			mData.remove(position);
			mData.add(position, element);
			notifyDataSetChanged();
		}
	}

	public void addElement(T element) {
		if (element != null) {
			if (mData == null) {
				mData = new ArrayList<T>();
			}
			mData.add(element);
			notifyDataSetChanged();
		}
	}

	public void clearData() {
		if (mData != null) {
			mData.clear();
			notifyDataSetChanged();
		}
	}

	public void clearNotNotify() {
		if (mData != null) {
			mData.clear();
		}
	}

	protected void visible(boolean flag, View view) {
		if (flag) {
			view.setVisibility(View.VISIBLE);
		}
	}

	protected void gone(boolean flag, View view) {
		if (flag) {
			view.setVisibility(View.GONE);
		}
	}

	protected void inVisible(View view) {
		view.setVisibility(View.INVISIBLE);
	}

	protected Drawable getDrawable(int resId) {
		return mContext.getResources().getDrawable(resId);
	}

	protected String getString(int resId) {
		return mContext.getResources().getString(resId);
	}

	protected int getColor(int resId) {
		return mContext.getResources().getColor(resId);
	}

	public List<T> getItems() {
		return mData;
	}

	public void setOnListItemListener(OnListItemListener<T> listener) {
		mOnListItemListener = listener;
	}

	public OnListItemListener<T> getOnListItemListener() {
		return mOnListItemListener;
	}

	public int getSize() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public int getCount() {
		return mData == null || mData.isEmpty() ? 0 : mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData != null ? mData.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	public Context getContext() {
		return mContext;
	}

}
