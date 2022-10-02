package com.youjingjiaoyu.upload.model

/**
 * 请求链接地址时需要实现这个接口
 */
interface LibraryUpdateEntity {
    /**
     * 获取版本号
     *
     * @return 版本号
     */
    val appVersionCode: Int

    /**
     * 是否强制更新 0：不强制更新 1：hasAffectCodes拥有字段强制更新 2：所有版本强制更新
     *
     * @return 是否强制更新
     */
    fun forceAppUpdateFlag(): Int

    /**
     * 版本名 描述作用
     *
     * @return 版本名
     */
    val appVersionName: String

    /**
     * 新安装包的下载地址
     *
     * @return 下载地址
     */
    val appApkUrls: String

    /**
     * 更新日志
     *
     * @return 更新日志
     */
    val appUpdateLog: String

    /**
     * 安装包大小 单位字节
     *
     * @return 安装包大小
     */
    val appApkSize: String

    /**
     * 受影响的版本号 如果开启强制更新 那么这个字段包含的所有版本都会被强制更新 格式 2|3|4
     *
     * @return 受影响的版本号
     */
    val appHasAffectCodes: String

    /**
     * 获取文件的加密校验值
     *
     * @return 获取文件的加密校验值
     */
    val fileMd5Check: String

    /**
     * 渠道
     * @return 渠道
     */
    val channel: String
}