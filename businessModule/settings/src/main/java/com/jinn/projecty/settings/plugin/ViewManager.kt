package com.jinn.projecty.settings.plugin

import android.content.Context
import android.view.LayoutInflater
import android.view.View

class ViewManager : PluginInterface {
    override fun getView(context: Context): View {
        val resource = context.resources
        val resId: Int = resource.getIdentifier("plugin_layout", "layout", "com.jinn.projextx")
        val view = LayoutInflater.from(context).inflate(resId, null)
        return view
    }

    override fun onVisible() {

    }

    override fun onHide() {

    }

}
