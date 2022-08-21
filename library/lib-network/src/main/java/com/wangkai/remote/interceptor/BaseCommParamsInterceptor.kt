package com.wangkai.remote.interceptor

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 添加公共请求参数与请求头的应用层拦截器基类
 * @author 王凯
 * @date 2022/08/20
 */
abstract class BaseCommParamsInterceptor : Interceptor {
    //公共参数
    abstract val pathParams: ConcurrentHashMap<String, String>
    abstract val bodyParams: ConcurrentHashMap<String, String>
    abstract val headersParams: ConcurrentHashMap<String, String>

    //过滤掉不添加公共参数的url
    abstract val excludeUrls: List<String>


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        //请求的ur如果l在过滤Url组里面的直接就执行请求，不添加公共参数
        for (excludeUrl in excludeUrls) {
            if (excludeUrl.isNotEmpty() && url.contains(excludeUrl)) {
                return chain.proceed(request)
            }
        }
        val requestHeaders = addHeader(request)
        val newRequest = parseRequest(requestHeaders)
        return chain.proceed(newRequest)
    }

    private fun addHeader(request: Request): Request {
        val builder = request.newBuilder()
        for ((key, value) in headersParams.entries) {
            builder.addHeader(key, value)
        }
        return builder.build()
    }

    /**
     * 解析请求
     */
    private fun parseRequest(request: Request): Request {
        return when (request.method.lowercase(Locale.getDefault())) {
            "get" -> {
                addGetParams(request)
            }
            "delete" -> {
                addGetParams(request)
            }
            "head" -> {
                addGetParams(request)
            }
            "options" -> {
                addGetParams(request)
            }
            "post" -> {
                addPostParams(request)
            }
            "put" -> {
                addPostParams(request)
            }
            "patch" -> {
                addPostParams(request)
            }
            else -> {
                addGetParams(request)
            }
        }
    }

    /**
     * 添加GET方式的参数
     */
    private fun addGetParams(request: Request): Request {
        val url = urlAppendParams(request)
        return request.newBuilder()
            .url(url)
            .build()
    }

    /**
     *公共参数添加在Url上
     */
    private fun urlAppendParams(request: Request): HttpUrl {
        val builder = request.url.newBuilder()
        for ((key, value) in pathParams.entries) {
            builder.addQueryParameter(key, value)
        }
        return builder.build()
    }

    /**
     * 添加POST方式的参数
     */
    private fun addPostParams(request: Request): Request {
        return addPostParamsInBody(request)
    }

    /**
     * POST方式-公共参数添加在Url上
     * POST方式-公共参数添加在Body里面
     */
    private fun addPostParamsInBody(request: Request): Request {
        val body = request.body ?: return request
        val url = urlAppendParams(request)
        return when (body) {
            //表单提交
            is FormBody -> {
                val builder = FormBody.Builder()
                //添加原来已有的参数
                for (i in 0 until body.size) {
                    builder.add(body.name(i), body.value(i))
                }
                //添加额外的参数
                for ((key, value) in bodyParams.entries) {
                    builder.add(key, value)
                }
                request.newBuilder()
                    .method(request.method, builder.build())
                    .url(url)
                    .build()
            }
            is MultipartBody -> {
                request.newBuilder()
                    .method(request.method, request.body)
                    .url(url)
                    .build()
            }
            //Body提交
            else -> {
                val buffer = Buffer()
                body.writeTo(buffer)
                val json = buffer.readUtf8()
                val jsonObject = if (json.isEmpty()) {
                    JSONObject()
                } else {
                    JSONObject(json)
                }
                for ((key, value) in bodyParams.entries) {
                    jsonObject.put(key, value)
                }
                val content: String = jsonObject.toString()
                val contentBody =
                    content.toRequestBody("application/json; charset=UTF-8".toMediaType())
                request.newBuilder()
                    .method(request.method, contentBody)
                    .url(url)
                    .build()
            }
        }
    }
}