package com.sidney.tools;

import android.content.Context;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;

import com.tencent.mmkv.MMKV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * 描 述：使用mmkv替换sp  数据缓存
 * 作 者：hxl  2022/1/4 15:26
 * 修改描述：
 * 修 改 人：xxx  2022/1/4 15:26
 * 修改版本：
 */
public class SPUtils {

    private static String PREFERENCE_NAME = "com_sp_data_preferenc";
    private static String USER_NAME = "SP_DATA";

    private static MMKV mmkv;

    public static void init(Context context) {
        init(context, "", "");
    }

    public static void init(Context context, String userName) {
        init(context, "", userName, MMKV.MULTI_PROCESS_MODE);
    }

    public static void init(Context context, String preferenceName, String userName) {
        init(context, preferenceName, userName, MMKV.MULTI_PROCESS_MODE);
    }

    /**
     * @param context
     * @param mode    传Context.MODE_MULTI_PROCESS表示进程间访问
     * @return
     */
    public static void init(Context context, String preferenceName, String userName, int mode) {
        // 创建自定义路径
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + PREFERENCE_NAME;
        LogUtils.e("mmkv数据缓存路径--------------" + dir);
        String rootDir;
        if (TextUtils.isEmpty(preferenceName)) {
            rootDir = MMKV.initialize(dir);
        } else {
            rootDir = MMKV.initialize(context);
        }
        LogUtils.e("数据缓存路径--------------" + rootDir);
        LogUtils.e("用户名--------------" + userName);
        if (!TextUtils.isEmpty(userName)) {
            mmkv = MMKV.mmkvWithID(userName, mode);
        } else {
            mmkv = MMKV.mmkvWithID(USER_NAME, mode);
        }
    }

    /**
     * 清除缓存
     */
    public static void clear() {
        try {
            LogUtils.d("clear");
            if (null != mmkv) {
                mmkv.clearAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }


    /**
     * 设置String类型值
     *
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        try {
            LogUtils.d("key:  value:" + key + "  " + value);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.encode(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 设置long类型值
     *
     * @param key
     * @param value
     */
    public static void put(String key, long value) {
        try {
            LogUtils.d("key:  value:" + key + "  " + value);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.encode(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 设置int类型值
     *
     * @param key
     * @param value
     */
    public static void put(String key, int value) {
        try {
            LogUtils.d("key:  value:" + key + "  " + value);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.putInt(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 设置Boolean类型值
     *
     * @param key
     * @param value
     */
    public static void put(String key, boolean value) {
        try {
            LogUtils.d("key:  value:" + key + "  " + value);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.encode(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 设置Float类型值
     *
     * @param key
     * @param value
     */
    public static void put(String key, float value) {
        try {
            LogUtils.d("key:  value:" + key + "  " + value);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.encode(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 将serializable对象储存到sharepreference
     *
     * @param key
     * @param <T>
     */
    public static <T extends Serializable> void put(String key, T serializable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(serializable);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            put(key, oAuth_Base64);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                oos.close();
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 将Parcelable对象储存
     *
     * @param key
     * @param <T>
     */
    public static <T extends Parcelable> void put(String key, T parcelable) {
        try {
            LogUtils.d("key:  value:" + key + "  " + parcelable);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.encode(key, parcelable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    public static Map<String, ?> getAll() {
        try {
            if (null != mmkv) {
                return mmkv.getAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return null;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getString(key, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return "";
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(String key, String defaultValue) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getString(key, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为false
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean get(String key, boolean defaultValue) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getBoolean(key, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @return
     */
    public static int get(String key, int defaultValue) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getInt(key, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static long get(String key, Long defaultValue) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getLong(key, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static float get(String key, Float defaultValue) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.getFloat(key, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return defaultValue;
    }

    public static <T extends Serializable> T get(String key, T defaultValue) {
        T serializable = defaultValue;
        String base64Str = get(key, "");
        if (base64Str == null) {
            return defaultValue;
        }
        // 读取字节
        byte[] base64 = Base64.decode(base64Str.getBytes(), Base64.DEFAULT);
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            bis = new ObjectInputStream(bais);
            serializable = (T) bis.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                bais.close();
                bis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                bais.close();
                bis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return serializable;
    }

    public static <T extends Parcelable> T get(String key, T tClass) {
        if (null != mmkv && !TextUtils.isEmpty(key)) {
            return mmkv.decodeParcelable(key, null);
        }
        return tClass;
    }

    /**
     * 判断是否存在此字段
     */
    public static boolean contains(String key) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                return mmkv.contains(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return false;
    }

    /**
     * 删除sharedPreferences文件中对应的Key和value
     */
    public static boolean remove(String key) {
        try {
            LogUtils.d("key:  " + key);
            if (null != mmkv && !TextUtils.isEmpty(key)) {
                mmkv.removeValueForKey(key);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return false;
    }

}
