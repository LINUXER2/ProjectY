package com.jinn.projecty.settings.api

import com.jinn.projecty.network.RetrofitManager
import com.jinn.projecty.network.BaseRepository
import com.jinn.projecty.network.BaseResponse
import com.jinn.projecty.settings.model.VideoBeanItem

class SettingRepo : BaseRepository() {
    private val api: SettingApi by lazy {
        RetrofitManager.getInstance().createService(
            SettingApi::class.java,
            BASE_URL
        )
    }

    companion object {
        private const val BASE_URL = "http://172.19.39.188:6789"
    }

    suspend fun getDataFromServer(): BaseResponse<MutableList<VideoBeanItem>> {
        return requestResponse { api.getDataFromServer() }
    }
}