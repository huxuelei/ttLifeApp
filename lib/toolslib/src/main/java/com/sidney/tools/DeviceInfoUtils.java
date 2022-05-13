package com.sidney.tools;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/19 10:26
 * desc:设备信息
 * version:1.0
 */
public class DeviceInfoUtils {

    /**
     * 返回app运行状态
     *
     * @param packageName 要判断应用的包名
     * @return int 1:前台 2:后台 0:不存在
     */
    public static int isAppAlive(Context context, String packageName) {
        try {
            if (TextUtils.isEmpty(packageName)) {
                return 0;
            }
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> listInfos = activityManager
                    .getRunningTasks(20);
            // 判断程序是否在栈顶
            if (listInfos.get(0).topActivity.getPackageName().equals(packageName)) {
                return 1;
            } else {
                // 判断程序是否在栈里
                for (ActivityManager.RunningTaskInfo info : listInfos) {
                    if (info.topActivity.getPackageName().equals(packageName)) {
                        return 2;
                    }
                }
                return 0;// 栈里找不到，返回0
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return 0;// 栈里找不到，返回0
        }
    }

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.
                    getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String szImei = TelephonyMgr.getDeviceId();
            return szImei;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        }
        return "";
    }

    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        try {
            return android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * Sim序列号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getSimSerialNumber(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE)).getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 设备序列号
     *
     * @return
     */
    public static String getDeviceNumber() {
        try {
            return android.os.Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 获取当前设备系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        try {
            return android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 操作系统的版本
     *
     * @return
     */
    public static String getOsVersion() {
        try {
            return System.getProperty("os.version");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 操作系统的名称
     *
     * @return
     */
    public static String getOsName() {
        try {
            return System.getProperty("os.name");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 操作系统的架构
     *
     * @return
     */
    public static String getOsArch() {
        try {
            return System.getProperty("os.arch");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 手机版本号 掌机版本号
     *
     * @return
     */
    public static String getDisplay() {
        try {
            return android.os.Build.DISPLAY;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 硬件制造商
     *
     * @return
     */
    public static String getManufacturer() {
        try {
            return android.os.Build.MANUFACTURER;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 品牌
     *
     * @return
     */
    public static String getBrand() {
        try {
            return android.os.Build.BRAND;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 基板
     *
     * @return
     */
    public static String getBoard() {
        try {
            return android.os.Build.BOARD;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 设备型号
     *
     * @return
     */
    public static String getModel() {
        try {
            return android.os.Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarH(Context context) {
        int statusHeight = 0;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return statusHeight;
    }

    /**
     * 屏幕是否锁屏 如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
     *
     * @param context
     * @return
     */
    public static boolean isScreenState(Context context) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm.isScreenOn();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return false;
    }

    /**
     * 广播定义获取屏幕状态 开屏1;锁屏2;解锁3  注意BroadcastReceiver解绑
     *
     * @param context
     * @param screenStateCallBack
     */
    public static BroadcastReceiver getScreenState(Context context,
                                                   final ScreenStateCallBack screenStateCallBack) {
        try {
            BroadcastReceiver screenReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                        // 开屏
                        screenStateCallBack.onScreenState(1);
                    } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                        // 锁屏
                        screenStateCallBack.onScreenState(2);
                    } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                        // 解锁
                        screenStateCallBack.onScreenState(3);
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            context.registerReceiver(screenReceiver, filter);
            return screenReceiver;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return null;
        }
    }

    public interface ScreenStateCallBack {
        void onScreenState(int state);
    }

}
