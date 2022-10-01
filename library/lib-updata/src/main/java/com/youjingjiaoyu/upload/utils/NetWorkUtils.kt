package com.youjingjiaoyu.upload.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

object NetWorkUtils {
    fun getCurrentNetType(context: Context): String {
        var type = ""
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null) {
            type = "null"
        } else if (info.type == ConnectivityManager.TYPE_WIFI) {
            type = "wifi"
        } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
            val subType = info.subtype
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g"
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g"
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) { // LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g"
            }
        }
        return type
    }
}