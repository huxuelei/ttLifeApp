package com.sidney.ui.adapter.abslistview.base;


import com.sidney.ui.adapter.abslistview.ViewHolder;

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);


}
