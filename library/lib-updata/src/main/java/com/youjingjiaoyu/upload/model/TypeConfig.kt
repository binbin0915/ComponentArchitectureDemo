package com.youjingjiaoyu.upload.model

/**
 * 配置信息
 * @author wangkai
 */
object TypeConfig {
    /**
     * 更新信息的来源
     */
    const val DATA_SOURCE_TYPE_MODEL = 10 //调用方提供信息model
    const val DATA_SOURCE_TYPE_URL = 11 //通过配置链接供sdk自主请求
    const val DATA_SOURCE_TYPE_JSON = 12 //调用方提供信息json

    /**
     * 请求方式类型
     */
    const val METHOD_GET = 20 //GET请求
    const val METHOD_POST = 21 //POST请求

    /**
     * UI样式类型
     */
    const val UI_THEME_AUTO = 300 //sdk自主决定，随机从十几种样式中选择一种，并保证同一个设备选择的唯一的
    const val UI_THEME_CUSTOM = 399 //用户自定义UI
    const val UI_THEME_A = 301 //类型A，具体样式效果请关注demo
    const val UI_THEME_B = 302 //类型B，具体样式效果请关注demo
}