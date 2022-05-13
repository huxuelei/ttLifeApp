package com.sidney.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

import androidx.core.app.ActivityCompat;

/**
 * 描 述：Activity 管理器
 * 作 者：huxuelei  2020/7/3 11:32
 * 修改描述：
 * 修 改 人：xxx 2020/7/3 11:32
 * 修改版本：
 */
public class ActivityManageUtils {

    private static Stack<Activity> activityStack = null;
    private static ActivityManageUtils activityManager;

    private ActivityManageUtils() {
    }

    /**
     * 单一实例
     */
    public static ActivityManageUtils getInstance() {
        if (activityManager == null) {
            synchronized (ActivityManager.class) {
                activityManager = new ActivityManageUtils();
                if (activityStack == null) {
                    activityStack = new Stack<>();
                }
            }

        }
        return activityManager;
    }

    public int getCount() {
        if (activityStack == null)
            activityStack = new Stack<Activity>();
        for (Activity activity : activityStack) {
            LogUtils.d("activityStack  activity " + activity.getClass().getSimpleName());
        }
        return activityStack.size();
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        synchronized (activityStack) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity;
        synchronized (activityStack) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        Activity activity = activityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        synchronized (activityStack) {
            if (activity != null) {
                LogUtils.d("activity关闭前activityStack数量：" + activityStack.size());
                activityStack.remove(activity);
                ActivityCompat.finishAfterTransition(activity);
                activity = null;
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        synchronized (activityStack) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    killActivity(activity);
                }
            }
        }
    }

    /**
     * 结束除指定Activity的其他Activity
     *
     * @param cls
     */
    public void killOtherActivity(Class<?> cls) {
        synchronized (activityStack) {
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    killActivity(activity);
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        synchronized (activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    ActivityCompat.finishAfterTransition(activityStack.get(i));
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivityWithRemoveTask() {
        if (activityStack == null) return;
        synchronized (activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                Activity activity = activityStack.get(i);
                if (null != activity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.finishAndRemoveTask();
                    } else {
                        activity.finish();
                    }
                }
            }
            activityStack.clear();
        }
    }

    public boolean isExists(Class<?> cls) {
        synchronized (activityStack) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            killAllActivity();
            System.exit(0);
        } catch (Exception e) {
            LogUtils.e("AppExit:" + e);
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExitAndRemoveTask(Context context) {
        try {
            killAllActivityWithRemoveTask();
            // apk模式：杀掉自己
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityMgr != null) {
                activityMgr.killBackgroundProcesses(context.getPackageName());
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            LogUtils.e("AppExit:" + e);
        }
    }

    public String getCurrentProcessName() {
        FileInputStream in = null;
        try {
            String fn = "/proc/self/cmdline";
            in = new FileInputStream(fn);
            byte[] buffer = new byte[256];
            int len = 0;
            int b;
            while ((b = in.read()) > 0 && len < buffer.length) {
                buffer[len++] = (byte) b;
            }
            if (len > 0) {
                String s = new String(buffer, 0, len, "UTF-8");
                return s;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
