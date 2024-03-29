package com.wangkai.remote.tools.url.parase

import okhttp3.HttpUrl

/**
 * 可重定向的Url替换策略功能接口声明
 * @author 王凯
 * @date 2022/07/04
 */
interface UrlReplaceable {

    /**
     * 描述：重定向Url
     * @param originUrl 原始请求url
     * @param redirectBaseUrl 重定向的url(host)
     * @param ignoreSegmentSize 重定向的Url需要替换的文段数目。
     * 即从host域名后开始算起，有几个`/`内的文段需要字符串替换
     *
     * @return HttpUrl 新的请求url
     *
     * @author 王凯
     * @date 2022/07/04
     * */
    fun redirectedUrl(
        originUrl: HttpUrl, redirectBaseUrl: HttpUrl, ignoreSegmentSize: Int
    ): HttpUrl
}