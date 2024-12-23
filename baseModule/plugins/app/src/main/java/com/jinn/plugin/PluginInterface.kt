package com.jinn.plugin

import android.content.Context
import android.view.View

interface PluginInterface {
    fun getView(context:Context): View

    fun onVisible()

    fun onHide()

}
