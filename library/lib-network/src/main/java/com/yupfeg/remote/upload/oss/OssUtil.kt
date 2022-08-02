package com.yupfeg.remote.upload.oss

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.*
import com.yupfeg.remote.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * oss
 */
class OssUtil(
    private val context: Context, //图片,视频 统一访问地址
    private var filehttpUrl: String, //上传文件目录名称
    private val uploadFilePath: String,
    private var accessToken: String,
    private var accessKeyId: String,
    private var accessKeySecret: String,
    private var endpoint: String,
    private var bucketName: String
) {
    private var number = 0
    var callback: OSSUploadHelperCallback? = null
    private var client: OSS? = null
    var successPath: MutableList<String> = ArrayList()
    var successPathMap: MutableMap<String, String> = IdentityHashMap()
    fun OssSts() {
        netOssStsToken
    }//这个初始化安全性没有Sts安全
//        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

    // 推荐使用
    // 连接超时，默认15秒
    // socket超时，默认15秒
    // 最大并发请求数，默认5个
    // 失败后最大重试次数，默认2次

    // oss为全局变量，endpoint是一个OSS区域地址
    /**
     * OSS的配置,建议只实例化一次，即不要放在循环体中。
     */
    val ossClient: OSS
        get() {
            if (BuildConfig.DEBUG) {
                OSSLog.enableLog()
            }
            //这个初始化安全性没有Sts安全
            //        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

            // 推荐使用
            val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                accessKeyId, accessKeySecret, accessToken
            )
            val conf = ClientConfiguration()
            conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
            conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
            conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个
            conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次

            // oss为全局变量，endpoint是一个OSS区域地址
            return OSSClient(context, endpoint, credentialProvider, conf)
        }

    /**
     * 多图片上传方法
     *
     * @param paths 需上传文件的路径
     */
    private fun uploads(paths: List<String>) {
        if (client == null) {
            return
        }
        successPath.clear()
        successPathMap.clear()
        number = 1
        for (path in paths) {
            // 构造上传请求
            val request = PutObjectRequest(
                bucketName, getObjectKey(path), path
            )
            // 异步上传可回调上传进度。
            request.progressCallback =
                OSSProgressCallback { request1: PutObjectRequest?, currentSize: Long, totalSize: Long -> }

            //同步上传回调
            val ptask = client!!.asyncPutObject(request,
                object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                    override fun onSuccess(request: PutObjectRequest, result: PutObjectResult?) {
                        Log.e(
                            TAG,
                            request.uploadFilePath + "===上传成功11== ：" + getAllPath(request.objectKey)
                        )
                        //上传成功
                        successPath.add(getAllPath(request.objectKey))
                        successPathMap[request.uploadFilePath] = getAllPath(request.objectKey)
                        if (number == paths.size && callback != null) {
                            callback!!.onSuccess(successPath)
                            callback!!.onSuccess(successPathMap)
                        }
                        number++
                    }

                    override fun onFailure(
                        request: PutObjectRequest,
                        clientException: ClientException?,
                        serviceException: ServiceException?
                    ) {
                        //上传失败
                        if (callback != null) {
                            callback!!.onFailure(clientException, serviceException)
                        }
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            Log.e(
                                TAG, """UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。
  *例如，当网络不可用时，这个异常将被抛出"""
                            )
                            Log.e(TAG, "ErrorCode ：" + clientException.message)
                            Log.e(TAG, "==========================================")
                            clientException.printStackTrace()
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e(TAG, "===UploadFailure：表示在OSS服务端发生错误====")
                            Log.e(TAG, "ErrorCode ：" + serviceException.errorCode)
                            Log.e(TAG, "RequestId ：" + serviceException.requestId)
                            Log.e(TAG, "HostId ：" + serviceException.hostId)
                            Log.e(TAG, "RawMessage ：" + serviceException.rawMessage)
                            Log.e(TAG, "==========================================")
                            //                        if(serviceException.getErrorCode().equals("InvalidAccessKeyId")){
//                            //token过期
//                            getNetOssStsToken();
//                        }
                        }
                    }
                })
            //ptask.cancel(); // 可以取消任务
            //ptask.waitUntilFinished(); // 可以等待直到任务完成
        }
    }

    /**
     * 分片上传
     */
    private var ztotalSize //多个文件总size
            : Long = 0
    private var scZtotalSize //多个文件上传的总size
            : Long = 0

    fun uploadMultipart(paths: List<String>, callback: OSSUploadHelperCallback?) {
        this.callback = callback
        if (client == null) {
            return
        }
        ztotalSize = 0
        scZtotalSize = 0
        successPath.clear()
        successPathMap.clear()
        number = 1
        for (path in paths) {
            val request = MultipartUploadRequest<ResumableUploadRequest>(
                bucketName, getObjectKey(path), path
            )
            // 回调上传进度。
            val file = File(path)
            ztotalSize += file.length()
            request.setProgressCallback { request1: ResumableUploadRequest, currentSize: Long, totalSize: Long ->
                Log.e(
                    TAG, request1.uploadFilePath + "===上传成功== ：" + request1.objectKey
                )
                if (currentSize == totalSize) { //表示第N个文件传完
                    if (paths[paths.size - 1] != request1.uploadFilePath) { //还要判断是否是最后一个文件
                        scZtotalSize += totalSize
                    }
                }
                val progress = (100 * (currentSize + scZtotalSize) / ztotalSize).toInt()
                Log.d(
                    TAG, "上传进度=$progress"
                )
                callback!!.onProgres(progress)
            }

            //同步上传回调
            val task = client!!.asyncMultipartUpload(request,
                object :
                    OSSCompletedCallback<MultipartUploadRequest<*>, CompleteMultipartUploadResult?> {
                    override fun onSuccess(
                        request: MultipartUploadRequest<*>, result: CompleteMultipartUploadResult?
                    ) {
//                    Log.e(TAG, request.getUploadFilePath()+"===上传成功== ：" + request.getObjectKey());
                        //上传成功
                        successPath.add(getAllPath(request.objectKey))
                        successPathMap[request.uploadFilePath] = getAllPath(request.objectKey)
                        if (number == paths.size && callback != null) {
                            callback.onSuccess(successPath)
                            callback.onSuccess(successPathMap)
                        }
                        number++
                    }

                    override fun onFailure(
                        request: MultipartUploadRequest<*>,
                        clientException: ClientException?,
                        serviceException: ServiceException?
                    ) {
                        //上传失败
                        callback?.onFailure(clientException, serviceException)
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            Log.e(
                                TAG,
                                """UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。*例如，当网络不可用时，这个异常将被抛出"""
                            )
                            Log.e(TAG, "ErrorCode ：" + clientException.message)
                            Log.e(TAG, "==========================================")
                            clientException.printStackTrace()
                        }

                        if (serviceException != null) {
                            OSSLog.logError(serviceException.rawMessage)
                            // 服务异常
                            Log.e(TAG, "===UploadFailure：表示在OSS服务端发生错误====")
                            Log.e(TAG, "ErrorCode ：" + serviceException.errorCode)
                            Log.e(TAG, "RequestId ：" + serviceException.requestId)
                            Log.e(TAG, "HostId ：" + serviceException.hostId)
                            Log.e(TAG, "RawMessage ：" + serviceException.rawMessage)
                            Log.e(TAG, "==========================================")
                            //                        if(serviceException.getErrorCode().equals("InvalidAccessKeyId")){
//                            //token过期
//                            getNetOssStsToken();
//                        }
                        }
                    }
                })
            //task.cancel();//取消分片上传。
            //task.waitUntilFinished();// 可以等待直到任务完成
        }
    }

    /**
     * 分片上传
     */
    var successBean: MutableList<OssAddBean>? = ArrayList()
    fun uploadMultipartBean(ossAddBeans: List<OssAddBean>, callback: OSSUploadHelperCallback?) {
        this.callback = callback
        if (client == null) {
            return
        }
        ztotalSize = 0
        scZtotalSize = 0
        successPath.clear()
        successPathMap.clear()
        number = 1
        for (ossbean in ossAddBeans) {
            val request = MultipartUploadRequest<ResumableUploadRequest>(
                bucketName, getObjectKey(ossbean.paths), ossbean.paths
            )
            // 回调上传进度。
            val file = File(ossbean.paths)
            ztotalSize += file.length()
            request.setProgressCallback { request1: ResumableUploadRequest, currentSize: Long, totalSize: Long ->
//                    Log.e(TAG, request.getUploadFilePath()+"===上传成功== ：" + request.getObjectKey());
//                    Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if (currentSize == totalSize) { //表示第N个文件传完
                    if (ossAddBeans[ossAddBeans.size - 1].paths.equals(request1.uploadFilePath)) { //还要判断是否是最后一个文件
                        scZtotalSize += totalSize
                    }
                }
                val progress = (100 * (currentSize + scZtotalSize) / ztotalSize).toInt()
                Log.d(
                    TAG, "上传进度=$progress"
                )
                callback!!.onProgres(progress)
            }

            //同步上传回调
            val task = client!!.asyncMultipartUpload(request,
                object :
                    OSSCompletedCallback<MultipartUploadRequest<*>, CompleteMultipartUploadResult?> {
                    override fun onSuccess(
                        request: MultipartUploadRequest<*>, result: CompleteMultipartUploadResult?
                    ) {
//                    Log.e(TAG, ossbean.getType()+"===上传成功== ：" + request.getObjectKey());
                        //上传成功
                        successBean!!.add(
                            OssAddBean(
                                ossbean.index,
                                getAllPath(request.objectKey),
                                ossbean.ide,
                                ossbean.type
                            )
                        )
                        if (number == ossAddBeans.size && successBean != null) {
                            callback!!.onSuccessben(successBean)
                        }
                        number++
                    }

                    override fun onFailure(
                        request: MultipartUploadRequest<*>,
                        clientException: ClientException?,
                        serviceException: ServiceException?
                    ) {

                        //上传失败
                        callback?.onFailure(clientException, serviceException)
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            Log.e(
                                TAG,
                                """UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。*例如，当网络不可用时，这个异常将被抛出"""
                            )
                            Log.e(TAG, "ErrorCode ：" + clientException.message)
                            Log.e(TAG, "==========================================")
                            clientException.printStackTrace()
                        }
                        if (serviceException != null) {
                            OSSLog.logError(serviceException.rawMessage)
                            // 服务异常
                            Log.e(TAG, "===UploadFailure：表示在OSS服务端发生错误====")
                            Log.e(TAG, "ErrorCode ：" + serviceException.errorCode)
                            Log.e(TAG, "RequestId ：" + serviceException.requestId)
                            Log.e(TAG, "HostId ：" + serviceException.hostId)
                            Log.e(TAG, "RawMessage ：" + serviceException.rawMessage)
                            Log.e(TAG, "==========================================")
                            //                        if(serviceException.getErrorCode().equals("InvalidAccessKeyId")){
//                            //token过期
//                            getNetOssStsToken();
//                        }
                        }
                    }
                })
            //task.cancel();//取消分片上传。
            //task.waitUntilFinished();// 可以等待直到任务完成
        }
    }

    // 拼接自己服务器的URL
    private fun getAllPath(objectKey: String): String {
        var imageUrl = ""
        if (!TextUtils.isEmpty(objectKey)) {
//            int i = objectKey.lastIndexOf("/");
//            String substring = objectKey.substring(i + 1, objectKey.length());
//            imageUrl = imghttpUrl + uploadFilePath + substring;
//            Log.e("Oss==>>", substring);
//            Log.e("Oss==>>", imageUrl);
            imageUrl = filehttpUrl + objectKey
        }
        return imageUrl
    }

    /**
     * 上传单个文件   (图片,视频,gif,.....)
     *
     * @param path     本地地址
     * @param callback 成功回调
     */
    fun uploadImage(path: String, callback: OSSUploadHelperCallback?) {
        this.callback = callback
        val strings: MutableList<String> = ArrayList()
        strings.add(path)
        uploads(strings)
    }

    /**
     * 上传多文件   (图片,视频,gif,.....)
     *
     * @param paths    本地地址
     * @param callback 成功回调
     */
    fun uploadImages(paths: List<String>, callback: OSSUploadHelperCallback?) {
        this.callback = callback
        uploads(paths)
    }

    /**
     * 取得系统时间
     *
     * @return 时间戳
     */
    private val dateString: String
        get() = System.currentTimeMillis().toString() + ""
    private val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.CANADA) // 格式化时间
    private val date = Date() // 获取当前时间

    /**
     * 返回key
     * 格式: app/uploadFilePath/2021/04/21/sfdsgfsdvsdfdsfs.jpg
     *
     * @param path 本地路径
     * @return key
     */
    private fun getObjectKey(path: String): String {
        Log.e("Oss==>>", path)
        Log.e(
            "Oss==>>", String.format(
                uploadFilePath + sdf.format(date) + "/" + "%s." + getFormatName(path), dateString
            )
        )
        return String.format(
            uploadFilePath + sdf.format(date) + "/" + "%s." + getFormatName(path), dateString
        )
    }

    /**
     * 获取文件格式名
     */
    private fun getFormatName(fileName: String): String {
        //去掉首尾的空格
        var fileNameCache = fileName
        fileNameCache = fileNameCache.trim { it <= ' ' }
        val s = fileNameCache.split("\\.").toTypedArray()
        return if (s.size >= 2) {
            s[s.size - 1]
        } else ""
    }

    interface OSSUploadHelperCallback {
        //list上传成功
        fun onSuccess(allPath: List<String>?)

        //map上传成功
        fun onSuccess(allPathMap: Map<String, String>?)

        //bean上传成功
        fun onSuccessben(allossbean: List<OssAddBean>?)

        //上传失败
        fun onFailure(clientException: ClientException?, serviceException: ServiceException?)

        //上传进度
        fun onProgres(progress: Int)
    }//得到client

    /**
     * 获取Oss 的ststoken
     */
    private val netOssStsToken: Unit
        get() {
//            bucketName = SpUtil.get(KeyConfig.bucketName, "")
//            accessKeyId = SpUtil.get(KeyConfig.accessKeyId, "")
//            accessKeySecret = SpUtil.get(KeyConfig.accessKeySecret, "")
//            accessToken = SpUtil.get(KeyConfig.accessStsToken, "")
//            endpoint = SpUtil.get(KeyConfig.endpoint, "")
//            filehttpUrl = SpUtil.get(KeyConfig.osstpdz, "")
//            //得到client
//            client = getOSSClient()
        }

    companion object {
        private const val TAG = "=====OssUtil===>>>"
    }
}