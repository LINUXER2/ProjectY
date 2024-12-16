package com.jinn.projecty.network

/**
 *  数据包装类
 */
data class BaseResponse<out T>(
    val data: T? ,
    val code: Int = 0,//服务器状态码 200表示请求成功
    val msg: String = ""//错误信息
) {

    fun isSucceed(): Boolean {
        return code == 200
    }

    override fun toString(): String {
        return "code:$code,msg:$msg,data:$data"
    }
}
