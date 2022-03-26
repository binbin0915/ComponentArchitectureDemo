# ComponentArchitectureDemo

组件化架构+Jetpack

## 运行环境

[AndroidStudio dolphin | 2021.3.1 Canary3]
[支持的Android版本:minSdk 21 targetSdk 32]
[JDK版本：OPENJDK 11]
[Gradle版本：7.4-bin]

<font color=Blue>Test</font>

## 项目背景

    基于Jetpack组件，并且使用Kotlin语言进行开发  
    目的是为了快速进行开发，拉下代码基于本项目就能直接开发。  
    本项目使用了大量的Kotlin特性和语法，简化了大量的代码。
    并且封装了一些常用的的功能。能够开箱即用，快速开发

#### 新建业务模块

    第一步:在business文件夹下新建一个AndroidModel
    第二部:修改Model的build.gradle文件,引入根目录的公共配置app-model-build.gradle
    第三步:在model下的main文件夹内新建alone文件夹,把复制一份清单文件放在里面,这个是独立运行时才会用到
    第四步:在业务模块下新建application包,在里面创建此模块的application类,用于model的SDK初始化
    第五步:在ModelApplicationManage里面注册刚刚创建好的application类,宿主App会自动初始化model的application
    第六步:在宿主app的build.gradle文件里面依赖新建的Mdoel模块
    第七步：复制res文件夹
    第八步：路更新由表