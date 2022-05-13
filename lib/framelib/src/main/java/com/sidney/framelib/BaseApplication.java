package com.sidney.framelib;

import android.app.Application;

import com.hjq.toast.ToastUtils;

/**
 * 描 述：BaseApplication 封装
 * 作 者：hxl  2022/1/7 15:30
 * 修改描述：
 * 修 改 人：xxx  2022/1/7 15:30
 * 修改版本：
 */
public class BaseApplication extends Application {

    //private volatile static BaseApplication mSingleton;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 Toast 框架
        ToastUtils.init(this);
    }

//    protected BaseApplication() {
//        mSingleton = new BaseApplication();
//    }
//
//    public static BaseApplication getSingleton() {
//        if (mSingleton == null) {
//            synchronized (BaseApplication.class) {
//                if (mSingleton == null) {
//                    mSingleton = new BaseApplication();
//                }
//            }
//        }
//        return mSingleton;
//    }


}
