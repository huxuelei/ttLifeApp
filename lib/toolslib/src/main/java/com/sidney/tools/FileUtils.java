package com.sidney.tools;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * author:hxl
 * e-mail:huxl@bjhzwq.com
 * time:2020/3/25 10:10
 * desc:文件相关工具 创建，读写文件，复制，删除（单个或多个）文件是否存在，获得文件大小（格式化）
 * 获取文件名及后缀，文件重命名，获取某个目录下的文件列表，文件的解压缩等
 * version:1.0
 */
public class FileUtils {

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static boolean creatFolder(String path) {
        try {
            File file = new File(path);
            return file.exists() || file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }


    /**
     * 创建文件
     *
     * @param path
     */
    public static boolean creatFile(String path) {
        try {
            File file = new File(path);
            return file.exists() || file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 写/存储文本数据  往/data/data/<package name>/files目录下写入文本
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param content  文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(Context context, String fileName, String content) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                LogUtils.e(ioe.getMessage());
            }
        }
        return success;
    }

    /**
     * 写/存储文本数据 任意路径下写入文本
     *
     * @param filePath 文件路径
     * @param content  写入的内容
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                LogUtils.e(ioe.getMessage());
            }
        }
        return success;
    }

    /**
     * 复制文件
     *
     * @param srcFile 源文件
     * @param dstFile 目标文件
     * @return
     */
    public static boolean copyFile(String srcFile, String dstFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File dst = new File(dstFile);
            if (!dst.getParentFile().exists()) {
                dst.getParentFile().mkdirs();
            }
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e(e.getMessage());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e(e.getMessage());
                }
            }
        }
        return true;
    }

    /**
     * 删除单个文件
     *
     * @param path 文件所在路径名
     * @return 删除成功则返回true
     */
    public static boolean deleteFile(String path) {
        try {
            LogUtils.e(path);
            File file = new File(path);
            if (file.exists()) {
                boolean isDeleted = file.delete();
                return isDeleted;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除文件 删除一个文件夹下的所有文件
     *
     * @param path 文件所在路径名
     **/
    public static void deleteFolder(String path) {
        try {
            File[] files = (new File(path)).listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    files[i].delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean ifExist(String path) {
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 文件是否存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean ifExist(Context context, String fileName) {
        try {
            return new File(context.getFilesDir(), fileName).exists();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 获取指定文件大小
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        long size = 0;
        try {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fis;
                fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        return size;
    }


    /**
     * 转换文件大小
     *
     * @param fileS 文件大小
     * @return
     */
    public static String formetFileSize(long fileS) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            String fileSizeString;
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            }
            return fileSizeString;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return "";
        }
    }

    /**
     * 获取文件名及后缀
     *
     * @param path
     */
    public static String getFileNameWithSuffix(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return "";
            }
            int start = path.lastIndexOf("/");
            if (start != -1) {
                return path.substring(start + 1);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return "";
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath 旧文件的绝对路径
     * @param newPath 新文件的绝对路径
     * @return 文件重命名成功则返回true
     */
    public static boolean renameTo(String oldPath, String newPath) {
        try {
            if (oldPath.equals(newPath)) {
                LogUtils.w("文件重命名失败：新旧文件名绝对路径相同！");
                return false;
            }
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            boolean isSuccess = oldFile.renameTo(newFile);
            LogUtils.w("文件重命名是否成功：" + isSuccess);
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFile 旧文件对象
     * @param newFile 新文件对象
     * @return 文件重命名成功则返回true
     */
    public static boolean renameTo(File oldFile, File newFile) {
        try {
            if (oldFile.equals(newFile)) {
                LogUtils.w("文件重命名失败：旧文件对象和新文件对象相同！");
                return false;
            }
            boolean isSuccess = oldFile.renameTo(newFile);
            LogUtils.w("文件重命名是否成功：" + isSuccess);
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFile 旧文件对象，File类型
     * @param newName 新文件的文件名，String类型
     * @return 重命名成功则返回true
     */
    public static boolean renameTo(File oldFile, String newName) {
        try {
            StringBuilder ss = new StringBuilder();
            ss.append(oldFile.getParentFile());
            ss.append(File.separator);
            ss.append(newName);
            LogUtils.e(ss.toString());
            File newFile = new File(ss.toString());
            boolean flag = oldFile.renameTo(newFile);
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取某个路径下的文件列表
     *
     * @param path 文件路径
     * @return 文件列表File[] files
     */
    public static File[] getFileList(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    return files;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return null;
        }
    }

    /**
     * 获取某个目录下的文件列表
     *
     * @param directory 目录
     * @return 文件列表File[] files
     */
    public static File[] getFileList(File directory) {
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                return files;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return null;
        }
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名称
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(Context context, String fileName) {
        if (!ifExist(context, fileName)) {
            return null;
        }
        FileInputStream fis = null;
        String content = null;
        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = arrayOutputStream.toString("utf-8");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            content = null;
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                LogUtils.e(ioe.getMessage());
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     *
     * @param filePath 文件路径
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(String filePath) {
        if (filePath == null || !new File(filePath).exists()) {
            return null;
        }
        FileInputStream fis = null;
        String content = null;
        try {
            fis = new FileInputStream(filePath);
            if (fis != null) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = arrayOutputStream.toString("utf-8");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            content = null;
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                LogUtils.e(ioe.getMessage());
            }
        }
        return content;
    }

    /**
     * 读取Assets文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名称
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = arrayOutputStream.toString("utf-8");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                LogUtils.e(ioe.getMessage());
            }
        }
        return content;
    }

    /**
     * 存储单个Parcelable对象到/data/data/<package name>/files目录下
     *
     * @param context      程序上下文
     * @param fileName     文件名，要在系统内保持唯一
     * @param parcelObject 对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            byte[] data = parcel.marshall();
            fos.write(data);
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    LogUtils.e(ioe.getMessage());
                }
            }
        }

        return success;
    }

    /**
     * 存储List对象到/data/data/<package name>/files目录下
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param list     对象数组集合，对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list) {
        boolean success = false;
        FileOutputStream fos = null;
        try {
            if (list instanceof List) {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                Parcel parcel = Parcel.obtain();
                parcel.writeList(list);
                byte[] data = parcel.marshall();
                fos.write(data);
                success = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    LogUtils.e(ioe.getMessage());
                }
            }
        }

        return success;
    }

    /**
     * 读取单个数据对象
     *
     * @param context  程序上下文
     * @param fileName 文件名称
     * @return Parcelable, 读取到的Parcelable对象，失败返回null
     */
    public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader) {
        Parcelable parcelable = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }
                byte[] data = bos.toByteArray();
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelable = parcel.readParcelable(classLoader);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            parcelable = null;
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
            if (bos != null) try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
        }
        return parcelable;
    }

    /**
     * 读取数据对象列表
     *
     * @param context  程序上下文
     * @param fileName 文件名称
     * @return List, 读取到的对象数组，失败返回null
     */
    public static List<Parcelable> readParcelableList(Context context, String fileName,
                                                      ClassLoader classLoader) {
        List<Parcelable> results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = context.openFileInput(fileName);
            if (fis != null) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }
                byte[] data = bos.toByteArray();
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classLoader);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            results = null;
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
            if (bos != null) try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
        }
        return results;
    }

    /**
     * 保存Serializable数据到/data/data/<package name>/files目录下
     *
     * @param context
     * @param fileName
     * @param data
     * @return
     */
    public static boolean saveSerializable(Context context, String fileName, Serializable data) {
        boolean success = false;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(data);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 读取/data/data/<package name>/files目录下SerialLizable序列化数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Serializable readSerialLizable(Context context, String fileName) {
        Serializable data = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 文件转化为字节
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
            bos.close();
        }
    }

    /**
     * 压缩文件
     *
     * @param srcPath    要压缩的文件路径
     * @param toPath     压缩后的路径
     * @param toFileName 压缩后的文件名
     */
    public static void compressFile(String srcPath, String toPath, String toFileName) {
        File file = new File(toPath + File.separator + toFileName + ".zip");
        File filePath = new File(toPath);
        if (!filePath.exists())
            filePath.mkdirs();
        if (file.exists())
            return;
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(file), new CRC32()));
            zip(zipOutputStream, toFileName, new File(srcPath));
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param zipOutputStream 压缩流
     * @param name            压缩后的文件名
     * @param fileSrc         压缩后的路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zipOutputStream, String name, File fileSrc) throws IOException {
        if (fileSrc.isDirectory()) {
            LogUtils.e("需要压缩的地址是目录");
            File[] files = fileSrc.listFiles();
            name = name + "/";
            zipOutputStream.putNextEntry(new ZipEntry(name));  // 建一个文件夹
            LogUtils.e("目录名: " + name);
            for (File f : files) {
                zip(zipOutputStream, name + f.getName(), f);
                LogUtils.e("目录: " + name + f.getName());
            }
        } else {
            LogUtils.e("需要压缩的地址是文件");
            zipOutputStream.putNextEntry(new ZipEntry(name));
            LogUtils.e("文件名: " + name);
            FileInputStream input = new FileInputStream(fileSrc);
            LogUtils.e("文件路径: " + fileSrc);
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.flush();
            input.close();
        }
    }

    /**
     * 解压文件
     *
     * @param filePath  要压缩的路径文件
     * @param unZipPath 压缩后的路径
     */
    public static void unZip(String filePath, String unZipPath) {
        File file = new File(filePath);
        try {
            upZipFile(file, unZipPath);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 解压缩
     * 将zipFile文件解压到folderPath目录下.
     *
     * @param zipFile    zip文件
     * @param folderPath 解压到的地址
     * @throws IOException
     */
    private static void upZipFile(File zipFile, String folderPath) throws IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                LogUtils.e("ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                LogUtils.e("str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            LogUtils.e("ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private static File getRealFileName(String baseDir, String absFileName) {
        LogUtils.e("baseDir==" + baseDir);
        LogUtils.e("absFileName==" + absFileName);
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr;
        if (dirs.length > 0) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    substr = new String(substr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ret = new File(ret, substr);

            }
            LogUtils.e("1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                LogUtils.e("substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ret = new File(ret, substr);
            LogUtils.e("2ret = " + ret);
            return ret;
        }
        return ret;
    }


}
