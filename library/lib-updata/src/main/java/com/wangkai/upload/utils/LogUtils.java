package com.wangkai.upload.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author wangkai
 */
public class LogUtils {
    /**
     * Log日志工具
     *
     * @param msg
     */
    public static void log(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (AppUpdateUtils.Companion.getInstance().getUpdateConfig().isDebug()) {
                Log.e("【AppUpdateUtils】", msg);
            }
        }
    }
}
