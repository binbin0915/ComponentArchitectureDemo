package com.youjingjiaoyu.upload.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 内部下载需要的信息
 */
class DownloadInfo : Parcelable {
    /**
     * apk下载地址
     */
    var apkUrl: String? = null
        private set

    /**
     * apk文件大小 单位byte
     */
    var fileSize: Long = 0
        private set

    /**
     * 最新的版本号 用于做比较
     */
    var prodVersionCode = 0
        private set

    /**
     * 最新的版本名称 用于做展示
     */
    var prodVersionName: String? = null
        private set

    /**
     * 更新日志
     */
    var updateLog: String? = null
        private set

    /**
     * 是否强制更新 0 不强制更新 1 所有版本强制更新 2 hasAffectCodes拥有字段强制更新
     */
    private var forceUpdateFlag = 0

    /**
     * 受影响的版本号 如果开启强制更新 那么这个字段包含的所有版本都会被强制更新 格式 2|3|4
     */
    var hasAffectCodes: String? = null
        private set

    /**
     * 文件MD5的校验值
     */
    var md5Check: String? = null
        private set

    /**
     * APK渠道
     */
    var channel: String? = null
        private set

    constructor() {}

    fun setMd5Check(md5Check: String?): DownloadInfo {
        this.md5Check = md5Check
        return this
    }

    fun setHasAffectCodes(hasAffectCodes: String?): DownloadInfo {
        this.hasAffectCodes = hasAffectCodes
        return this
    }

    fun isForceUpdateFlag(): Boolean {
        return forceUpdateFlag > 0
    }

    fun getForceUpdateFlag(): Int {
        return forceUpdateFlag
    }

    fun setForceUpdateFlag(forceUpdateFlag: Int): DownloadInfo {
        this.forceUpdateFlag = forceUpdateFlag
        return this
    }

    fun setUpdateLog(updateLog: String?): DownloadInfo {
        this.updateLog = updateLog
        return this
    }

    fun setApkUrl(apkUrl: String?): DownloadInfo {
        this.apkUrl = apkUrl
        return this
    }

    fun setFileSize(fileSize: Long): DownloadInfo {
        this.fileSize = fileSize
        return this
    }

    fun setProdVersionCode(prodVersionCode: Int): DownloadInfo {
        this.prodVersionCode = prodVersionCode
        return this
    }

    fun setProdVersionName(prodVersionName: String?): DownloadInfo {
        this.prodVersionName = prodVersionName
        return this
    }

    fun setChannel(channel: String?): DownloadInfo {
        this.channel = channel
        return this
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(apkUrl)
        dest.writeLong(fileSize)
        dest.writeInt(prodVersionCode)
        dest.writeString(prodVersionName)
        dest.writeString(updateLog)
        dest.writeInt(forceUpdateFlag)
        dest.writeString(hasAffectCodes)
        dest.writeString(md5Check)
        dest.writeString(channel)
    }

    protected constructor(`in`: Parcel) {
        apkUrl = `in`.readString()
        fileSize = `in`.readLong()
        prodVersionCode = `in`.readInt()
        prodVersionName = `in`.readString()
        updateLog = `in`.readString()
        forceUpdateFlag = `in`.readInt()
        hasAffectCodes = `in`.readString()
        md5Check = `in`.readString()
        channel = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DownloadInfo> = object : Parcelable.Creator<DownloadInfo> {
            override fun createFromParcel(source: Parcel): DownloadInfo {
                return DownloadInfo(source)
            }

            override fun newArray(size: Int): Array<DownloadInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}