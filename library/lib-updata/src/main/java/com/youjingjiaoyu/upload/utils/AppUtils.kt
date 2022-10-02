package com.youjingjiaoyu.upload.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object AppUtils {

    private var density = 0f

    /**
     * 安装app
     *
     * @param context
     * @param file
     */
    fun installApkFile(context: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                context, context.packageName + ".fileprovider",
                file!!
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        if (context.packageManager.queryIntentActivities(intent, 0).size > 0) {
            context.startActivity(intent)
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        if (density == 0f) {
            density = Resources.getSystem().displayMetrics.density
        }
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

    /**
     * 获取apk的安装路径
     *
     * @return
     */
    @JvmStatic
    fun getAppLocalPath(context: Context, versionName: String): String {
        // apk 保存名称
        val apkName = getAppName(context)
        return getAppRootPath(context) + "/" + apkName + "_" + versionName + ".apk"
    }

    /**
     * 获取apk存储的根目录
     *
     * @return
     */
    fun getAppRootPath(context: Context): String {
        //构建下载路径
        val packageName = AppUpdateUtils.getInstance().context.packageName
        return context.externalCacheDir.toString() + "/" + packageName + "/apks"
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    @JvmStatic
    fun deleteFile(filePath: String?) {
        if (filePath == null) {
            return
        }
        val file = File(filePath)
        try {
            if (file.isFile) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 删除文件夹的所有文件
     *
     * @param file
     * @return
     */
    @JvmStatic
    fun delAllFile(file: File?): Boolean {
        if (file == null || !file.exists()) {
            return false
        }
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    delAllFile(f)
                }
            }
        }
        return file.delete()
    }

    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        var versionName = ""
        val packInfo = getPackInfo(context)
        if (packInfo != null) {
            versionName = packInfo.versionName
        }
        return versionName
    }

    /**
     * 获得apkinfo
     *
     * @param context
     * @return
     */
    private fun getPackInfo(context: Context): PackageInfo? {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo
    }

    /**
     * 获得apk版本号
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getVersionCode(context: Context): Int {
        var versionCode = 0
        val packInfo = getPackInfo(context)
        if (packInfo != null) {
            versionCode = packInfo.versionCode
        }
        return versionCode
    }
}