package com.jinn.projecty.settings.api

import com.jinn.projecty.network.BaseResponse
import com.jinn.projecty.settings.model.VideoBeanItem
import retrofit2.http.GET
import io.reactivex.Observable


interface SettingApi {
    @GET("/list")
    suspend fun getDataFromServer(): BaseResponse<MutableList<VideoBeanItem>>

    @GET("/list")
    fun getDataFromServer2(): Observable<VideoBeanItem>
}