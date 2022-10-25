package com.wangkai.upload.utils

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
     * Android 上的软件包可见性过滤
     * - 如果应用以 Android 11（API 级别 30）或更高版本为目标平台，并查询与设备上已安装的其他应用相关的信息，则系统在默认情况下会过滤此信息。此过滤行为意味着您的应用无法检测设备上安装的所有应用，这有助于最大限度地减少您的应用可以访问但在执行其用例时不需要的潜在敏感信息。
     * - 此外，过滤后的软件包可见性可帮助 Google Play 等应用商店评估应用为用户提供的隐私权和安全性。例如，Google Play 会将已安装应用的列表视为个人和敏感用户数据。
     * - 有限的应用可见性会影响提供其他应用相关信息的方法的返回结果，例如 queryIntentActivities()、getPackageInfo() 和 getInstalledApplications()。有限的可见性还会影响与其他应用的显式交互，例如启动另一个应用的服务。
     * - 部分软件包是自动可见的。您的应用始终可以在查询其他已安装的应用时检测这些软件包。如需查看其他软件包，请使用 <queries> 元素声明您的应用需要提高软件包可见性。用例页面提供了有关如何选择性地扩展软件包可见性的示例。其中介绍的工作流可让您在保护用户隐私的同时执行常见的应用交互场景。
     * - 在极少数情况下，如果遇到 <queries> 元素无法提供适当的软件包可见性，您还可以使用 QUERY_ALL_PACKAGES 权限。如果您在 Google Play 上发布应用，那么应用使用此权限需要获得批准。
     * - 在 Android 10 及更早版本上，应用程序可以使用queryIntentActivities(). 在大多数情况下，这比应用程序实现其功能所需的访问权限要广泛得多。随着我们对隐私的持续关注，我们正在对应用程序如何在 Android 11 上的同一设备上查询和与其他已安装应用程序交互的方式进行更改。特别是，我们将更好地访问特定设备上安装的应用程序列表设备。
     * 为了更好地负责对设备上已安装应用的访问，以 Android 11（API 级别 30）为目标的应用默认会看到已过滤的已安装应用列表。
     * 为了访问更广泛的已安装应用程序列表，应用程序可以指定有关它们需要直接查询和交互的应用程序的信息。这可以通过<queries>在 Android 清单中添加一个元素来完成。
     * 对于最常见的场景，包括以 开头的任何隐式意图startActivity()，您无需更改任何内容！对于其他场景，例如直接从您的 UI 打开特定的第三方应用程序，开发人员必须明确列出应用程序包名称或意图过滤器签名。
     * ```xml
     * <manifest package="com.example.game">
     *     <queries>
     *         <!-- 您与之交互的特定应用，例如：-->
     *         <package android:name="com.example.store" />
     *         <package android:name ="com.example.service" />
     *         <!--您查询的特定意图，例如：自定义共享 UI-->
     *         <intent>
     *             <action android:name="android.intent.action.SEND" />
     *             <data android:mimeType="image/jpeg " />
     *         </intent>
     *    </queries>
     * </manifest>
     * ```
     *
     * packageManager用于检索与当前安装在设备上的应用程序包相关的各种信息的类。您可以通过Context#getPackageManager.
     * 注意：如果您的应用面向 Android 11（API 级别 30）或更高版本，则此类中的每个方法都会返回经过过滤的应用列表。了解有关如何 管理包可见性的更多信息。
     *
     *
     * @param context
     * @param file
     */
    fun installApkFile(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                context, context.packageName + ".fileprovider", file
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        //此方法在 API 级别 33 中已弃用。 请改用queryIntentActivities(android.content.Intent, android.content.pm.PackageManager.ResolveInfoFlags)。
        //flags：用于修改返回数据的附加选项标志。最重要的是，将决议限制在那些支持的活动上 。或者，设置 为防止对结果进行任何过滤。MATCH_DEFAULT_ONLYIntent.CATEGORY_DEFAULTMATCH_ALL
        if (context.packageManager.queryIntentActivities(intent, PackageManager.INSTALL_SCENARIO_FAST).size > 0) {
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
        val packageName = AppUpdateUtils.getInstance().getContext().packageName
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