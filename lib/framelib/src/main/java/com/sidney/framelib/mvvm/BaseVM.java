package com.sidney.framelib.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * 描 述：Base ViewModel
 * 作 者：hxl  2022/1/14 9:50
 * 修改描述：
 * 修 改 人：xxx  2022/1/14 9:50
 * 修改版本：
 */
public abstract class BaseVM<T extends VmCommand> extends AndroidViewModel implements IBaseVM {

    public MutableLiveData<T> mLiveData = new MutableLiveData<>();

    public BaseVM(@NonNull Application application) {
        super(application);
        this.init();
    }

    // 执行命令
    public void runCommand(T cmd) {
        mLiveData.postValue(cmd);
    }

}
