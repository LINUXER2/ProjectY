package com.jinn.projecty;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jinn.projecty.databases.MyAsyncQueryHandler;
import com.jinn.projecty.databases.MyContentProvider;
import com.jinn.projecty.databinding.HomeActivityBinding;
import com.jinn.projecty.main.ui.MainFragment;
import com.jinn.projecty.settings.SettingFragment;
import com.jinn.projecty.utils.HeavyWorkThread;
import com.jinn.projecty.utils.LogUtils;
import com.jinn.projecty.video.VideoFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
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
    private HomeActivityBinding mHomeActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //不生效，因为继承的是AppCompatActivity
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mHomeActivityBinding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(mHomeActivityBinding.getRoot());
        getSupportActionBar().hide();
        navigationView = mHomeActivityBinding.bottomNav;
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
        ReuseFragmentNavigator fragmentNavigator =new ReuseFragmentNavigator(this,getSupportFragmentManager(),navHostFragment.getId());
        mNavControl.getNavigatorProvider().addNavigator(fragmentNavigator);
        mNavControl.setGraph(initNavGraph(mNavControl.getNavigatorProvider(),fragmentNavigator));
        NavigationUI.setupWithNavController(navigationView,mNavControl);  //将navigation 与 control绑定
    }

    /**
     *  自定义导航器
     * @param provider
     * @param fragmentNavigator
     * @return
     */
    private NavGraph initNavGraph(NavigatorProvider provider, ReuseFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //用自定义的导航器来创建目的地
        FragmentNavigator.Destination destination1 = fragmentNavigator.createDestination();
        destination1.setId(R.id.nav_main);
        destination1.setClassName(MainFragment.class.getCanonicalName());
        navGraph.addDestination(destination1);


        FragmentNavigator.Destination destination2 = fragmentNavigator.createDestination();
        destination2.setId(R.id.nav_video);
        destination2.setClassName(VideoFragment.class.getCanonicalName());
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = fragmentNavigator.createDestination();
        destination3.setId(R.id.nav_mine);
        destination3.setClassName(SettingFragment.class.getCanonicalName());
        navGraph.addDestination(destination3);

        navGraph.setStartDestination(destination1.getId());

        return navGraph;
    }


    @Override
    protected void onResume() {
        super.onResume();
        TestQueryData();
    }

    private void TestQueryData(){
        HeavyWorkThread.getHandler().post(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(MyContentProvider.USER,new String[]{MyContentProvider.DB_COLUMN_USER_AGE,MyContentProvider.DB_COLUMN_USER_NAME},null,null,MyContentProvider.DB_COLUMN_USER_AGE+" DESC");
                if(cursor!=null && cursor.getCount()>0){
                    int nameIndex = cursor.getColumnIndexOrThrow(MyContentProvider.DB_COLUMN_USER_NAME);
                    while (cursor.moveToNext()){
                        String name = cursor.getString(nameIndex);
                        LogUtils.d(TAG,"query user:"+name);
                    }
                }
            }
        });

        //AsyncQueryHandler是一个异步的查询操作帮助类，可以处理增删改ContentProvider提供的数据并在主线程回调查询结果
        MyAsyncQueryHandler queryHandler = new MyAsyncQueryHandler(getContentResolver());
        queryHandler.startQuery(0,null,MyContentProvider.USER,new String[]{MyContentProvider.DB_COLUMN_USER_AGE,MyContentProvider.DB_COLUMN_USER_NAME},null,null,MyContentProvider.DB_COLUMN_USER_AGE+" DESC");
    }

}
