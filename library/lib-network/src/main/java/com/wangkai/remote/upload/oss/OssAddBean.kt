package com.wangkai.remote.upload.oss

class OssAddBean {
    var index = 0
    var paths //文件路径
            : String
    var ide //标识
            : String? = null
    var type //类型 TP SP YY
            : String? = null

    constructor(index: Int, paths: String, ide: String?, type: String?) {
        this.index = index
        this.paths = paths
        this.ide = ide
        this.type = type
    }

    constructor(paths: String, ide: String?, type: String?) {
        this.paths = paths
        this.ide = ide
        this.type = type
    }

    constructor(paths: String, type: String?) {
        this.paths = paths
        this.type = type
    }

    constructor(paths: String) {
        this.paths = paths
    }
}