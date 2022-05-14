package com.library.launcher.picture

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File

/**
 * 描述：
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class TakeVideoLauncher :
    BasePictureLauncher<Uri, Boolean>(ActivityResultContracts.CaptureVideo()) {
    private var onSuccess: (path: String) -> Unit = {}
    private var path: String = ""

    @RequiresPermission(value = Manifest.permission.CAMERA)
    fun lunch(
        onSuccess: (path: String) -> Unit = {}
    ) {
        this.onSuccess = onSuccess
        path =
            "${context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath}/${System.currentTimeMillis()}.mp4"
        val fileUri =
            FileProvider.getUriForFile(
                context,
                "${context.applicationContext.packageName}.chooseProvider",
                File(path)
            )
        launcher.launch(fileUri)
    }

    override fun onActivityResult(result: Boolean?) {
        onSuccess.invoke(path)
    }
}