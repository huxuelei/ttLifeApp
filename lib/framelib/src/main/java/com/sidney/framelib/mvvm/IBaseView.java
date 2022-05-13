package com.sidney.framelib.mvvm;

import android.app.Activity;
import android.content.Context;

/**
 * 描 述：基View，View（Activity, Fragment）都要实现这些接口
 * 作 者：hxl  2022/1/14 9:47
 * 修改描述：
 * 修 改 人：xxx  2022/1/14 9:47
 * 修改版本：
 */
public interface IBaseView {

    /**
     * 获取View的上下文
     *
     * @return
     */
    Context getViewContext();

    /**
     * 获取Activity
     *
     * @return
     */
    Activity getViewActivity();

    /**
     * 显示Toast信息
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示Toast信息
     *
     * @param resid
     */
    void showToast(int resid);

}
