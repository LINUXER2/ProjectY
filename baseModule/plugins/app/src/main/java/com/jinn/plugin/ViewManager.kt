package com.jinn.plugin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View

class ViewManager : PluginInterface {
    companion object {
        val TAG = "ViewManager"
    }

    override fun getView(context: Context): View {
        val resource = context.resources
        Log.d(TAG, "getView invoked")
        val resId: Int = resource.getIdentifier("plugin_layout", "layout", "com.jinn.plugin")
        val view = LayoutInflater.from(context).inflate(resId, null)
        Log.d(TAG, "getView,resId:${resId}")
        return view
    }

    override fun onVisible() {
        Log.d(TAG, "onVisible")
    }

    override fun onHide() {
        Log.d(TAG, "onHide")
    }

}
