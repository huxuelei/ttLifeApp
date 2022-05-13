package com.sidney.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sidney.ui.R;


/**
 * 描 述：网络错误弹框
 * 作 者：hxl  2022/1/28 14:15
 * 修改描述：
 * 修 改 人：xxx  2022/1/28 14:15
 * 修改版本：
 * final IntenetErrorDialog dialog = new IntenetErrorDialog(getActivity());
 * dialog.show("", "", new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * dialog.dismiss();
 * }
 * },
 * new View.OnClickListener() {
 * @Override public void onClick(View v) {
 * dialog.dismiss();
 * }
 * });
 */
public class IntenetErrorDialog extends BaseDialog {

    private TextView mTvSure;

    public IntenetErrorDialog(@NonNull Context context) {
        super(context, R.style.Dialog_FullScreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_intenet_error);
        initView();
    }

    private void initView() {
        mTvSure = findViewById(R.id.tv_sure);
    }

    public void show(View.OnClickListener okListener) {
        this.setCancelable(false);
        super.show();
        if (okListener != null) {
            mTvSure.setOnClickListener(okListener);
        }
    }

}
