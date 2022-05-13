package com.sidney.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/18 17:52
 * desc:app信息
 * version:1.0
 */
public class AppInfoUtils {

    /**
     * 获取App名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {


        try {
            PackageManager packageManager;
            ApplicationInfo applicationInfo;
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }


    /**
     * 获取App版本号
     */
    public static int getVersionCode(Context context) {
        int version = 1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return version;
    }

    /**
     * 获取App版本名称
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return version;
    }

    /**
     * 获取当前应用的包名
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

}
