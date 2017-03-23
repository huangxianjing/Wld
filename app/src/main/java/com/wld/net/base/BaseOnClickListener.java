package com.wld.net.base;

import android.view.View;
import android.view.View.OnClickListener;

public class BaseOnClickListener implements OnClickListener {
    private WLDViewHolder viewHolder;
    private int position;
    private WLDBaseAdapter.IBaseAdapterListener adapterListener;


    public void setAdapterListener(WLDBaseAdapter.IBaseAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public void setViewHolder(WLDViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        if (adapterListener != null) {
            adapterListener.onClick(v, viewHolder, position);
        }
    }

}
