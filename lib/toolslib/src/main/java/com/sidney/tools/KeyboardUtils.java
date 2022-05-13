package com.sidney.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/17 16:30
 * desc:InputMethodUtils 键盘工具类管理
 * version:1.0
 */
public class KeyboardUtils {

    /**
     * 软键盘显示
     *
     * @param context
     */
    public static void showSoft(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 软键盘显示
     *
     * @param view
     * @return
     */
    public static boolean showSoft(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 软键盘显示
     *
     * @param activity
     * @return
     */
    public static boolean showSoft(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        return false;
    }

    /**
     * 软键盘隐藏
     *
     * @param view
     * @return
     */
    public static boolean hideSoft(View view) {
        if (view == null) {
            return true;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return true;
        }
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 软键盘隐藏
     *
     * @param activity
     * @return
     */
    public static boolean hideSoft(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 获取输入法打开的状态
     *
     * @param context
     * @return
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 切换软键盘
     */
    public static void toggleSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.toggleSoftInput(0, 0);
    }

}
