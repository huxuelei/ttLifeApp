package com.sidney.ui.view;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 描 述：全局点击去重
 * 作 者：hxl  2022/1/28 14:20
 * 修改描述：
 * 修 改 人：xxx  2022/1/28 14:20
 * 修改版本：
 * private static final int BTN_LIMIT_TIME = 500; // 防重复点击时间
 * public void setOnItemListener(OnClickListener listener) {
 * OnClickListener myListener = new ToHeavyOnClickListener(listener, BTN_LIMIT_TIME);
 * findViewById(R.id.lineLay_layout).setOnClickListener(myListener);
 * }
 */
public class ToHeavyOnClickListener implements OnClickListener {

    // 全局防频繁点击
    private static long lastClick;

    private OnClickListener listener;

    private long intervalClick;

    public ToHeavyOnClickListener(OnClickListener listener, long intervalClick) {
        this.intervalClick = intervalClick;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() > lastClick
                && System.currentTimeMillis() - lastClick <= intervalClick) {
            return;
        }
        listener.onClick(v);
        lastClick = System.currentTimeMillis();
    }
}
