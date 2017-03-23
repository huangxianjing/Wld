package com.wld.net.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */

public abstract class WLDBaseAdapter<T> extends android.widget.BaseAdapter {
    protected List<T> list;
    protected Context mContext;
    protected LayoutInflater inflater;
    protected IBaseAdapterListener adapterListener;

    public WLDBaseAdapter(Context context) {
        super();
        this.mContext = context;
        this.list = new ArrayList<T>();
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(Collection<? extends T> l) {
        if (this.list == null) {
            list = new ArrayList<T>();
        }
        if (l != null) {
            this.list.clear();
            this.list.addAll(l);
            this.notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WLDViewHolder viewHolder;
        BaseOnClickListener listener = null;
        if (convertView == null) {
            convertView = inflater.inflate(getContentViewResource(), parent, false);
            viewHolder = getViewHolder();
            initAdapterView(viewHolder, convertView);
            convertView.setTag(viewHolder);
            if (adapterListener != null) {
                listener = new BaseOnClickListener();
                listener.setAdapterListener(adapterListener);
                listener.setViewHolder(viewHolder);
                listener.setPosition(position);
                adapterListener.getListenerView(viewHolder).setOnClickListener(listener);
                convertView.setTag(adapterListener.getListenerId(viewHolder), listener);
            }
        } else {
            viewHolder = (WLDViewHolder) convertView.getTag();
            if (adapterListener != null) {
                listener = (BaseOnClickListener) convertView.getTag(adapterListener.getListenerId(viewHolder));
            }
        }
        setHolderContent(viewHolder, position);
        return convertView;
    }

    /**
     * 获取adapter布局文件
     */
    protected abstract int getContentViewResource();

    /**
     * 初始化adapter各种组件
     */
    protected abstract void initAdapterView(WLDViewHolder viewHolder, View convertView);

    /**
     * 获取ViewHolder
     */
    protected abstract WLDViewHolder getViewHolder();

    /**
     * 设置组件内容
     */
    protected abstract void setHolderContent(WLDViewHolder viewHolder, int position);

    /**
     * Adapter单击事件接口
     */
    public interface IBaseAdapterListener {
        void onClick(View v, WLDViewHolder viewHolder, int position);

        View getListenerView(WLDViewHolder viewHolder);

        int getListenerId(WLDViewHolder viewHolder);
    }


    public void setAdapterListener(IBaseAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }
}
