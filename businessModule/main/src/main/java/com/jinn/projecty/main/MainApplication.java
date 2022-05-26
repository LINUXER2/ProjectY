package com.jinn.projecty.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Messenger;
import android.os.PersistableBundle;

import com.jinn.projecty.frameapi.base.BaseApplication;
import com.jinn.projecty.frameapi.interfaces.IApplicationInterface;
import com.jinn.projecty.main.manager.WebViewPreloadHelper;
import com.jinn.projecty.main.message.MyHandler;
import com.jinn.projecty.main.service.JobScheduleService;
import com.jinn.projecty.main.ui.video.ExoMediaPlayer;
import com.jinn.projecty.utils.LogUtils;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;

/**
 * Created by jinnlee on 2021/2/24.
 */
public class MainApplication implements IApplicationInterface {
    private final String TAG = "MainApplication";
    private Context mContext;
    private int mJobId = 0;
    public MainApplication() {
        LogUtils.d(TAG,"MainApplication constructor1");
    }

    public MainApplication(Context context){
        mContext = context;
        LogUtils.d(TAG,"MainApplication constructor2");
    }

    @Override
    public void onCreate() {
        LogUtils.d(TAG,"MainApplication onCreate");
        startJobService();
        initPlayerBase();
        WebViewPreloadHelper.INSTANCE.prepareWebView();
    }

    private void initPlayerBase(){
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        int defaultPlanId = 1;
        PlayerConfig.addDecoderPlan(new DecoderPlan(defaultPlanId, ExoMediaPlayer.class.getName(),"ExoPlayer"));
        PlayerConfig.setDefaultPlanId(defaultPlanId);
        PlayerLibrary.init(BaseApplication.sInstance);
    }

    private void startJobService() {
        Intent intentService = new Intent(mContext, JobScheduleService.class);
        Messenger messenger = new Messenger(new MyHandler());
        intentService.putExtra("message_init", messenger);
        mContext.startService(intentService);

        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, new ComponentName(mContext, JobScheduleService.class));
       // builder.setMinimumLatency(3 * 1000);  //设置任务的延迟执行时间(单位是毫秒)
        builder.setOverrideDeadline(10 * 1000); //设置任务最晚的延迟时间。如果到了规定的时间时其他条件还未满足，任务也会被启动。
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // 这个任务只有在满足指定的网络条件时才会被执行
        builder.setRequiresCharging(true); // 充电时才会执行
       // builder.setPeriodic(5000);
        PersistableBundle extras =  new PersistableBundle();
        extras.putLong("work_duration_key",2*1000);
        builder.setExtras(extras);  // 传递其它参数
        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

    }

    private void stopJobService(){

    }

    @Override
    public void onLowMemory() {
        LogUtils.d(TAG,"MainApplication onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        LogUtils.d(TAG,"MainApplication onTrimMemory:"+level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.d(TAG,"MainApplication onConfigurationChanged");
    }

    @Override
    public void onTerminate() {
        LogUtils.d(TAG,"MainApplication onTerminate");
    }
}
