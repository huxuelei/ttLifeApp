package com.sidney.framelib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.sidney.framelib.R;
import com.sidney.framelib.mvvm.BaseVM;
import com.sidney.framelib.mvvm.IBaseView;
import com.sidney.framelib.mvvm.VmCommand;
import com.sidney.tools.ActivityManageUtils;
import com.sidney.tools.KeyboardUtils;
import com.sidney.ui.dialog.LoadingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 描 述：基于mvvm的封装
 * 作 者：hxl  2022/1/14 10:14
 * 修改描述：
 * 修 改 人：xxx  2022/1/14 10:14
 * 修改版本：
 */
public abstract class BaseActivity<VB extends ViewDataBinding, VM extends BaseVM>
        extends AppCompatActivity implements IBaseView {

    protected VB binding;
    protected VM viewModel;

    protected Context mContext;
    protected Activity mActivity;

    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        ActivityManageUtils.getInstance().addActivity(this);
        if (getLayoutId() > 0) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            // 点击外部隐藏软键盘，提升用户体验
            getContentView().setOnClickListener(v -> {
                // 隐藏软键，避免内存泄漏
                KeyboardUtils.hideSoft(getCurrentFocus());
            });
        }
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();
            // 设置标题栏沉浸
            if (getTitleBar() != null) {
                ImmersionBar.setTitleBar(this, getTitleBar());
            }
        }
        // 绑定生命周期对象
        binding.setLifecycleOwner(this);
        // 创建ViewModel
        viewModel = this.buildVM();
        viewModel.mLiveData.observe(this, new Observer<VmCommand>() {
            @Override
            public void onChanged(VmCommand vmCommand) {
                getVmCommand(vmCommand);
            }
        });
        // 初始化数据
        init(savedInstanceState);
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity);
        // 隐藏软键，避免内存泄漏
        KeyboardUtils.hideSoft(getCurrentFocus());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity);
        // 隐藏软键，避免内存泄漏
        KeyboardUtils.hideSoft(getCurrentFocus());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (null != binding) {
            binding.unbind();
        }
        if (viewModel != null) {
            viewModel.release();
        }
        if (ActivityManageUtils.getInstance().getCount() == 0) {
            AppExit();
        } else {
            //activity管理
            ActivityManageUtils.getInstance().killActivity(this);
        }
        super.onDestroy();
    }

    protected void AppExit() {
        ActivityManageUtils.getInstance().AppExit(getApplicationContext());
    }

    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
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
        Class<VM> vmClass = getVMClass();
        return createVM(vmClass);
    }

    /**
     * 初始化方法
     *
     * @param savedInstanceState
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    public Context getViewContext() {
        return mContext;
    }

    @Override
    public Activity getViewActivity() {
        return mActivity;
    }

    public abstract TitleBar getTitleBar();

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.green)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }


    @Override
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void showToast(int resid) {
        ToastUtils.show(resid);
    }

    private LoadingDialog mDialog;

    /**
     * 数据加载弹框
     */
    protected void showLoadingDialog() {
        showLoadingDialog("正在加载...", false, false);
    }

    /**
     * 数据加载弹框
     *
     * @param msg
     */
    protected void showLoadingDialog(String msg) {
        showLoadingDialog(msg, false, true);
    }

    /**
     * 数据加载弹框
     *
     * @param msg
     * @param isCancelable    设置是否可以按返回键取消
     * @param isCancelOutside 设置点击外部是否取消
     */
    protected void showLoadingDialog(String msg, boolean isCancelable, boolean isCancelOutside) {
        mDialog = new LoadingDialog.Builder(mActivity)
                .setCancelable(isCancelable)
                .setCancelOutside(isCancelOutside)
                .setMsg(msg)
                .create();
        mDialog.show();
    }

    /**
     * 数据加载弹框 消失
     */
    protected void dismissLoadingDialog() {
        if (null != mDialog) {
            mDialog.dismiss();
        }
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) { // 如果添加了，显示
            ft.show(fragment);
        } else {
            ft.add(layoutId, fragment);
        }
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
        FragmentManager fm = getSupportFragmentManager();
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 启动service
     *
     * @param intent
     */
    public void newStartService(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
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
     * 创建VM
     *
     * @param clz
     * @return
     */
    public VM createVM(Class<VM> clz) {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(clz);
    }

}
