package com.sidney.framelib.mvvm;

import android.content.Intent;

/**
 * 描 述： VM = ViewModel   基 ViewModel
 * 作 者：hxl  2022/1/14 9:49
 * 修改描述：
 * 修 改 人：xxx  2022/1/14 9:49
 * 修改版本：
 */
public interface IBaseVM {

    /**
     * 初始化 --> onCreate
     */
    void init();

    /**
     * 释放 --> onDestroy
     */
    void release();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

}
