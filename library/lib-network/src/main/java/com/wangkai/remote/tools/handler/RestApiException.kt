package com.wangkai.remote.tools.handler

/**
 * 网络请求接口执行错误的异常类
 * * 用以与网络状态异常`HttpException`、无法识别域名异常`UnknownHostException`等，描述网络状态的异常相区分
 * @param errorCode 接口执行状态码
 * @param errorMsg 接口执行提示文本
 * @author 王凯
 * @date 2022/07/18
 */
class RestApiException(val errorCode : Int, errorMsg : String) : Exception(errorMsg)