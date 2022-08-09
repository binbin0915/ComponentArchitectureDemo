# FileOperator使用文档

[git地址]https://github.com/javakam/FileOperator.git

> 介绍：  
> Android文件操作库。适用于Android 4.4及以上系统, 已兼容AndroidQ和Android11新的存储策略。  
> 包括处理Android端文件目录及缓存、文件MimeType、文件打开方式、文件路径和Uri、文件大小、文件常用工具类以及文件选择处理等功能。

使用：  
Application中初始化。  
混淆：  
未用到反射, 不需要混淆。

#### 一、常用文件操作

1. 获取文件MimeType类型
2. 计算文件或文件夹的大小
    1. 获取指定文件/文件夹大小
    2. 获取文件大小
    3. 自动计算指定文件/文件夹大小
    4. 格式化大小
3. 直接打开Url/Uri
    1. 打开系统分享弹窗
    2. 打开浏览器
    3. 直接打开Url对应的系统应用（通常为系统内置的音视频播放器或浏览器，如果url是视频地址, 系统会直接用内置的视频播放器打开）
    4. 根据文件路径和类型(后缀判断)显示支持该格式的程序
4. 获取文件Uri/Path
    1. 从File路径中获取Uri
    2. 获取Uri对应的文件路径,兼容API 26
5. 通用文件工具类
   Method | Remark
   :-|:-
   `getMediaShotTime(uri: Uri?, block: (Long))` | 获取媒体文件拍摄时间
   `formatMediaMetadataKeyDate(date: String?): Date?` | 转换`MediaMetadataRetriever.METADATA_KEY_DATE`特殊的时间格式
   `dumpMediaInfoByMediaMetadataRetriever(uri)` | 打印`音频或视频`的详细信息 `(Use MediaMetadataRetriever)`
   `dumpMediaInfoByExifInterface(uri)` | 打印`图片`的详细信息 `(Use ExifInterface)`
   `checkImage(uri)` | 检查`Uri`对应的文件是否为`图片`
   `checkUri(uri)` | 检查`Uri`是否正确; `Uri`指向的文件是否存在
   `getExtension` | 获取文件后缀`jpg`
   `getExtensionFull` | 获取文件完整后缀`.jpg`
   `splitFilePath()` | 拆分文件路径 eg: `/xxx/xxx/note.txt` 👉 `path`: `/xxx/xxx`(注:尾部没有`/`)  `name`:note `suffix`: txt
   `getFileNameFromPath(path: String?)` | 通过`FilePath`获取文件名
   `getFileNameFromUri(uri: Uri?)` | 通过`Uri`获取文件名
   `createFile(filePath: String?, fileName: String?, overwrite: Boolean = false):File?` | 创建文件, 同名文件创建多次会跳过已有创建新的文件,如:note.txt已存在,则再次创建会生成note(1).txt
   `createDirectory(filePath: String?): Boolean` | 创建目录
   `deleteFile` | 删除文件或目录
   `deleteFileWithoutExcludeNames(file: File?, vararg excludeDirs: String?)` | 删除文件或目录, `excludeDirs`指定名称的一些`文件/文件夹`不做删除
   `deleteFilesNotDir` | 只删除文件，不删除文件夹
   `readFileText(InputStream/Uri): String?` | 读取文本文件中的内容
   `readFileBytes(InputStream/Uri): ByteArray?` | 读取文件中的内容并返回`ByteArray`
   `copyFile` | 根据文件路径拷贝文件 `java.nio`
   `writeBytes2File(bytes: ByteArray, target: File)` | 把`ByteArray`写到目标文件`target(File)`中
   `write2File(bitmap:Bitmap, file:File?, overwrite:Boolean=false)` | 把`Bitmap`写到文件中,可通过`BitmapFactory.decodeStream()`读取出来
   `write2File(input:InputStream?, file:File?, overwrite:Boolean=false)` | 向文件中写入数据
   `isLocal` | 检验是否为本地URI
   `isGif()` | 检验是否为 gif

#### 二、选择文件

1. 单选图片
2. 多选文件(多选+多种类型)
3. 自定义FileType
    1. 扩展已有的FileType
    2. 通过IFileType自定义文件类型

#### 三、图片压缩

1. 直接压缩不缓存
2. 压缩图片并缓存
