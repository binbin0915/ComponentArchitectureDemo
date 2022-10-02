package com.youjingjiaoyu.upload.net

import android.accounts.NetworkErrorException
import android.content.Context
import android.text.TextUtils
import com.library.common.network.tools.json.JsonUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object HttpUtils {
    /**
     * 自定义线程池
     */
    private val THREAD_POOL: ExecutorService = ThreadPoolExecutor(
        2,
        5,
        3,
        TimeUnit.SECONDS,
        LinkedBlockingDeque(3),
        Executors.defaultThreadFactory(),
        ThreadPoolExecutor.DiscardOldestPolicy()
    )

    /**
     * GET方法 返回数据会解析成cls对象
     *
     * @param context   上下文
     * @param urlString 请求路径
     * @param listener  回调监听
     * @param cls       返回的对象
     * @param <T>       监听的泛型
     */
    @JvmStatic
    fun <T> doGet(
        context: Context,
        urlString: String,
        headers: Map<String, Any>,
        params: Map<String, Any>,
        cls: Class<T>,
        listener: HttpCallbackModelListener<Any>?
    ) {
        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
        THREAD_POOL.execute {
            val url: URL
            var httpUrlConnection: HttpURLConnection? = null
            try {
                val paramsStr = StringBuilder()
                // 组织请求参数
                params.forEach {
                    paramsStr.append(it.key)
                    paramsStr.append("=")
                    paramsStr.append(it.key)
                    paramsStr.append("&")
                }
                if (paramsStr.isNotEmpty()) {
                    paramsStr.deleteCharAt(paramsStr.length - 1)
                }
                // 根据URL地址创建URL对象
                url = if (urlString.contains("?")) {
                    URL(urlString + paramsStr)
                } else {
                    URL("$urlString?$paramsStr")
                }
                httpUrlConnection = obtainConnection(url, "", headers, false)
                httpUrlConnection.requestMethod = "GET"
                // 响应码为200表示成功，否则失败。
                if (httpUrlConnection.responseCode == 200) {
                    // 获取网络的输入流
                    val `is` = httpUrlConnection.inputStream
                    val bf = BufferedReader(
                        InputStreamReader(
                            `is`, StandardCharsets.UTF_8
                        )
                    )
                    //最好在将字节流转换为字符流的时候 进行转码
                    val buffer = StringBuilder()
                    var line: String?
                    while (bf.readLine().also { line = it } != null) {
                        buffer.append(line)
                    }
                    bf.close()
                    `is`.close()
                    JsonUtils.fromJson(buffer.toString(), cls)?.let {
                        ResponseCall<T>(context, listener).doSuccess(
                            it
                        )
                    }
                } else {
                    if (listener != null) {
                        // 回调onError()方法
                        ResponseCall<Any>(
                            context, listener
                        ).doFail(NetworkErrorException("response err code:" + httpUrlConnection.responseCode))
                    }
                }
            } catch (e: Exception) {
                if (listener != null) {
                    // 回调onError()方法
                    ResponseCall<Any>(context, listener).doFail(e)
                }
            } finally {
                httpUrlConnection?.disconnect()
            }
        }
    }

    /**
     * POST方法 返回数据会解析成cls对象
     *
     * @param context   上下文
     * @param urlString 请求的路径
     * @param listener  回调监听
     * @param params    参数列表
     * @param cls       对象
     * @param <T>       监听泛型
     */
    @JvmStatic
    fun <T> doPost(
        context: Context,
        urlString: String,
        headers: Map<String, Any>,
        params: Map<String, Any>,
        cls: Class<T>,
        listener: HttpCallbackModelListener<Any>?
    ) {
        val paramsStr = StringBuffer()
        // 组织请求参数
        params.forEach {
            paramsStr.append(it.key)
            paramsStr.append("=")
            paramsStr.append(it.key)
            paramsStr.append("&")
        }
        if (paramsStr.isNotEmpty()) {
            paramsStr.deleteCharAt(paramsStr.length - 1)
        }
        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
        THREAD_POOL.execute {
            val url: URL
            var httpUrlConnection: HttpURLConnection? = null
            try {
                url = URL(urlString)
                httpUrlConnection = obtainConnection(
                    url, paramsStr.toString(), headers, true
                )
                httpUrlConnection.requestMethod = "POST"
                if (httpUrlConnection.responseCode == 200) {
                    // 获取网络的输入流
                    val inputStream = httpUrlConnection.inputStream
                    val bf = BufferedReader(
                        InputStreamReader(
                            inputStream, StandardCharsets.UTF_8
                        )
                    )
                    //最好在将字节流转换为字符流的时候 进行转码
                    val buffer = StringBuilder()
                    var line: String?
                    while (bf.readLine().also { line = it } != null) {
                        buffer.append(line)
                    }
                    bf.close()
                    inputStream.close()
                    JsonUtils.fromJson(buffer.toString(), cls)?.let {
                        ResponseCall<T>(context, listener).doSuccess(
                            it
                        )
                    }
                } else {
                    ResponseCall<T>(
                        context, listener
                    ).doFail(NetworkErrorException("response err code:" + httpUrlConnection.responseCode))
                }
            } catch (e: Exception) {
                if (listener != null) {
                    // 回调onError()方法
                    ResponseCall<Any>(context, listener).doFail(e)
                }
            } finally {
                httpUrlConnection?.disconnect()
            }
        }
    }

    @Throws(IOException::class)
    private fun obtainConnection(
        url: URL, params: String, headers: Map<String, Any>, usePostMethod: Boolean
    ): HttpURLConnection {
        val httpUrlConnection = url.openConnection() as HttpURLConnection
        httpUrlConnection.setRequestProperty("Content-type", "application/json")
        headers.forEach {
            httpUrlConnection.setRequestProperty(it.key, it.value.toString())
        }
        httpUrlConnection.connectTimeout = 8000
        httpUrlConnection.readTimeout = 8000
        // 设置运行输入
        httpUrlConnection.doInput = true
        if (usePostMethod) {
            // 设置运行输出
            httpUrlConnection.doOutput = true
            if (!TextUtils.isEmpty(params)) {
                val printWriter = PrintWriter(httpUrlConnection.outputStream)
                // 发送请求参数
                printWriter.write(params)
                // flush输出流的缓冲
                printWriter.flush()
                printWriter.close()
            }
        }
        return httpUrlConnection
    }
}