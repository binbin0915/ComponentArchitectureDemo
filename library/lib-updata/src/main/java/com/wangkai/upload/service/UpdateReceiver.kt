package com.wangkai.upload.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import com.wangkai.upload.R
import com.wangkai.upload.utils.AppUpdateUtils
import com.wangkai.upload.utils.AppUtils
import com.wangkai.upload.utils.ResUtils

class UpdateReceiver : BroadcastReceiver() {
    private var lastProgress = 0
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (context.packageName + DOWNLOAD_ONLY == action) {
            //下载
            val progress = intent.getIntExtra(PROGRESS, 0)
            val systemService =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (progress != -1) {
                lastProgress = progress
            }
            showNotification(context, progress, systemService)
            // 下载完成
            if (progress == 100) {
                downloadComplete(systemService)
            }
        } else if (context.packageName + RE_DOWNLOAD == action) {
            //重新下载
            AppUpdateUtils.getInstance().reDownload()
        } else if (context.packageName + CANCEL_DOWNLOAD == action) {
            //取消下载
            val systemService =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            downloadComplete(systemService)
        }
    }

    /**
     * 下载完成
     *
     * @param context
     * @param notifyId
     * @param systemService
     */
    private fun downloadComplete(
        systemService: NotificationManager
    ) {
        systemService.cancel(DOWNLOAD_NOTIFY_ID)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            systemService.deleteNotificationChannel(notificationChannel)
        }
    }

    /**
     * 显示通知栏
     * @param context
     * @param id
     * @param progress
     * @param systemService
     */
    private fun showNotification(
        context: Context, progress: Int, systemService: NotificationManager
    ) {
        val notificationName = "notification"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannel, notificationName, NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(false)
            channel.setShowBadge(false)
            channel.enableVibration(false)
            systemService.createNotificationChannel(channel)
        }
        val builder = Notification.Builder(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationChannel)
        }

        //设置图标
        val notificationIconRes = AppUpdateUtils.getInstance().getUpdateConfig().notificationIconRes
        if (notificationIconRes != 0) {
            builder.setSmallIcon(notificationIconRes)
            builder.setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources, notificationIconRes
                )
            )
        } else {
            builder.setSmallIcon(R.mipmap.upload_ic_launcher)
            builder.setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources, R.mipmap.upload_ic_launcher
                )
            )
        }

        // 设置进度
        builder.setProgress(100, lastProgress, false)
        builder.setWhen(System.currentTimeMillis())
        builder.setShowWhen(true)
        builder.setAutoCancel(false)
        if (progress == -1) {
            val intent = Intent(context.packageName + RE_DOWNLOAD)
            intent.setPackage(context.packageName)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)
            // 通知栏标题
            builder.setContentTitle(ResUtils.getString(R.string.download_fail))
        } else {
            // 通知栏标题
            builder.setContentTitle(AppUtils.getAppName(context) + " " + ResUtils.getString(R.string.has_download) + progress + "%")
        }

        // 设置只响一次
        builder.setOnlyAlertOnce(true)
        val build = builder.build()
        systemService.notify(DOWNLOAD_NOTIFY_ID, build)
    }

    companion object {
        private const val notificationChannel = "10000"

        private const val DOWNLOAD_NOTIFY_ID = 1

        /**
         * 进度key
         */
        const val PROGRESS = "app.progress"

        /**
         * ACTION_UPDATE
         */
        const val DOWNLOAD_ONLY = "app.update"

        /**
         * ACTION_RE_DOWNLOAD
         */
        const val RE_DOWNLOAD = "app.re_download"

        /**
         * 取消下载
         */
        const val CANCEL_DOWNLOAD = "app.download_cancel"
        const val REQUEST_CODE = 1001

        /**
         * 发送进度
         *
         * @param context
         * @param progress
         */
        @JvmStatic
        fun send(context: Context, progress: Int) {
            val intent = Intent(context.packageName + DOWNLOAD_ONLY)
            intent.putExtra(PROGRESS, progress)
            context.sendBroadcast(intent)
        }

        /**
         * 取消下载
         *
         * @param context
         */
        @JvmStatic
        fun cancelDownload(context: Context) {
            val intent = Intent(context.packageName + CANCEL_DOWNLOAD)
            context.sendBroadcast(intent)
        }
    }
}