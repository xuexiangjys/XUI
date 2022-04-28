
# XUI

[![](https://jitpack.io/v/xuexiangjys/XUI.svg)](https://jitpack.io/#xuexiangjys/XUI)
[![api](https://img.shields.io/badge/API-17+-brightgreen.svg)](https://android-arsenal.com/api?level=17)
[![Issue](https://img.shields.io/github/issues/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI)

## [English](https://github.com/xuexiangjys/XUI/blob/master/README.md) | [Chinese](https://github.com/xuexiangjys/XUI/blob/master/README_ZH.md)

A simple and elegant Android native UI framework, free your hands! Click on the [instruction document](https://github.com/xuexiangjys/XUI/wiki) and experience it!

> Covers most of the UI components：TextView、Button、EditText、ImageView、Spinner、Picker、Dialog、PopupWindow、ProgressBar、LoadingView、StateLayout、FlowLayout、Switch、Actionbar、TabBar、Banner、GuideView、BadgeView、MarqueeView、WebView、SearchView...etc. A series of components and colorful style themes。

Please read [【wisdom of asking questions】](https://xuexiangjys.blog.csdn.net/article/details/83344235) before raising the issue and strictly follow the [issue template](https://github.com/xuexiangjys/XUI/issues/new/choose) fill in and save everyone's time.

Please read the [instruction document](https://github.com/xuexiangjys/XUI/wiki) carefully before use, important things are to be repeated for three time!!！

please read the [instruction document](https://github.com/xuexiangjys/XUI/wiki) carefully before use, important things are to be repeated for three time!!！

Please read the [instruction document](https://github.com/xuexiangjys/XUI/wiki) carefully before use, important things are to be repeated for three time!!！

## About me

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![简书](https://img.shields.io/badge/简书-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![掘金](https://img.shields.io/badge/掘金-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![知乎](https://img.shields.io/badge/知乎-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys)

## Rapid integration of X-Library

In order to facilitate the rapid integration of X-Library, I provide a template project for your reference: [https://github.com/xuexiangjys/TemplateAppProject](https://github.com/xuexiangjys/TemplateAppProject)

In addition, I have also produced several [video tutorials](https://space.bilibili.com/483850585/channel/detail?cid=104998) for your reference.

----

## Features

* Simple and elegant，the total size of the project library is less than 1M (about 644k after packaging).

* Rich components，provides the vast majority of our developers in common functional components.

* Easy to use，in order to facilitate rapid development and improve development efficiency, the API is optimized to provide one click access.

* Uniform style，the framework provides a series of unified styles to make the UI look beautiful and harmonious.

* High compatibility，the framework also provides three different sizes of device styles (4.5 inch, 7 inch and 10 inch) and is compatible with Android 17 at least, which makes UI compatibility stronger.

* Strong expansibility，each component provides rich properties and style APIs, and different styles of UI can be built by setting different style properties.

----

## Usage

> Before you consider using XUI, you must be clear that this framework provides a whole set of UI solutions. If you just want to use a few of them, you don't need to introduce such a huge UI library. You can find better component libraries on GitHub. If you want to have a customized, unified UI overall solution, then you can continue to look!

### Add gradle dependency

1.In the project root directory `build.gradle`:

```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.Then add in the dependencies of `build.gradle` of the application project (usually app):

```
dependencies {
  ...
  //androidx project
  implementation 'com.github.xuexiangjys:XUI:1.1.9'

  implementation 'androidx.appcompat:appcompat:1.3.1'
  implementation 'androidx.recyclerview:recyclerview:1.2.1'
  implementation 'com.google.android.material:material:1.4.0'
  implementation 'com.github.bumptech.glide:glide:4.12.0'
}
```

【Note】If your project does not currently use `androidx`, please use the following configuration:

```
dependencies {
  ...
  //support project
  implementation 'com.github.xuexiangjys:XUI:1.0.9-support'

  implementation 'com.android.support:appcompat-v7:28.0.0'
  implementation 'com.android.support:recyclerview-v7:28.0.0'
  implementation 'com.android.support:design:28.0.0'
  implementation 'com.github.bumptech.glide:glide:4.8.0'
}
```

### Initialization

1.Modify the basic theme of the application (required)

> The basic theme of the application must be set, otherwise the component will not work normally! It is very important to ensure that the theme of all windows using XUI components is a subclass of `XUITheme`!!!

Basic topic type：

* Large flat plate(10 inches, 240dpi, 1920*1200）：XUITheme.Tablet.Big

* Small plate(7 inches, 320dpi, 1920*1200）：XUITheme.Tablet.Small

* Mobile phone（4.5 inches, 320dpi, 720*1280）：XUITheme.Phone

```
<style name="AppTheme" parent="XUITheme.Phone">
     <!-- Customize your own theme style --> 
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
 </style>

```

Of course, you can also call the following code at the beginning of the `Activity` to set the theme dynamically.

```
@Override
protected void onCreate(Bundle savedInstanceState) {
    XUI.initTheme(this);
    super.onCreate(savedInstanceState);
    ...
}
```

2.Adjust font library (omit if there is no requirement for font)

（1）Set the font library path you need to modify (under assets)

```
// Set the default font to Chinese line Kai, write your font library here
XUI.getInstance().initFontStyle("fonts/hwxk.ttf");
```

（2）Add the following code to the basic activity of the project to inject fonts

Note: after version 1.1.4, use the following settings for injection

```
@Override
protected void attachBaseContext(Context newBase) {
    // Injection font
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
}
```

Note: versions 1.1.3 and earlier use the following settings for injection

```
@Override
protected void attachBaseContext(Context newBase) {
    // Injection font
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
}
```

## Proguard

```
-keep class com.xuexiang.xui.widget.edittext.materialedittext.** { *; }

```

----
### Demo download

> The demo program is about 18M (mainly because the demo integrates a small video shooting library, which is about 13M, while XUI library is only 644k in size). The project is relatively large, and dandelion is recommended to download.

![xui_size.png](./art/xui_size.png)

#### Pgyer Download

> Pgyer Download password: xuexiangjys

[![Pgyer](https://img.shields.io/badge/downloads-pgyer-blue.svg)](https://www.pgyer.com/XUIDemo)

[![download_pugongying.png](./art/download_pugongying.png)](https://www.pgyer.com/XUIDemo)

#### Github Download

[![Github](https://img.shields.io/badge/downloads-Github-blue.svg)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

![download_github.png](./art/download_github.png)

## Thanks

* [QMUI_Android](https://github.com/Tencent/QMUI_Android)
* [AgentWeb](https://github.com/Justson/AgentWeb)
* [Android-Iconics](https://github.com/mikepenz/Android-Iconics)
* [Android-PickerView](https://github.com/Bigkoo/Android-PickerView)
* [CityPicker](https://github.com/xuexiangjys/CityPicker)
* [ELinkageScroll](https://github.com/MFC-TEC/ELinkageScroll)
* [FlycoBanner_Master](https://github.com/H07000223/FlycoBanner_Master)
* [Linkage-RecyclerView](https://github.com/KunMinX/Linkage-RecyclerView)
* [MaterialEditText](https://github.com/rengwuxian/MaterialEditText)
* [MaterialSpinner](https://github.com/jaredrummler/MaterialSpinner)
* [MaterialProgressBar](https://github.com/DreaminginCodeZH/MaterialProgressBar)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [PictureSelector](https://github.com/LuckSiege/PictureSelector)
* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
* [SlideBack](https://github.com/ParfoisMeng/SlideBack)
* [SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)

## Contact

[![](https://img.shields.io/badge/XUI开源交流群-695048677-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=a2ab505862c81f1528416b585832022e835ce0abe28eefa4b0d53f8094a5691d)

[![](https://img.shields.io/badge/XUI开源交流2群-700246750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=39497f13d5e456d219be785361a282d2d9c8cd9ba7745f6170def9d90643e164)

[![](https://img.shields.io/badge/XUI开源交流3群-1090612354-blue.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=nOY3GGJY-jiwzhQpR8E06G-yrOUsxCP1)

![](https://s1.ax1x.com/2022/04/27/LbGMJH.jpg)