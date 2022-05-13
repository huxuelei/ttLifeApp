package com.sidney.ui.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 描 述：CommonPopupWindow
 * 作 者：hxl  2022/1/6 16:31
 * 修改描述：
 * 修 改 人：xxx  2022/1/6 16:31
 * 修改版本：
 * <p>
 * CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(this)
 * //设置PopupWindow布局
 * .setView(R.layout.popup_down)
 * //设置宽高
 * .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
 * ViewGroup.LayoutParams.WRAP_CONTENT)
 * //设置动画
 * .setAnimationStyle(R.style.AnimDown)
 * //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
 * .setBackGroundLevel(0.5f)
 * //设置PopupWindow里的子View及点击事件
 * .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
 *
 * @Override public void getChildView(View view, int layoutResId) {
 * TextView tv_child = (TextView) view.findViewById(R.id.tv_child);
 * tv_child.setText("我是子View");
 * }
 * })
 * //设置外部是否可点击 默认是true
 * .setOutsideTouchable(true)
 * //开始构建
 * .create();
 * //弹出PopupWindow
 * popupWindow.showAsDropDown(view);
 */
public class CommonPopupWindow extends PopupWindow {
    final PopupController controller;

    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }

    public interface ViewInterface {
        void getChildView(View view, int layoutResId);
    }

    private CommonPopupWindow(Context context) {
        controller = new PopupController(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        controller.setBackGroundLevel(1.0f);
    }

    public static class Builder {
        private final PopupController.PopupParams params;
        private ViewInterface listener;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
//            params.mView = view;
//            params.layoutResId = 0;
            params.mView = view;
            params.layoutResId = view == null ? 0 : view.getId();
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContext);
            params.apply(popupWindow.controller);
            if (listener != null && params.layoutResId != 0) {
                listener.getChildView(popupWindow.controller.mPopupView, params.layoutResId);
            }
            measureWidthAndHeight(popupWindow.controller.mPopupView);
            return popupWindow;
        }
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    private static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
