package com.model.login.data

import com.google.gson.annotations.SerializedName
import com.yupfeg.remote.data.HttpResponseParsable
import java.io.Serializable

/**
 * 玩Android的首页文章列表返回实体类
 * @author yuPFeG
 * @date 2021/09/25
 */
data class LoginData(
    val data: DataEntity, override val code: Int, override val message: String
) : HttpResponseParsable, Serializable {
    data class DataEntity(
        @SerializedName("username")
        val username: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("loginType")
        val loginType: Int = 1,
    ) : Serializable
}
