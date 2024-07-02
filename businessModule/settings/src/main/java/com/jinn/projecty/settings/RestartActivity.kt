package com.jinn.projecty.settings

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.jinn.projecty.utils.LogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * 双进程交互实现app自动重启
 * https://juejin.cn/post/7350126677572304907
 */
class RestartActivity : FragmentActivity() {
    companion object {
        private const val EXTRA_MAIN_PID = "extra_main_pid"

        fun launch(activity: Activity) {
            activity.startActivity(Intent(activity, RestartActivity::class.java).apply {
                putExtra(EXTRA_MAIN_PID, android.os.Process.myPid())
            })
            activity.finish()
            GlobalScope.launch {
                delay(500)
                killProcess()
            }
        }

        private fun killProcess(pid: Int = android.os.Process.myPid()) {
            android.os.Process.killProcess(pid)
            exitProcess(0)
        }

        fun isMainProcessAlive(context: Context): Boolean = runCatching {
            (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses.find {
                it.processName == context.packageName
            } != null
        }.getOrDefault(false)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            doubleCheckMainProcess()
            val intent = Intent(Intent.ACTION_VIEW)
            val comp = ComponentName("com.jinn.projecty", "com.jinn.projecty.MainActivity")
            startActivity(intent.apply {
                component = comp
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
            killProcess()
        }

    }

    private suspend fun doubleCheckMainProcess() {
        delay(1500)
        if (isMainProcessAlive(this)) {
            val mainPid = intent.getIntExtra(EXTRA_MAIN_PID, 0)
            if (mainPid != 0) {
                killProcess(mainPid)
                delay(1500)
            }
        }
    }
}