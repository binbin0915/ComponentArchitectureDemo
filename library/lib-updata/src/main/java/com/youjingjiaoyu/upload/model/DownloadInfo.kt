package com.youjingjiaoyu.upload.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 内部下载需要的信息
 * @author wangkai
 */
data class DownloadInfo(
    /**
     * apk下载地址
     */
    var apkUrl: String,
    /**
     * apk文件大小 单位byte
     */
    var fileSize: Long = 0,
    /**
     * 最新的版本号 用于做比较
     */
    var prodVersionCode: Int = 0,
    /**
     * 最新的版本名称 用于做展示
     */
    var prodVersionName: String,
    /**
     * 更新日志
     */
    var updateLog: String,
    /**
     * 是否强制更新 0 不强制更新 1 所有版本强制更新 2 hasAffectCodes拥有字段强制更新
     */
    var forceUpdateFlag: Int = 0,
    /**
     * 受影响的版本号 如果开启强制更新 那么这个字段包含的所有版本都会被强制更新 格式 2|3|4
     */
    var hasAffectCodes: String,
    /**
     * 文件MD5的校验值
     */
    var md5Check: String,
    /**
     * APK渠道
     */
    var channel: String
) : Parcelable {

    /**
     * 是否强制更新
     */
    val isForceUpdateFlag: Boolean
        get() {
            return forceUpdateFlag > 0
        }

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(apkUrl)
        parcel.writeLong(fileSize)
        parcel.writeInt(prodVersionCode)
        parcel.writeString(prodVersionName)
        parcel.writeString(updateLog)
        parcel.writeInt(forceUpdateFlag)
        parcel.writeString(hasAffectCodes)
        parcel.writeString(md5Check)
        parcel.writeString(channel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadInfo> {
        override fun createFromParcel(parcel: Parcel): DownloadInfo {
            return DownloadInfo(parcel)
        }

        override fun newArray(size: Int): Array<DownloadInfo?> {
            return arrayOfNulls(size)
        }
    }
}