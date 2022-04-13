package com.model.mykotlin.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityFileOperatorBinding

@Route(path = RouterPath.PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class FileOperatorActivity : BaseActivity<BaseViewModel, MykotlinActivityFileOperatorBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    /**
     * # 常用文件操作
     * 1. 获取文件MimeType类型（根据File Name/Path/Url获取相应MimeType-资源的媒体类型）
     * 2. 计算文件或文件夹的大小
     *      * 获取指定文件/文件夹大小
     *      * 获取文件大小
     *      * 自动计算指定文件/文件夹大小
     *      * 格式化大小(BigDecimal实现)
     * 3. 直接打开Url/Uri(远程or本地)
     *      * 打开系统分享弹窗
     *      * 打开浏览器
     *      * 直接打开Url对应的系统应用(通常为系统内置的音视频播放器或浏览器)
     *      * 根据文件路径和类型(后缀判断)显示支持该格式的程序
     * 4. 获取文件Uri/Path
     *      * 从File路径中获取Uri
     *      * 获取Uri对应的文件路径,兼容API 26
     * 5. 通用文件工具类
     */
    private fun coreFile() {

    }


    override fun initData() {

    }

    override fun createdObserve() {

    }
}