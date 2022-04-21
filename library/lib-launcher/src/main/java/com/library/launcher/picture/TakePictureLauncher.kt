package com.library.launcher.picture

import android.Manifest
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
class TakePictureLauncher :
    BasePictureLauncher<Uri, Boolean>(ActivityResultContracts.TakePicture()) {
    var onSuccess: (path: String) -> Unit = {}
    var onError: (path: String) -> Unit = {}
    var path: String = ""

    /**
     * 打开相机拍照，无需手动请求权限，内部已请求
     * [onSuccess] 成功回调，返回图片路径
     */
    fun lunch(
        onError: (message: String) -> Unit = {},
        onSuccess: (path: String) -> Unit = {}
    ) {
        this.onError = onError
        this.onSuccess = onSuccess
        camera.lunch(Manifest.permission.CAMERA) {
            granted = {
                path =
                    "${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath}/${System.currentTimeMillis()}.jpg"
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
        if (result == true) onSuccess.invoke(path) else onError.invoke("")
    }
}