package com.sidney.tools;

import android.app.AlarmManager;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/18 17:06
 * desc:DataFormatUtil 时间相关的处理
 * version:1.0
 */
public class TimeUtils {

    /**
     * 一些时间格式
     */
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SECOND_NOSPILE = "yyyyMMddHHmmss";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_TIME_NOSPILE = "yyyyMMddHHmm";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_TIME_NOSPILE = "HHmm";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_DATE_NOSPILE = "yyyyMMdd";
    public final static String FORMAT_DATE_YEAR = "yy-MM-dd";
    public final static String FORMAT_DATE_YEAR_NOSPILE = "yyMMdd";

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        try {
            return System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取当前日期和时间
     *
     * @param formatType
     * @return
     */
    public static String getCurrentDate(String formatType) {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(formatType, Locale.CHINA);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 获取当前日期和时间
     *
     * @return
     */
    public static String getCurrentDate() {
        try {
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE_TIME_SECOND, Locale.CHINA);
            return formatter.format(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 格式化当前时间 时间转时间戳
     *
     * @param dateStr    时间戳字符串
     * @param dateFormat 时间格式
     * @return
     */
    public static long dateToStamp(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.CHINA);
            return formatter.parse(dateStr).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return 0;
        }
    }

    /**
     * 格式化当前时间 时间戳转时间
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return
     */
    public static String stampToDate(long date, String dateFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.CHINA);
            return formatter.format(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 类似QQ/微信 聊天消息的时间
     *
     * @param timesamp 时间
     * @return
     */
    public static String getChatTime(long timesamp) {
        try {

            String result;
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.CHINA);
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(timesamp);
            int temp = Integer.parseInt(sdf.format(today))
                    - Integer.parseInt(sdf.format(otherDay));
            switch (temp) {
                case 0:
                    result = "今天 " + stampToDate(timesamp, FORMAT_TIME);
                    break;
                case 1:
                    result = "昨天 " + stampToDate(timesamp, FORMAT_TIME);
                    break;
                case 2:
                    result = "前天 " + stampToDate(timesamp, FORMAT_TIME);
                    break;
                default:
                    result = stampToDate(timesamp, FORMAT_DATE_TIME);
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        try {

            String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            Calendar cal = Calendar.getInstance(Locale.CHINA);
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }

    }

    /**
     * 描述：计算两个日期相差多少
     *
     * @param str1 第一个时间 时间格式yyyy-MM-dd HH:mm:ss
     * @param str2 第二个时间 时间格式yyyy-MM-dd HH:mm:ss
     * @param type 1相差天数，2相差小时数，3分钟数,4秒数
     * @return int 所差的值
     */
    public static long getOffsetTime(String str1, String str2, int type) {
        LogUtils.d("第一个时间===" + str1);
        LogUtils.d("第二个时间===" + str2);
        LogUtils.d(String.valueOf(type));
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_SECOND, Locale.CHINA);
        Date one;
        Date two;
        long day;//天数差
        long hour;//小时数差
        long min;//分钟数差
        long second = 0;//秒数差
        try {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            one = df.parse(str1);
            c.setTime(one);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            diff = time2 - time1;
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            second = diff / 1000;
            LogUtils.d("day=" + day);
            LogUtils.d("hour=" + hour);
            LogUtils.d("min=" + min);
            LogUtils.d("second=" + second);
            if (1 == type) {
                return day;
            } else if (2 == type) {
                return hour;
            } else if (3 == type) {
                return min;
            } else if (4 == type) {
                return second;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
        }
        return second;
    }

    /**
     * 时间比较 要求的时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 0，异常信息   1，时间相等    2，time1小于time2    3，time1大于time2
     */
    public static int compareDate(String time1, String time2) {
        try {
            LogUtils.d(time1);
            LogUtils.d(time2);

            DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_SECOND, Locale.CHINA);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
            int result = c1.compareTo(c2);
            if (result == 0) {
                return 1;
            } else if (result < 0) {
                return 2;
            } else {
                return 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("格式不正确");
            return 0;
        }
    }

    /**
     * 时间加减  要求的时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param amount -1昨天的时间 -2前天的时间 1后一天时间
     * @return
     */
    public static String timeAddSubtract(int amount) {
        try {

            SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_SECOND, Locale.CHINA);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, amount);
            Date time = cal.getTime();
            return df.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }


    /**
     * 时间加减
     * dateFormat 时间格式
     *
     * @param amount -1昨天的时间 -2前天的时间
     * @return
     */
    public static String timeAddSubtract(String dateFormat, int amount) {
        try {

            SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.CHINA);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, amount);
            Date time = cal.getTime();
            return df.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 得到某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        try {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
            return new SimpleDateFormat(FORMAT_DATE, Locale.CHINA).format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 得到某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        try {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, value);
            return new SimpleDateFormat(FORMAT_DATE, Locale.CHINA).format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return "";
        }
    }

    /**
     * 当前时间在不在开始时间和结束时间之内
     *
     * @param startDate 开始时间 （例：2020-02-28 10:30:21）
     * @param endDate   结束时间 （例：2020-03-31 10:30:21）
     * @param value     当前日期  （例：2020-03-01 10:30:21）
     * @return true 在时间段之内， false 不在时间段之内
     */
    public static boolean between(String startDate, String endDate, String value) {
        try {
            LogUtils.d(startDate);
            LogUtils.d(endDate);
            LogUtils.d(value);

            DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME, Locale.CHINA);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c1.setTime(df.parse(startDate));
            c2.setTime(df.parse(endDate));
            c.setTime(df.parse(value));
            if (c1.compareTo(c2) > 0) {
                LogUtils.d("开始时间必须比结束时间小");
                return false;
            }
            return c.compareTo(c1) > 0 && c.compareTo(c2) < 0;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
            return false;
        }
    }

    /**
     * 设置当前时区为东八区
     *
     * @param cxt
     */
    public static void setTimeZone(Context cxt) {
        try {
            AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setTimeZone("Asia/Shanghai");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e.getMessage());
        }
    }

}
