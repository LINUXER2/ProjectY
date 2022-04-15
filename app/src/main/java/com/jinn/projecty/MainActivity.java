package com.jinn.projecty;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

/** bottomNavigation 与  FragmentContainerView 实现的底部Tab
 * https://blog.csdn.net/weixin_44902943/article/details/115618012
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private NavController mNavControl;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                LogUtils.d(TAG,"onNavigationItemSelected,"+item.getItemId());
                mNavControl.navigate(item.getItemId());
                return false;
            }
        });
        NavHostFragment  navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_container);
        mNavControl = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(navigationView,mNavControl);  //将navigation 与 control绑定

    }

}
