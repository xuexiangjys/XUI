<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/BugF/IMG/2020/10/05/5f7b2469a6676.png" width="388" height="321" alt="Banner" />
</p>

# XUI

[![](https://jitpack.io/v/xuexiangjys/XUI.svg)](https://jitpack.io/#xuexiangjys/XUI)
[![api](https://img.shields.io/badge/API-17+-brightgreen.svg)](https://android-arsenal.com/api?level=17)
[![Issue](https://img.shields.io/github/issues/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XUI.svg)](https://github.com/xuexiangjys/XUI)

## [English](./README.md) | [Chinese](./README_ZH.md)

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

2.Then, in the dependencies of the project:

```
dependencies {
  ...
  //androidx project
  implementation 'com.github.xuexiangjys:XUI:1.1.6'

  implementation 'androidx.appcompat:appcompat:1.1.0'
  implementation 'androidx.recyclerview:recyclerview:1.1.0'
  implementation 'com.google.android.material:material:1.1.0'
  implementation 'com.github.bumptech.glide:glide:4.11.0'
}
```

【Note】 If your project does not currently use `androidx`, please use the following configuration:

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

## Demonstration

### Screenshot

![1_splash.png](https://img.rruu.net/image/5f7b65c2e3cce) &emsp; ![2_main.png](https://img.rruu.net/image/5f7b65c3073e5) &emsp; ![3_about.png](https://img.rruu.net/image/5f7b65c30ee6f) &emsp;

![4_menu.png](https://img.rruu.net/image/5f7b65c31862f) &emsp; ![5_dialog.png](https://img.rruu.net/image/5f7b65c31b2f3) &emsp; ![6_bottom_dialog.png](https://img.rruu.net/image/5f7b65c322c15) &emsp;

![7_picker.png](https://img.rruu.net/image/5f7b65c32e420) &emsp; ![8_webview.png](https://img.rruu.net/image/5f7b65c330c49) &emsp;![9_flowlayout.png](https://img.rruu.net/image/5f7b65c330c6a) &emsp;

![10_ninegrid.png](https://img.rruu.net/image/5f7b65c33da98) &emsp; ![11_radius_imageview.png](https://img.rruu.net/image/5f7d827d31fe4) &emsp; ![12_badge_view.png](https://img.rruu.net/image/5f7d827ccb45c) &emsp;

![13_tabview.png](https://img.rruu.net/image/5f7d827d0ddde) &emsp; ![14_citypicker.png](https://img.rruu.net/image/5f7d827d51eaf) &emsp; ![15_refresh_layout.png](https://img.rruu.net/image/5f7d827ddfb2e) &emsp;

![16_spinner.png](https://img.rruu.net/image/5f7d827dcd311) &emsp;

### Demo download

> The demo program is about 18M (mainly because the demo integrates a small video shooting library, which is about 13M, while XUI library is only 644k in size). The project is relatively large, and dandelion is recommended to download.

![xui_size.png](https://img.rruu.net/image/5f7d8400bf84f)

#### Pgyer Download

> Pgyer Download password: xuexiangjys

[![Pgyer](https://img.shields.io/badge/downloads-pgyer-blue.svg)](https://www.pgyer.com/XUIDemo)

![download_pugongying.png](https://img.rruu.net/image/5f7d827de5af4)

#### Github Download

[![Github](https://img.shields.io/badge/downloads-Github-blue.svg)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

![download_github.png](https://img.rruu.net/image/5f7d827d5d755)

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


## Sponsor

> Your support is the driving force of my maintenance. I will list the list of all the reward personnel at the bottom as the voucher. Please leave the notes of the support items before rewarding!

![pay.png](https://img.rruu.net/image/5f871d00045da)

Thank you for your sponsorship：

Name | Money | Platform
:-|:-|:-
C*y | 1￥ | WeChat
*流 | 1￥ | WeChat
*声 | 50￥ | WeChat
*宇涛 | 5￥ | Alipay
*事 | 10￥ | WeChat
优*1 | 168￥ | WeChat
*、 | 20￥ | WeChat
*钰晗 | 6￥ | Alipay
*娜 | 3￥ | WeChat
*米 | 20￥ | WeChat
*忘 | 10￥ | WeChat
*清红 | 1￥ | Alipay
*口 | 5￥ | WeChat
\* | 10.24￥ | WeChat
*俊耀 | 100￥ | Alipay
*俊杰 | 1￥ | Alipay
*鸥 | 10.24￥ | WeChat
*云 | 20.21￥ | Alipay
*钰晗 | 66￥ | Alipay
*杰柱 | 10￥ | Alipay
*毛 | 6.66￥ | WeChat
*凯 | 10￥ | WeChat
r*o | 8.88￥ | WeChat
T*8 | 7.77￥ | WeChat
v*d | 20￥ | WeChat
B*G | 1￥ | WeChat
*舞 | 10￥ | WeChat
*肉 | 2￥ | WeChat
*拖 | 12.12￥ | WeChat
*鱼 | 20￥ | WeChat
*明 | 20￥ | WeChat
*化 | 8￥ | WeChat
*攀 | 16.80￥ | Alipay
**航 | 10￥ | Alipay
**飞 | 10.24￥ | Alipay
*瑟 | 1￥ | WeChat
*原 | 10.24￥ | Alipay
*越 | 10.24￥ | WeChat
**俊 | 80￥ | Alipay
*尋 | 10.24￥ | WeChat
爱生活 | 100￥ | QQ

## Contact

[![](https://img.shields.io/badge/XUIGroup1-695048677-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=a2ab505862c81f1528416b585832022e835ce0abe28eefa4b0d53f8094a5691d)

[![](https://img.shields.io/badge/XUIGroup2-700246750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=39497f13d5e456d219be785361a282d2d9c8cd9ba7745f6170def9d90643e164)

![gzh_weixin.jpg](https://img.rruu.net/image/5f871cfff3194)
