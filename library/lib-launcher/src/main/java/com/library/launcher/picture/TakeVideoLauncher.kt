package com.library.launcher.picture

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

/**
 * 描述：
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class TakeVideoLauncher : BasePictureLauncher<Uri, Boolean>(ActivityResultContracts.CaptureVideo()) {
    var onError: (message: String) -> Unit = {}
    var onSuccess: (path: String) -> Unit = {}
    var path: String = ""

    fun lunch(
        onError: (message: String) -> Unit = {},
        onSuccess: (path: String) -> Unit = {}
    ) {
        this.onError = onError
        this.onSuccess = onSuccess
        camera.lunch(Manifest.permission.CAMERA) {
            granted = {
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
            denied = {
                onError.invoke("camera permission denied")
            }
            explained = {
                onError.invoke("camera permission denied")
            }
        }
    }

    override fun onActivityResult(result: Boolean?) {
        onSuccess.invoke(path)
    }
}