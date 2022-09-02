Kotlin中StateFlow的使用

StateFlow 是 Flow 的实现，是一个特殊的流，默认的 Flow 是冷流，而StateFlow 是热流，和 LiveData 比较类似。（关于热流冷流的概念）

使用 StateFlow 替代 LiveData 应该是目前很多开发者的呼吁了，确实 LiveData 的功能 StateFlow 都能实现，可以说是 LiveData 的升级版。官方推荐当暴露 UI
的状态给视图时，应该使用 StateFlow。这是一种安全和高效的观察者，专门用于容纳 UI 状态。

一、StateFlow的特点：

1. 它始终是有值的。
2. 它的值是唯一的。
3. 它允许被多个观察者共用 (因此是共享的数据流)。
4. 它永远只会把最新的值重现给订阅者，这与活跃观察者的数量是无关的。

二、StateFlow的使用
