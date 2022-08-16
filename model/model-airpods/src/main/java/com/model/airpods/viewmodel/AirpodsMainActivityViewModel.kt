package com.model.airpods.viewmodel

import com.library.base.viewmodel.BaseViewModel

/**
 * 错误的消息处理方式
 *
 * 一、使用接口或者其他回调：
 *
 * 通过定义一个接口，让Activity/Fragment来实现接口。
 * 当需要传递消息时，只需要调用接口中的方法。方式简单而又粗暴~~~，但却是最愚蠢的选择。
 * 在MVVM中，我们不再通过接口来进行View与ViewModel之间的通讯，而且ViewModel也不应该持有View的引用。
 *
 * 二、使用LiveData发送消息：
 *
 * 将信息放在LiveData中，Activity/Fragment通过监听LiveData来进行消息处理。这种方式简单有效，但却有一些小缺陷。
 * 1. 当Activity/Fragment销毁时，ViewModel并未销毁。当横竖屏切换时，消息会重复提示（也许我们已经多次处理了消息了）。如果我们在onViewCreated之后进行监听，情况会更恶劣。
 * 每次onDestroyView 进行view的销毁之后，再重新绘制时，都会进行消息的提示。
 * 2. 某些情况下存在多个fragments使用同一个ViewModel。当进行消息通知时，所有的界面可能都会进行消息的处理。显而易见，我们只需要在某一个固定的监听者中进行消息的处理就足够了。
 *
 * 三、使用只有一次通知功能LiveData：
 *
 * 有一些LiveData的继承类，实现了只通知监听者一次。采用这种方式能够有效的解决上一个方案中的第一个问题。 相信很多人已经在实际工作中使用这种方式。
 * 但是当我们遇到第二个问题的时候，我们根本无法判断到底是哪个fragment监听到了消息。
 *
 *
 * 正确的消息处理姿势
 *
 * LiveData具有生命感知功能，并且只在View处于激活状态时进行消息的通知。我们通过一层封装，在充分利用LiveData的优势的条件下，实现我们只进行一次消息通知的功能。
 *
 *
 *
 *
 */


class AirpodsMainActivityViewModel : BaseViewModel() {


}