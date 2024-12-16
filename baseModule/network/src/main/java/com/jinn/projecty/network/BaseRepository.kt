package com.jinn.projecty.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseRepository {

    /**
     * IO中处理请求
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return try {
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    requestCall()
                }
            }
            response
        } catch (e: Exception) {
            BaseResponse(data = null, code = -1, msg = "网络连接异常")
        }
    }
}