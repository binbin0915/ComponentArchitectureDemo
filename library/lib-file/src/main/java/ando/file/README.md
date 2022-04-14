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
    3. 直接打开Url对应的系统应用
    4. 根据文件路径和类型(后缀判断)显示支持该格式的程序
4. 获取文件Uri/Path
    1. 从File路径中获取Uri
    2. 获取Uri对应的文件路径,兼容API 26
5. 通用文件工具类

#### 二、选择文件

1. 单选图片
2. 多选文件(多选+多种类型)
3. 自定义FileType
    1. 扩展已有的FileType
    2. 通过IFileType自定义文件类型

#### 三、图片压缩

1. 直接压缩不缓存
2. 压缩图片并缓存
