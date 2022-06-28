package com.jinn.projecty.api;

import android.database.Cursor;
import android.os.Handler;

import com.jinn.projecty.utils.LogUtils;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * https://juejin.cn/post/7043399520486424612
 * https://juejin.cn/post/7034178205728636941
 *  饿了么开源的轻量级AOP框架，编译时插桩，支持在不懂字节码的情况下，也能够完成对对应方法字节码的修改
 *  注意1：只能在app module使用，如果放在子module中会报错  // TODO
 *    2：文章所述依赖改为com.bytedance.tools.lancet:lancet-plugin-asm6:1.0.0
 */
public class LancetHooker {

    /**
     * 作用是整体捕获TestQueryData异常，并增加一行log打印
     * TargetClass 注解：标识要修改的类名；
     * Insert注解：表示要往 TestQueryData 这个方法里面注入下面的代码
     * 方法声明需要和原方法保持一致，如果有参数，参数也要保持一致（方法名、参数名不需要一致）
     */
    @Insert("TestQueryData")
    @TargetClass("com.jinn.projecty.MainActivity")
    private void TestQueryData(){
        try {
            LogUtils.d("jinn2","TestQueryData");
            Origin.callVoid();
        }catch (Exception e){

        }
    }
}
