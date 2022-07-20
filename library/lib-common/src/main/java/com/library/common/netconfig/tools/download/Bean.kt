package com.library.common.netconfig.tools.download


data class Bean(
    /*文件下载链接*/
    var url:String,
    var progress:Int=0,
    var downloadState:Int=-1, //-1下载 0下载中 1暂停 2断点下载中 3下载完成
    var isResume:Boolean = false //是否显示没下载完的状态
)
