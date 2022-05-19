package com.jinn.projecty;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;


/**
 * 可以重复使用的FragmentNavigator
 * FragmentNavigator默认采用replace的方式切换导航栏，这样会导致fragment没出都会重新创建，这里将replace改为hide和show
 * 注意，使用自定义导航时需要将xml中设置的默认导航去掉
 * 参考：https://blog.csdn.net/weixin_42575043/article/details/108709467
 */
@Navigator.Name("reuseFragmentNavigator")
public class ReuseFragmentNavigator extends FragmentNavigator {

    private Context context;
    private FragmentManager fragmentManager;
    private int containerId;

    public ReuseFragmentNavigator(@NonNull Context context, @NonNull FragmentManager fragmentManager, int containerId) {
        super(context, fragmentManager, containerId);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // 获取当前显示的Fragment
        Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
        if (fragment != null) {
            ft.hide(fragment);
        }else{
        }

        final String tag = String.valueOf(destination.getId());
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            ft.show(fragment);
        } else {
            fragment = instantiateFragment(context, fragmentManager, destination.getClassName(), args);
            ft.add(containerId, fragment, tag);
        }
        ft.setPrimaryNavigationFragment(fragment);
        ft.setReorderingAllowed(true);
        ft.commit();
        return destination;
    }
}

