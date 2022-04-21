package com.model.mykotlin.activity

import ando.file.core.FileGlobal
import ando.file.core.FileLogger
import ando.file.core.FileUtils
import ando.file.selector.*
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.flywith24.activityresult.ActivityResultLauncher
import com.kongzue.dialogx.dialogs.MessageDialog
import com.library.base.expand.toastError
import com.library.base.expand.toastSucceed
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.launcher.permission.MultiPermissionLauncher
import com.library.launcher.permission.utils.checkPermissions
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityFileOperatorBinding

@Route(path = RouterPath.PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class FileOperatorActivity : BaseActivity<BaseViewModel, MykotlinActivityFileOperatorBinding>() {
    private val multiPermissionLauncher by lazy { MultiPermissionLauncher() }
    private val activityLauncher by lazy { ActivityResultLauncher() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(multiPermissionLauncher)
        lifecycle.addObserver(activityLauncher)
        viewBinding.btnSelectSingleImg.setOnClickListener {
            requestMultiPermissions()
        }
    }

    /**
     * 请求权限
     */

    //要申请的权限
    private val mPermissions = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun requestMultiPermissions() {
        multiPermissionLauncher.lunch(
            //要申请的权限列表
            permissions = mPermissions,
            //全部权限均已申请成功
            allGranted = {
                //处理成功后的逻辑
                chooseFile()
            },
            //权限申请失败且未勾选不再询问，下次可继续申请
            denied = { list ->
                Log.e("FileOperatorActivity", "下次可继续申请list:$list")
            },
            //权限申请失败且已勾选不再询问，需要向用户解释原因并引导用户开启权限
            explained = {
                MessageDialog.show("权限", "前往设置手动开启相机和读取联系人权限", "去开启", "取消")// 文本
                    .setCancelable(false)//是否允许点击外部区域或返回键关闭
                    .setOkButton { _, _ ->
                        forwardToSettings()
                        false
                    }.setCancelButton { _, _ ->
                        finish()
                        false
                    }
            })
    }

    private fun forwardToSettings() {
        activityLauncher.lunch(
            //配置请求 intent
            setIntent = {
                it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                it.data = Uri.fromParts("package", packageName, null)
            },
            //回调结果
            onActivityResult = {
                if (checkPermissions(this, mPermissions).size != 0) {
                    toastError("手动开启权限失败")
                    return@lunch
                }
                //处理成功后的逻辑
                chooseFile()
                toastSucceed("手动开启权限成功")
            })
    }

    /**
     * 选择文件
     */
    private var mFileSelector: FileSelector? = null
    private fun chooseFile() {
        //图片 Image
        val optionsImage = FileSelectOptions().apply {
            fileType = FileType.IMAGE
            minCount = 1
            maxCount = 2
            minCountTip = "至少选择一张图片" //Select at least one picture
            maxCountTip = "最多选择两张图片" //Select up to two pictures
            singleFileMaxSize = 5242880
            singleFileMaxSizeTip = "单张图片最大不超过5M !" //A single picture does not exceed 5M !
            allFilesMaxSize = 10485760
            allFilesMaxSizeTip =
                "图片总大小不超过10M !" //The total size of the picture does not exceed 10M !
            fileCondition = object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    return (fileType == FileType.IMAGE && uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(
                        uri
                    ))
                }
            }
        }
        //音频 Audio
        val optionsAudio = FileSelectOptions().apply {
            fileType = FileType.AUDIO
            minCount = 2
            maxCount = 3
            minCountTip = "至少选择两个音频文件" //Select at least two audio files
            maxCountTip = "最多选择三个音频文件" //Select up to three audio files
            singleFileMaxSize = 20971520
            singleFileMaxSizeTip = "单音频最大不超过20M !" //Maximum single audio does not exceed 20M !
            allFilesMaxSize = 31457280
            allFilesMaxSizeTip = "音频总大小不超过30M !" //The total audio size does not exceed 30M !
            fileCondition = object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    return (uri != null)
                }
            }
        }
        //文本文件 txt
        val optionsTxt = FileSelectOptions().apply {
            fileType = FileType.TXT
            minCount = 1
            maxCount = 2
            minCountTip = "至少选择一个文本文件" //Select at least one text file
            maxCountTip = "最多选择两个文本文件" //Select at most two text files
            singleFileMaxSize = 5242880
            singleFileMaxSizeTip = "单文本文件最大不超过5M !" //The single biggest text file no more than 5M
            allFilesMaxSize = 10485760
            allFilesMaxSizeTip = "文本文件总大小不超过10M !" //Total size not more than 10M text file
            fileCondition = object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    return (uri != null)
                }
            }
        }

        mFileSelector = FileSelector.with(this).run {
            setMultiSelect()//开启多选
            setRequestCode(10).setTypeMismatchTip("File type mismatch !")
            setMinCount(1, "设定类型文件至少选择一个!")//最小值
            setMaxCount(4, "最多选四个文件!")//最大值
            //选择文件不满足预设条件时的策略
            setOverLimitStrategy(FileGlobal.OVER_LIMIT_EXCEPT_OVERFLOW)
            setSingleFileMaxSize(10737418240, "The size cannot exceed 10G !") //byte (B)
            setAllFilesMaxSize(53687091200, "The total size cannot exceed 50G !")
            //默认不做文件类型约束为"*/*", 不同类型系统提供的选择UI不一样 eg: "video/*","audio/*","image/*"
            setExtraMimeTypes("audio/*", "image/*", "text/plain")
            //如果setExtraMimeTypes和applyOptions没对应上会出现`文件类型不匹配问题`
            applyOptions(optionsImage, optionsAudio, optionsTxt)
            filter(object : FileSelectCondition {
                override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                    return when (fileType) {
                        FileType.IMAGE -> {
                            (uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(uri))
                        }
                        FileType.AUDIO -> true
                        FileType.TXT -> true
                        else -> false
                    }
                }
            })
            callback(object : FileSelectCallBack {
                override fun onSuccess(results: List<FileSelectResult>?) {
                    for (i in 0 until results!!.size) {
                        FileLogger.w("FileSelectCallBack onSuccess ${results[i].fileType}")
                        if (FileType.IMAGE == results[i].fileType) {
                            //加载IMG
                            viewBinding.tvResult.setImageURI(results[i].uri)
                        }
                    }
                }

                override fun onError(e: Throwable?) {
                    FileLogger.e("FileSelectCallBack onError ${e?.message}")
                }
            })
            choose()
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFileSelector?.obtainResult(requestCode, resultCode, data)
    }


    override fun initData() {

    }

    override fun createdObserve() {

    }
}