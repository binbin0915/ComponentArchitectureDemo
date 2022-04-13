package ando.file.compressor

import com.library.lib_file.core.FileOperator.getContext
import com.library.lib_file.core.*
import com.library.lib_file.core.FileGlobal.MODE_READ_ONLY
import com.library.lib_file.core.FileGlobal.openFileDescriptor
import com.library.lib_file.core.FileUri.getUriByFile
import android.graphics.*
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.library.lib_file.compressor.ImageChecker
import java.io.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * # 图片压缩
 *
 * @author javakam
 * @date 2020/5/21  10:52
 */
object ImageCompressEngine {

    /**
     * com.android.internal.util.ImageUtils.calculateSampleSize
     *
     * 根据图片原始尺寸和需求尺寸计算压缩比例 -> https://developer.android.google.cn/reference/android/graphics/BitmapFactory.Options#inSampleSize
     */
    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        srcWidth: Int,
        srcHeight: Int,
    ): Int {
        if (srcWidth <= 0 || srcHeight <= 0) return 1
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        FileLogger.w("calculateInSampleSize origin, w= $width h=$height")
        var inSampleSize = 1 //采样率

        if (height > srcHeight || width > srcWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= srcHeight && halfWidth / inSampleSize >= srcWidth) inSampleSize *= 2
        }
        FileLogger.w("inSampleSize= $inSampleSize")
        return inSampleSize
    }

    fun calculateInSampleSize(srcWidth: Int, srcHeight: Int): Int {
        if (srcWidth <= 0 || srcHeight <= 0) return 1

        FileLogger.w("calculateInSampleSize origin, w= $srcWidth h=$srcHeight")
        val reqWidth = if (srcWidth % 2 == 1) srcWidth + 1 else srcWidth
        val reqHeight = if (srcHeight % 2 == 1) srcHeight + 1 else srcHeight
        val longSide: Int = max(reqWidth, reqHeight)
        val shortSide: Int = min(reqWidth, reqHeight)
        val scale = shortSide.toFloat() / longSide
        return when {
            scale <= 1 && scale > 0.5625 -> {
                when {
                    longSide < 1664 -> 1
                    longSide < 4990 -> 2
                    longSide in 4991..10239 -> 4
                    else -> if (longSide / 1280 == 0) 1 else longSide / 1280
                }
            }
            scale <= 0.5625 && scale > 0.5 -> if (longSide / 1280 == 0) 1 else longSide / 1280
            else -> ceil(longSide / (1280.0 / scale)).toInt()
        }
    }

    fun rotatingImage(bitmap: Bitmap?, angle: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmap ?: return null, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 图片尺寸压缩 + 质量压缩
     *
     * @param cache 是否将压缩后的图片写入本地 Pictures/xxx
     */
    @Throws(IOException::class, FileNotFoundException::class)
    fun compressCompat(uri: Uri?, targetFile: File, cache: Boolean, focusAlpha: Boolean): Uri? =
        BitmapFactory.Options().run {
            if (uri == null || !FileUtils.checkImage(uri)) return null
            val fd: FileDescriptor = openFileDescriptor(uri, MODE_READ_ONLY)?.fileDescriptor ?: return null

            var tagBitmap: Bitmap? = BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeFileDescriptor(fd, null, this)
                inSampleSize = calculateInSampleSize(outWidth, outHeight)
                inJustDecodeBounds = false
                BitmapFactory.decodeFileDescriptor(fd, null, this)
            }

            val ins: InputStream = getContext().contentResolver.openInputStream(uri) ?: return null
            val angle: Int = if (ImageChecker.isJPG(ins)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    ImageChecker.getRotateDegree(ExifInterface(ins))
                } else {
                    ImageChecker.getOrientation(ins)
                }
            } else -1
            if (angle != -1) tagBitmap = rotatingImage(tagBitmap, angle)

            FileLogger.w("inSampleSize=$inSampleSize ; angle=$angle")

            var quality = 70
            val byteArrOps = ByteArrayOutputStream(1024)
            tagBitmap?.compress(
                if (focusAlpha) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                quality,
                byteArrOps
            )

            // 循环判断压缩后图片是否超过数量限制和大小限制
            while (byteArrOps.toByteArray().size.div(256) > 512L) {
                byteArrOps.reset()
                tagBitmap?.compress(
                    if (focusAlpha) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                    quality,
                    byteArrOps
                )
                // 最低限度
                if (quality == 50) break
                quality -= 10
            }
            val byteArray = byteArrOps.toByteArray()
            tagBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            tagBitmap?.recycle()
            if (cache) {
                //写入缓存
                FileOutputStream(targetFile, false).use {
                    it.write(byteArray)
                    it.flush()
                    byteArrOps.close()
                    ins.close()
                    getUriByFile(targetFile)
                }
            } else {
                byteArrOps.close()
                ins.close()
                null
            }
        }

    /**
     * 图片尺寸压缩 + 质量压缩
     */
    fun compressPure(uri: Uri?, maxSize: Long = 300L): Bitmap? {
        if (uri == null || !FileUtils.checkImage(uri)) return null
        val fd: FileDescriptor = openFileDescriptor(uri, MODE_READ_ONLY)?.fileDescriptor ?: return null

        val bitmap: Bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(fd, null, this)
            inSampleSize = calculateInSampleSize(outWidth, outHeight)

            inJustDecodeBounds = false
            FileLogger.w("compressPure inSampleSize= $inSampleSize")
            BitmapFactory.decodeFileDescriptor(fd, null, this)
        }

        val byteArrOps = ByteArrayOutputStream()
        var quality = 70
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrOps)
        // 大小限制
        while (byteArrOps.toByteArray().size / 1024 > maxSize) {
            byteArrOps.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrOps)
            if (quality == 50) {
                break
            }
            quality -= 10
        }
        byteArrOps.close()
        return bitmap
    }

}