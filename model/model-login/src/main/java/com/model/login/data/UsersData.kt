package com.model.login.data

import com.google.gson.annotations.SerializedName
import com.wangkai.remote.data.HttpResponseParsable
import java.io.Serializable

/**
 * 用户列表实体类
 * @author wangkai
 * @date 2022/08/21
 */
data class UsersData(
    val data: DataEntity, override val code: Int, override val message: String
) : HttpResponseParsable, Serializable {
    data class DataEntity(
        @SerializedName("count")
        val count: Int
    ) : Serializable
}
