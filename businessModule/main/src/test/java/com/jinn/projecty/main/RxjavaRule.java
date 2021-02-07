package com.jinn.projecty.main;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jinnlee on 2021/2/4.
 */
public class RxjavaRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
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
                base.evaluate();
            }
        };
    }
}
