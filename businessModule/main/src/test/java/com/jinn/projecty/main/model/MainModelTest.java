package com.jinn.projecty.main.model;

import com.jinn.projecty.main.RxjavaRule;
import com.jinn.projecty.main.api.RetrofitApi;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.network.RetrofitManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Created by jinnlee on 2021/2/4.
 */
public class MainModelTest {
    private final String BASE_URL = "https://baobab.kaiyanapp.com/api/";

    @Rule
    public RxjavaRule rule = new RxjavaRule();

    @Before
    public void setUp() throws Exception {
     //   initRxJava2();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 因为网络请求是异步的，所以我们直接测试是不能直接拿到数据，因此无法打印出Log以及测试。
     * 所以我们使用initRxJava2()方法将异步转化为同步。这样我们就可以看到返回信息
     * 试用方式：1：在每个需要测试的单元运行该方法
     *           2：使用rule注解
     */
    private void initRxJava2() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void requestMainData() {
        RetrofitManager.getInstance().createService(RetrofitApi.class,BASE_URL).getMainData("2")
                .subscribeOn(Schedulers.io())
              //  .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommandDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(RecommandDataBean recommandData) {
                        System.out.println("onNext:"+recommandData.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError:"+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    @Test
    public void requestRelateData() {
        assertEquals(2+2,4);
    }
}