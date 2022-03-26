package com.model.mykotlin.activity

import android.os.Bundle
import android.text.Spanned
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityBaseBinding
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

@Route(path = RouterPath.PAGE_KOTLIN_BASE_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class KotlinBaseActivity : BaseActivity<BaseViewModel, MykotlinActivityBaseBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val markdown = Markwon.builder(this)
            .usePlugin(HtmlPlugin.create())
            .build()
        markdown.setMarkdown(
            viewBinding.mykotlinTvMarkdown, "# ComponentArchitectureDemo\n" +
                    "\n" +
                    "组件化架构+Jetpack\n" +
                    "\n" +
                    "## 运行环境\n" +
                    "\n" +
                    "[AndroidStudio dolphin | 2021.3.1 Canary3]\n" +
                    "[支持的Android版本:minSdk 21 targetSdk 32]\n" +
                    "[JDK版本：OPENJDK 11]\n" +
                    "[Gradle版本：7.4-bin]\n" +
                    "\n" +
                    "<font color=Blue>Test</font>\n" +
                    "\n" +
                    "## 项目背景\n" +
                    "\n" +
                    "    基于Jetpack组件，并且使用Kotlin语言进行开发  \n" +
                    "    目的是为了快速进行开发，拉下代码基于本项目就能直接开发。  \n" +
                    "    本项目使用了大量的Kotlin特性和语法，简化了大量的代码。\n" +
                    "    并且封装了一些常用的的功能。能够开箱即用，快速开发\n" +
                    "\n" +
                    "#### 新建业务模块\n" +
                    "\n" +
                    "    第一步:在business文件夹下新建一个AndroidModel\n" +
                    "    第二部:修改Model的build.gradle文件,引入根目录的公共配置app-model-build.gradle\n" +
                    "    第三步:在model下的main文件夹内新建alone文件夹,把复制一份清单文件放在里面,这个是独立运行时才会用到\n" +
                    "    第四步:在业务模块下新建application包,在里面创建此模块的application类,用于model的SDK初始化\n" +
                    "    第五步:在ModelApplicationManage里面注册刚刚创建好的application类,宿主App会自动初始化model的application\n" +
                    "    第六步:在宿主app的build.gradle文件里面依赖新建的Mdoel模块\n" +
                    "    第七步：复制res文件夹\n" +
                    "    第八步：路更新由表"
        )

        //toast
        val spanned: Spanned = markdown.toMarkdown("**Hello there!**")
        Toast.makeText(this, spanned, Toast.LENGTH_LONG).show()


    }

    override fun initData() {

    }

    override fun createdObserve() {

    }
}