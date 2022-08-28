* `getDrawable()`                   😕最简单但有警告
* `getResources().getDrawable()`       😕具有主题属性的drawable
* `ContextCompat.getDrawable()`        😕默认app主题
* `ResourcesCompat.getDrawable()`      😕ResourcesCompat的内部实现

# Drawable详解

| Number | xml标签        | Class类            | 含义                   |
| ------ | -------------- | ------------------ | ---------------------- |
| 1      | shape          | ShapeDrawable      | 特定形状，模型的图样   |
| 2      | selector       | StateListDrawable  | 不同状态选择不同的图样 |
| 3      | layer-list     | LayerDrawable      | 层叠图样               |
| 4      | level-list     | LevelListDrawable  | 不同程度图样           |
| 5      | transition     | TransitionDrawable | 渐变图样               |
| 6      | ripple         | RippleDrawable     | 波纹图样               |
| 7      | inset          | InsetDrawable      | 内嵌图样               |
| 8      | scale          | ScaleDrawable      | 缩放图样               |
| 9      | clip           | ClipDrawable       | 剪切图样               |
| 10     | rotate         | RotateDrawable     | 旋转图样               |
| 11     | animation-list | AnimationDrawable  | 动画效果               |
| 12     | bitmap         | BitmapDrawable     | 图片图样               |
| 13     | nine-patch     | NinePatchDrawable  | .9图                   |

## 一、shape - ShapeDrawable : 特定形状，模型的图样

###### 1.shape标签

| 属性      | 含义                                                 |
| --------- | ---------------------------------------------------- |
| rectangle | 矩形，默认的形状，可以画出直角矩形、圆角矩形、弧形等 |
| oval      | 边界线的宽度                                         |
| line      | 线形，可以画实线和虚线                               |
| ring      | 环形，可以画环形进度条                               |

| 属性              | 含义                  | 值等解释                                                                                                                                                    |
| ----------------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| shape的公共属性： |                       |                                                                                                                                                             |
| tint              | 着色                  | 给shape着色                                                                                                                                                 |
| tintMode          | 着色模式              | 着色模式（有关tint和tintMode请参看文章：[http://blog.csdn.net/u010687392/article/details/47399719](http://blog.csdn.net/u010687392/article/details/47399719)） |
| dither            | 抖动平滑：建议true    | 将在位图的像素配置与屏幕不同时（例如：ARGB 8888 位图和 RGB 565 屏幕）启用位图的抖动；值为“false”时则停用抖动。默认值为 true。                             |
| visible           | -                     | -                                                                                                                                                           |
| useLevel          | 使用等级，建议为false | 如果为true，则可在LevelListDrawable中使用。这通常应为“false”，否则形状不会显示。                                                                          |
| 环形特有属性：    |                       |                                                                                                                                                             |
| thickness         | 外环厚度              | 环的厚度，指内环与外环的环间距。（只适用于shape为ring）                                                                                                     |
| thicknessRatio    | 同上，浮点型          | 浮点型，以环的宽度比率来表示环的厚度，默认为9，表示环的厚度为环的宽度除以9，该值会被android:thickness覆盖（只适用于shape为ring）                            |
| innerRatio        | 内环半径              | 内环半径（只适用于shape为ring）                                                                                                                             |
| innerRadiusRatio  | 同上，浮点型          | 浮点型，以环的宽度比率来表示内环的半径，默认为3，表示内环半径为环的宽度除以3，该值会被android:innerRadius覆盖（只适用于shape为ring）                        |

###### 2.shape下size标签：设置shape宽高值

注意事项：只有控件宽高设置成wrap_content时，此处宽高才起作用，但是起到的却是最小宽高值。也就是说，当控件宽高超过你此处指定的值时，它会变化（wrap_content!!!）

| 属性   | 含义 |
| ------ | ---- |
| width  | 宽度 |
| height | 高度 |

###### 3、shape下solid标签：设置形状填充颜色

| 属性  | 含义     |
| ----- | -------- |
| color | 指定颜色 |

###### 4、shape下padding标签：设置内容与边界的距离

| 属性   | 含义     |
| ------ | -------- |
| left   | 左内边距 |
| top    | 上内边距 |
| right  | 右内边距 |
| bottom | 左内边距 |

###### 5、shape下corners标签：设置四个角的圆角

| 属性              | 含义         |
| ----------------- | ------------ |
| radius            | 四个角圆角   |
| topLeftRadius     | 左上角的圆角 |
| topRightRadius    | 右上角的圆角 |
| bottomLeftRadius  | 左下角的圆角 |
| bottomRightRadius | 右下角的圆角 |

###### 6、shape下stroke标签：设置shape的外边界线

| 属性      | 含义         |
| --------- | ------------ |
| color     | 描边的颜色   |
| width     | 边界线的宽度 |
| dashWidth | 段虚线的宽度 |
| dashGap   | 段虚线的间隔 |

###### 7、shape下的gradient标签：设置形状渐变

| 属性           | 含义                | 值等解释                                                                                                                      |
| -------------- | ------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| type           | 渐变的类型          | 1.linear：线性渐变，默认的渐变类型2.radial：放射渐变，设置该项时，必须设置android:gradientRadius渐变半径属3.sweep：扫描性渐变 |
| angle          | 渐变角度            | 渐变的角度，线性渐变时(linear也是默认的渐变类型)才有效，必须是45的倍数，0表示从左到右，90表示从下到上                         |
| centerX        | 渐变中心的相对X坐标 | 放射渐变时(radial)才有效，在0.0到1.0之间，默认为0.5，表示在正中间                                                             |
| centerY        | 渐变中心的相对Y坐标 | 放射渐变时(radial才有效，在0.0到1.0之间，默认为0.5，表示在正中间                                                              |
| useLevel       | 使用等级            | 如果为true，则可在LevelListDrawable中使用。这通常应为“false”，否则形状不会显示                                              |
| startColor     | 渐变开始的颜色      | -                                                                                                                             |
| centerColor    | 渐变中间的颜色      | -                                                                                                                             |
| endColor       | 渐变结束的颜色      | -                                                                                                                             |
| gradientRadius | 渐变半径            | 渐变的半径，只有渐变类型为radial时才使用                                                                                      |

## 二、selector - StateListDrawable：不同状态选择不同的图样

selector下的item标签

1.1.作为drawable资源使用时，一般和shape一样放于drawable目录下，item必须指定android:drawable属性。
1.2.作为color资源使用时，则放于color目录下，item必须指定android:color属性。

| 属性                 | 含义                                                                                                                                                                                                               |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| drawable             | color颜色或者其他drawable或图片等                                                                                                                                                                                  |
| state_enabled        | 设置触摸或点击事件是否可用状态**，一般只在false时设置该属性，表示不可用状态                                                                                                                                        |
| state_pressed        | 设置是否按压状态**，一般在true时设置该属性，表示已按压状态，默认为false                                                                                                                                            |
| state_selected       | 设置是否选中状态**，true表示已选中，false表示未选中                                                                                                                                                                |
| state_checked        | 设置是否勾选状态**，主要用于CheckBox和RadioButton，true表示已被勾选，false表示未被勾选                                                                                                                             |
| state_checkable      | 设置勾选是否可用状态**，类似state_enabled，只是state_enabled会影响触摸或点击事件，state_checkable影响勾选事件                                                                                                      |
| state_focused        | 设置是否获得焦点状态**，true表示获得焦点，默认为false，表示未获得焦点                                                                                                                                              |
| state_window_focused | 设置当前窗口是否获得焦点状态**，true表示获得焦点，false表示未获得焦点，例如拉下通知栏或弹出对话框时， 当前界面就会失去焦点；另外，ListView的ListItem获得焦点时也会触发true状态，可以理解为当前窗口就是ListItem本身 |
| state_activated      | 设置是否被激活状态**，true表示被激活，false表示未激活，API Level 11及以上才支持，可通过代码调用控件的setActivated(boolean)方法设置是否激活该控件                                                                   |
| state_hovered        | 设置是否鼠标在上面滑动的状态**，true表示鼠标在上面滑动，默认为false，API Level 14及以上才支持                                                                                                                      |
| exitFadeDuration     | 状态改变时，旧状态消失时的淡出时间，以毫秒为单位                                                                                                                                                                   |
| enterFadeDuration    | 状态改变时，新状态展示时的淡入时间，以毫秒为单位                                                                                                                                                                   |

注意：异常：tag requires a 'drawable' attribute or child tag defining a drawable，可用drawable下的shape中添加color颜色值

## 三、layer-list - LayerDrawable：层叠图样

layer-list下的item标签。上面已经讲过了，item下面能放各种样式的drawable或者shape图样

>>案例

1、上左右，乱七八糟阴影，内部渐变色，坐上右上弧角

2、阴影图案

## 四、level-list - LevelListDrawable：不同程度图样

###### level-list下的item标签。

| 属性     | 含义   |
| -------- | ------ |
| minLevel | 最小值 |
| maxLevel | 最大值 |
| drawable | 图样   |

案例：比方说电量、wifi质量，音量等

## 五、transition - TransitionDrawable：渐变图样

## 六、ripple - RippleDrawable：波纹图样


| ripple属性      | 含义                            |
| --------------- | ------------------------------- |
| color           | 波纹颜色                        |
| tools:targetApi | lollipop，必须这个api以上才能用 |

## 七、inset - InsetDrawable：内嵌图样

| inset属性   | 含义         |
| ----------- | ------------ |
| insetTop    | 嵌入内部上边 |
| insetBottom | 嵌入内部下边 |
| insetLeft   | 嵌入内部左边 |
| insetRight  | 嵌入内部右边 |

## 八、scale - ScaleDrawable：缩放图样

| scale属性    | 含义                                                                        |
| ------------ | --------------------------------------------------------------------------- |
| drawable     | 资源引用                                                                    |
| scaleGravity | 缩放的方向, 比如: top, 缩放的时候就会向顶部靠拢,bottom, 缩放时会想底部靠拢; |
| scaleHeight  | 表示Drawable能够在高度上缩放的百分比, 比如: 50%,                            |
| scaleWidth   | 表示Drawable能够在宽度上缩放的百分比, 同上                                  |

## 九、clip - ClipDrawable：剪切的图样

| clip属性        | 含义                                                      |
| --------------- | --------------------------------------------------------- |
| drawable        | 资源引用                                                  |
| clipOrientation | top\bottom\left\right\gravity,fill... 横向，竖向，中间... |
| gravity         | left,right,top,bottom--哪个方向开始                       |

## 十、rotate - RotateDrawable：旋转图样

| clip属性    | 含义                                                                                          |
| ----------- | --------------------------------------------------------------------------------------------- |
| drawable    | 资源引用                                                                                      |
| visible     | 是否可见                                                                                      |
| pivotX      | pivotX表示旋转轴心在x轴横坐标上的位置，用百分比表示，表示在当前drawable总宽度百分之几的位置。 |
| pivotY      | 同上，Y轴方向的                                                                               |
| fromDegrees | 从什么角度开始                                                                                |
| toDegrees   | 到什么角度                                                                                    |

## 十一、animation-list - AnimationDrawable：动画效果图样

| animation-list属性 | 含义                   |
| ------------------ | ---------------------- |
| oneshot            | 是否一次，不然就是循环 |

## 十二、bitmap - BitmapDrawable

| xml属性   | 含义                                                                                                     | 代码                                                                                                                                                                                                                                                 |
| --------- | -------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| src       | 资源：color\drawable                                                                                     | -                                                                                                                                                                                                                                                    |
| antialias | 抗锯齿，建议开启                                                                                         | bitmapDrawable.setAntiAlias(true);                                                                                                                                                                                                                   |
| dither    | 颤抖处理：平滑，饱满，清晰，建议开启                                                                     | bitmapDrawable.setDither(true);                                                                                                                                                                                                                      |
| filter    | 位图过滤：平滑，建议开启                                                                                 | bitmapDrawable.setFilterBitmap(true);                                                                                                                                                                                                                |
| gravity   | 位置[start,end,top,bottom,center...]                                                                     | bitmapDrawable.setGravity(Gravity.START);                                                                                                                                                                                                            |
| mipMap    | 文理映射，建议关闭                                                                                       | bitmapDrawable.setMipMap(false);                                                                                                                                                                                                                     |
| tileMode  | 平铺方式``disabled-不使用平铺``clamp-复制边缘色彩``repeat-重复``mirror-镜像] | bitmapDrawable.setTileModeXY(Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);``bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);``bitmapDrawable.setTileModeXY(Shader.TileMode.MIRROR,Shader.TileMode.MIRROR); |

## 十三、nine-patch - NinePatchDrawable

这个是.9图，使用方法和上面bitmap类似，不赘述。好处是可以动态拉伸此图片某些边的时候而不造成整体变形。
