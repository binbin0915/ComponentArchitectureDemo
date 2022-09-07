DSL是什么？

官方解释：所谓的DSL(Domain Specified Language)领域专用语言，其基本思想是“求专不求全”，不像通用目的语言那样目标范围涵盖一切软件问题，而是专门针对某一特定问题的计算机语言
其实DSL并不是单独为Kotlin语言提供的， 简单来说,通过DSL语言我们可以构建属于自己的语法结构，而在Kotlin中并不只有一种方式实现DSL，而主要的实现方式就是高阶函数。

DSL基础用法：

1. 首先我们新建一个Dependency，名字可以随便起，然后我们声明一个list数组，为list提供添加的数据的方法，类代码如下：

```kotlin
class Dependency {
    var libs = mutableListOf<String>()
    fun implementation(lib: String) {
        libs.add(lib)
    }
}
```

2. 接着我们定义一个高阶函数,参数是Dependency的扩展函数

```kotlin
fun dependencies(block: Dependency.() -> Unit): List<String> {
    val dependency = Dependency()
    dependency.block()
    return dependency.libs
}
```

3. 高阶函数中的参数是Dependency的扩展函数，所以我们要先初始化一个Dependency，通过实例调用参数，就可以执行传入的Lambda表达式了

```kotlin
dependencies {
    implementation("com.jacky.ll")
    implementation("com.jacky.hh")
}
```

因为定义的方法，返回的是List可以将它打印出来，代码如下所示：

```kotlin

var list = dependencies {
    implementation("com.jacky.ll")
    implementation("com.jacky.hh")
}
for (text in list) {
    println("$text")
}
```

4. 扩展：DSL还能怎么用？

DSL还可以将符合标准API规范的代码转化为符合人类理解的自然语言

- 首先，我们以创建一个用户对象为例，新建User.kt,为了方便打印 我们重写toString方法，代码如下所示：

```kotlin
data class User(var name: String = "", var age: Int = 0) {
    override fun toString(): String {
        return "My name is $name ,i am $age years old"
    }
}
```

- 按照API规范我们如何来创建一个User对象

```kotlin
val user = User("jacky", 22)
println(user)
```

- 如何使用DSL的方式去创建一个User对象呢，首先我们需要提供一个高阶函数

```kotlin
fun create(block: User.() -> Unit): User {
    val user = User()
    block(user)
    return user
}
```

- 定义了一个类型为User扩展函数的高阶函数，通过block调用表达式的部分 所以我们可以直接这样来创建一个User对象：

```kotlin
val user1 = create {
    name = "jacky"
    age = 22
}
println(user1)
```

5. 结语
   DSL的使用场景远远不止这些，其实前提就是使用好高阶函数，很多例子都讲到了使用DSL来生成HTML的代码，不过在业务中没get到他的作用。

