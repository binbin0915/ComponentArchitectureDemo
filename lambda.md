##### kotlin 语法糖
- it
> it并不是Kotlin中的一个关键字(保留字)。
> it是在当一个高阶函数中Lambda表达式的参数只有一个的时候可以使用it来使用此参数。it可表示为单个参数的隐式名称，是Kotlin语言约定的。

- also
> also函数的结构实际上和let很像唯一的区别就是返回值的不一样，let是以闭包的形式返回，返回函数体内最后一行的值，如果最后一行为空就返回一个Unit类型的默认值。
> 而also函数返回的则是传入对象的本身

```kotlin
obj.also {
    //TODO
}

```

- apply
> apply函数的返回的是传入对象的本身

```kotlin
obj.apply {
    //TODO
}
```

- let
> let扩展函数的实际上是一个作用域函数，当你需要去定义一个变量在一个特定的作用域范围内，let函数的是一个不错的选择；let函数另一个作用就是可以避免写一些判断null的操作。

```kotlin
obj.let {
    it.todo()//在函数体内使用it替代object对象去访问其公有的属性和方法
}

//另一种用途 判断object为null的操作
obj?.let {//表示object不为null的条件下，才会去执行let函数体
    it.todo()
}

```

- with
- run
- takeIf 和 takeUnless