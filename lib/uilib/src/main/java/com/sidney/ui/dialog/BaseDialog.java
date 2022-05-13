package com.sidney.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatDialog;


/**
 * 描 述：dialog封装
 * 作 者：hxl  2022/1/6 16:24
 * 修改描述：
 * 修 改 人：xxx  2022/1/6 16:24
 * 修改版本：
 */
public class BaseDialog extends AppCompatDialog {
    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
