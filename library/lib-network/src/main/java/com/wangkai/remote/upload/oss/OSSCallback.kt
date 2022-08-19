package com.wangkai.remote.upload.oss

import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.ServiceException
import com.wangkai.remote.upload.oss.OssUtil.OSSUploadHelperCallback

//oss上传回调
class OSSCallback : OSSUploadHelperCallback {
    //上传成功List
    override fun onSuccess(allPath: List<String>?) {}

    //上传成功Map
    override fun onSuccess(allPathMap: Map<String, String>?) {}
    override fun onSuccessben(allossbean: List<OssAddBean>?) {}

    //上传失败
    override fun onFailure(
        clientException: ClientException?, serviceException: ServiceException?
    ) {
    }

    //上传进度
    override fun onProgres(progress: Int) {}
}