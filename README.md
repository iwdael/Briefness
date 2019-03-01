# Briefness
[![](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/language-java-yellow.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/Jcenter-2.0.5-brightgreen.svg)](https://jcenter.bintray.com/com/hacknife/briefness/) [![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/license-apache--2.0-green.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/api-11+-green.svg)](https://github.com/hacknife)<br/><br/>
数据绑定、布局绑定、控件绑定、事件绑定、数据异常预处理、开发更加简单。
## 特点
* 支持MVVM(Model-View-ViewModel)
* 支持任意数据类型绑定(包含自定义控件)
* 支持任意类绑定(Activity、Fragment、Dialog、View 如果有需要普通类也可以)
* 规避空指针引发的异常(防止项目开发中，数据异常引起的空指针)
* 支持View绑定(我不建议你这样做，因为根本没有必要)
* 支持点击事件双向绑定(Activity中绑、layout中也可以)
* 支持长按点击事件绑定
* 支持library、application
* 支持布局绑定
* 支持简单比较运算
## 概述
Briefness能够避免在项目开发中一些重复繁琐的工作，规避因数据异常引发的空指针异常，提高开发效率。它量身为[MVVM](https://baike.baidu.com/item/MVVM)框架打造，实现UI逻辑与业务分离，解耦，提高可重用性。
它提供了3种注解方式，9种xml属性完成数据绑定，事件绑定等操作。

|属性|功能|控件|
|:------:|:------:|:------:|
|imports|引入需要绑定的类|root view|
|viewModel|引入Viewmodel|root view|
|bind|绑定数据|view|
|click|点击监听|view|
|longClick|长按监听|view| 
|textChanged|文本变化监听|TextView|
|checkChanged|选择变化监听|CheckBox|
|tabSelected|选择变化监听|TabLayout|
|tabUnselected|选择变化监听|TabLayout|
|pageSelected|选择变化监听|ViewPager|
|progressChanged|进度变化监听|SeekBar|
|radioChanged|选择变化监听|RadioButton|
|Immersive|选择变化监听|Activity|

|注解|功能|
|:------:|:------:|
|BindLayout|绑定布局，并解析布局文件|
|BindView|绑定控件|
|BindClick|绑定事件|
## 比较
||注解控件|注解方法|注解布局|数据绑定|任意绑定|事件传递|学习成本|
|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:------:|
|Briefness|支持|支持|支持|支持|支持|支持|低|
|Databinding|不支持|不支持|不支持|支持|不支持|支持|高|
|ButterKnife|支持|支持|不支持|不支持|不支持|不支持|低|
|XUtil|支持|支持|不支持|不支持|不支持|不支持|中|
## 使用说明
1. 在使用Briefness提供的功能之前，必须调用Briefness.bind方法，该方法会返回Briefenssor的一个实现类。

2. 在绑定和引入类中，如果你需要绑定多个数据，或者引入多个类，请使用“|”符号隔开。

3. "$"符号必须是成对使用的，使用"$"引起来的变量表示是需要变换的。

4. [注解使用说明](https://github.com/hacknife/briefness/blob/master/doc/tutorial_annotation.md)

5. [XML属性标签使用说明](https://github.com/hacknife/briefness/blob/master/doc/tutorial_xml.md)

6. [MVVM的使用与框架搭建](https://www.jianshu.com/p/2f60736b6900)
## 快速引入项目
合并以下代码到需要使用的Module的dependencies中。
```Java
	dependencies {
                ...
              implementation 'com.hacknife.briefness:briefness:2.0.5'
    	      annotationProcessor 'com.hacknife.briefness:briefness-compiler:2.0.5'
	}
```
## 混淆
```Java
-keep class * implements com.hacknife.briefness.Briefnessor{*;}
-keep @com.hacknife.briefness.BindLayout class *
-keep public interface com.hacknife.briefness.Briefnessor{public *;}
-keep class **.BriefnessInjector {
    *;
}
-keep class **.ViewInjector {
     *;
}
```
<br><br><br>
## 感谢浏览
请不要吝啬你的小星星，如果你有任何疑问，请加入QQ群，我将竭诚为你解答。
<br>
![Image Text](https://github.com/hacknife/CarouselBanner/blob/master/qq_group.png)
