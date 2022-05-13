package com.sidney.framelib.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hjq.toast.ToastUtils;
import com.sidney.framelib.mvvm.BaseVM;
import com.sidney.framelib.mvvm.IBaseView;
import com.sidney.framelib.mvvm.VmCommand;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 描 述：基于mvvm的封装
 * 作 者：hxl  2022/1/14 10:14
 * 修改描述：
 * 修 改 人：xxx  2022/1/14 10:14
 * 修改版本：
 */
public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseVM>
        extends Fragment implements IBaseView {

    protected VB binding;
    protected VM viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        // 绑定生命周期对象
        binding.setLifecycleOwner(this);
        // 创建ViewModel
        viewModel = buildVM();
        viewModel.mLiveData.observe(getViewLifecycleOwner(), new Observer<VmCommand>() {
            @Override
            public void onChanged(VmCommand vmCommand) {
                getVmCommand(vmCommand);
            }
        });
        // 初始化数据
        init(savedInstanceState);
        return binding.getRoot();
    }

    /**
     * 获取livedata发送后的数据
     *
     * @param vmCommand
     */
    protected abstract void getVmCommand(VmCommand vmCommand);

    /**
     * 返回布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 构建ViewModel
     *
     * @return
     */
    protected VM buildVM() {
        return createVM(getVMClass());
    }

    /**
     * 初始化方法
     *
     * @param savedInstanceState
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 启动service
     *
     * @param intent
     */
    public void newStartService(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getViewActivity().startForegroundService(intent);
        } else {
            getViewActivity().startService(intent);
        }
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }


    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void showToast(int resid) {
        ToastUtils.show(resid);
    }

    /**
     * 获取VM Class
     *
     * @return
     */
    public Class<VM> getVMClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type realType = type.getActualTypeArguments()[1];
        return (Class<VM>) realType;
    }

    /**
     * 创建ViewModel
     *
     * @param clz
     * @return
     */
    public VM createVM(Class<VM> clz) {
        Application application;
        if (getActivity() != null) {
            application = getActivity().getApplication();
        } else {
            throw new RuntimeException("" + clz.getName());
        }
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(clz);
    }

    /**
     * 改变Fragment
     *
     * @param fragment
     * @param layoutId
     */
    public void addFragment(Fragment fragment, @IdRes int layoutId) {
        if (fragment == null) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(layoutId, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment == null || !fragment.isAdded()) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 改变Fragment
     *
     * @param fragment
     * @param layoutId
     */
    public void replaceFragment(Fragment fragment, @IdRes int layoutId) {
        if (fragment == null) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.replace(layoutId, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏Fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (fragment == null || !fragment.isAdded()) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 移除fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment == null || !fragment.isAdded()) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != binding) {
            binding.unbind();
        }
    }
}
