package com.sidney.tools;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/19 15:50
 * desc:ExceptionHandle 异常捕获处理
 * version:1.0
 * <p>调用说明
 * <p>
 * CrashHandlerUtils.getInstance().init(context,
 * new CrashHandlerUtils.SaveCrashCallBack() {
 *
 * @Override public void onSaveLog(Throwable ex) {
 * <p>
 * }
 * });
 */
public class CrashHandlerUtils implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionHandle";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    /**
     * ExceptionHandle实例
     */
    @SuppressLint("StaticFieldLeak")
    private static CrashHandlerUtils instance;
    private Context mContext;


    private SaveCrashCallBack mSaveCrashCallBack;

    private CrashActionCallBack mCrashActionCallBack;

    private CrashHandlerUtils() {
    }

    public static synchronized CrashHandlerUtils getInstance() {
        if (instance == null) {
            instance = new CrashHandlerUtils();
        }
        return instance;
    }

    //异常后执行的跳转
    public interface CrashActionCallBack {
        void onTogo(Throwable ex);
    }

    //保存日志
    public interface SaveCrashCallBack {
        void onSaveLog(Throwable ex);
    }

    public void setCrashActionCallBack(CrashActionCallBack mCrashActionCallBack) {
        this.mCrashActionCallBack = mCrashActionCallBack;
    }

    /**
     * 初始化
     *
     * @param callBack 保存日志文件
     */
    public void init(Context context, SaveCrashCallBack callBack) {
        this.mContext = context;
        this.mSaveCrashCallBack = callBack;
        // 获取系统默认的UncaughtException处理器
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该ExceptionHandle为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean handleException = handleException(ex);
        if (!handleException && defaultExceptionHandler != null) {
            defaultExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //自定义崩溃后的操作
            if (mCrashActionCallBack != null) {
                mCrashActionCallBack.onTogo(ex);
            } else {
                // 只退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (null == ex) {
            return false;
        }
        //保存日志到文件
        if (null != mSaveCrashCallBack) {
            mSaveCrashCallBack.onSaveLog(ex);
        } else {
            LogUtils.e("保存日志到文件的时候SaveCrashCallBackW为空");
            throw new NullPointerException("保存日志到文件的时候SaveCrashCallBackW为空");
        }
        return false;
    }
}
