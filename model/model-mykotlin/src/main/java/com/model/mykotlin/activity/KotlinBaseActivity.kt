package com.model.mykotlin.activity

import android.os.Bundle
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
        val markdown = Markwon.builder(this).usePlugin(HtmlPlugin.create()).build()
        markdown.setMarkdown(
            //使用原生字符串
            viewBinding.mykotlinTvMarkdown, """
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
            """.trimIndent()
        )

        //toast
//        val spanned: Spanned = markdown.toMarkdown("**Hello there!**")
//        Toast.makeText(this, spanned, Toast.LENGTH_LONG).show()

        //TODO: 0.1 lambda 表达式
        val subtract = { a: Int, b: Int, c: Int -> a - b - c }

        //TODO: 1.1 常规高阶函数的匿名函数调用
        operate(1, 2, 3, fun(a: Int, b: Int, c: Int): Int { return a + b + c })
        //TODO: 1.2 常规高阶函数的 lambda 方式调用
        operate(3, 4, 5) { a, b, c -> a - b - c }

        //TODO: 2.2 高阶函数柯里化后的匿名函数调用
        operate1(5)(6)(7)(fun(a: Int, b: Int, c: Int): Int { return a + b + c })
        //TODO: 2.2 高阶函数柯里化后的 lambda 调用
        operate1(7)(8)(9)(subtract)
    }

    //TODO: 1. 实现对三个数加减乘除的操作（多参数）
    private fun operate(a: Int, b: Int, c: Int, test: (Int, Int, Int) -> Int): Int {
        return test(a, b, c)
    }

    //TODO: 2. 对多参数函数柯里化操作
    private fun operate1(a: Int) = { b: Int ->
        { c: Int ->
            { test: (Int, Int, Int) -> Int ->
                {
                    test(a, b, c)
                }
            }
        }
    }


    override fun initData() {

    }

    override fun createdObserve() {

    }
}