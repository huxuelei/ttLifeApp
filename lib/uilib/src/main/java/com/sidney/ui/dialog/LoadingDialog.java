package com.sidney.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sidney.ui.R;

/**
 * 描 述：加载数据弹框
 * 作 者：hxl 2022/1/28 11:03
 * 修改描述： XXX
 * 修 改 人： XXX  2022/1/28 11:03
 * 修改版本： XXX
 */
public class LoadingDialog extends BaseDialog {

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog_Loading);
    }

    /**
     * Builder封装
     */
    public static class Builder {

        private Context context;
        private boolean isCancelable = false;//设置是否可以按返回键取消
        private boolean isCancelOutside = false;//设置点击外部是否取消
        private String msg;//提示文字信息

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置点击外部是否取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        /**
         * 设置提示消息内容
         *
         * @param msg 提示内容
         * @return
         */
        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public LoadingDialog create() {
            LoadingDialog dialog = new LoadingDialog(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            dialog.setContentView(view);
            TextView tvContent = view.findViewById(R.id.tv_content);
            if (TextUtils.isEmpty(msg)) {
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(msg);
            }
            dialog.setCanceledOnTouchOutside(isCancelOutside);
            WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
            attributes.alpha = 0.8f;
            dialog.getWindow().setAttributes(attributes);
            dialog.setCancelable(isCancelable);
            return dialog;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (LoadingDialog.this.isShowing())
                    LoadingDialog.this.dismiss();
                break;
        }
        return true;
    }

}
