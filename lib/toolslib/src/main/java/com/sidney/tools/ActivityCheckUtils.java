package com.sidney.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/16 10:57
 * desc:其他工具管理方法
 * version:1.0
 */
public class ActivityCheckUtils {
    private static final String MAX_VERSION = "2.3.4";
    /**
     * activity是否未释放
     *
     * @param context 上下文
     * @return true：有效  false：已经释放
     */
    public static boolean isActive(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !activity.isDestroyed();
            } else {
                return !activity.isFinishing();
            }
        }
        return true;
    }


    //获取系统中安装的应用包的信息
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }



    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    //是否支持业务 APP 隐藏
    public static boolean checkAppHideIcon(Context context,String pkgName) {
        try{
            String versionName = getAppVersionName(context,pkgName);
            int value = compareVersion(versionName,MAX_VERSION);
            if(value >= 0) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //通过获取某个应用信息并捕获未安装时的异常判断
    public static String getAppVersionName(Context context,String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return "";
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return "";
        } else {
            return packageInfo.versionName;
        }
    }

    /**
     * 版本号比较
     * compareTo()方法返回值为int类型，就是比较两个值，如：x.compareTo(y)。如果前者大于后者，返回1，前者等于后者则返回0，前者小于后者则返回-1
     * @param s1
     * @param s2
     * @return 范围可以是"2.20.", "2.10.0"  ,".20","2.10.0",2.1.3 ，3.7.5，10.2.0
     */
    public static int compareVersion(String s1, String s2) {
        String[] s1Split = s1.split("\\.", -1);
        String[] s2Split = s2.split("\\.", -1);
        int len1 = s1Split.length;
        int len2 = s2Split.length;
        int lim = Math.min(len1, len2);
        int i = 0;
        while (i < lim) {
            int c1 = "".equals(s1Split[i]) ? 0 : Integer.parseInt(s1Split[i]);
            int c2 = "".equals(s2Split[i]) ? 0 : Integer.parseInt(s2Split[i]);
            if (c1 != c2) {
                return c1 - c2;
            }
            i++;
        }
        return len1 - len2;
    }

}
