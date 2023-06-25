<p align="center">
  <img src="https://raw.githubusercontent.com/xuexiangjys/XUI/master/art/app_logo_xui.png" width="388" height="321" alt="Banner" />
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

| WeChat public number   | juejin     |  zhihu    |  CSDN   |   jianshu   |   segmentfault  |   bilibili  |   toutiao
|---------|---------|--------- |---------|---------|---------|---------|---------|
| [我的Android开源之旅](https://t.1yb.co/Irse)  |  [Click me](https://juejin.im/user/598feef55188257d592e56ed/posts)    |   [Click me](https://www.zhihu.com/people/xuexiangjys/posts)       |   [Click me](https://xuexiangjys.blog.csdn.net/)  |   [Click me](https://www.jianshu.com/u/6bf605575337)  |   [Click me](https://segmentfault.com/u/xuexiangjys)  |   [Click me](https://space.bilibili.com/483850585)  |   [Click me](https://img.rruu.net/image/5ff34ff7b02dd)

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

## Stargazers over time

[![Stargazers over time](https://starchart.cc/xuexiangjys/XUI.svg)](https://starchart.cc/xuexiangjys/XUI)

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
  implementation 'com.github.xuexiangjys:XUI:1.2.1'

  implementation 'androidx.appcompat:appcompat:1.3.1'
  implementation 'androidx.recyclerview:recyclerview:1.2.1'
  implementation 'com.google.android.material:material:1.4.0'
  implementation 'com.github.bumptech.glide:glide:4.12.0'
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

----

## Demonstration

### Screenshot

![1_splash.png](./art/1_splash.png) &emsp; ![2_main.png](./art/2_main.png) &emsp; ![3_about.png](./art/3_about.png) &emsp;

![4_menu.png](./art/4_menu.png) &emsp; ![5_dialog.png](./art/5_dialog.png) &emsp; ![6_bottom_dialog.png](./art/6_bottom_dialog.png) &emsp;

![7_picker.png](./art/7_picker.png) &emsp; ![8_webview.png](./art/8_webview.png) &emsp;![9_flowlayout.png](./art/9_flowlayout.png) &emsp;

![10_ninegrid.png](./art/10_ninegrid.png) &emsp; ![11_radius_imageview.png](./art/11_radius_imageview.png) &emsp; ![12_badge_view.png](./art/12_badge_view.png) &emsp;

![13_tabview.png](./art/13_tabview.png) &emsp; ![14_citypicker.png](./art/14_citypicker.png) &emsp; ![15_refresh_layout.png](./art/15_refresh_layout.png) &emsp;

![16_spinner.png](./art/16_spinner.png) &emsp;

### Demo download

> The demo program is about 18M (mainly because the demo integrates a small video shooting library, which is about 13M, while XUI library is only 644k in size). The project is relatively large, and dandelion is recommended to download.

![xui_size.png](./art/xui_size.png)

#### Pgyer Download

> Pgyer Download password: xuexiangjys

[![Pgyer](https://img.shields.io/badge/downloads-pgyer-blue.svg)](https://www.pgyer.com/XUIDemo)

[![download_pugongying.png](./art/download_pugongying.png)](https://www.pgyer.com/XUIDemo)

#### Github Download

[![Github](https://img.shields.io/badge/downloads-Github-blue.svg)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

[![download_github.png](./art/download_github.png)](https://github.com/xuexiangjys/XUI/blob/master/apk/xuidemo.apk?raw=true)

## Contribution

> Due to my limited energy, you are welcome to actively contribute your idea. You will have the opportunity to participate in the maintenance of star over 1000 projects on GitHub and enhance your industry influence!

Code contribution requirements：

* Please keep the existing code style, not according to your habits. Please comply with Alibaba java coding specification.

* Just modify the code you are sure need to be optimized, not all the different code from your ideas.

* Before launching a pull request, you should test your commit code adequately.

* Please commit new code to the dev branch instead of the master branch.

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

![pay.png](https://raw.githubusercontent.com/xuexiangjys/Resource/master/img/pay/pay.png)

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
*茶 | 100￥ | WeChat
*头 | 2￥ | WeChat
*噜 | 10.99￥ | WeChat
*W*m | 10￥ | WeChat
*谷 | 10￥ | WeChat
*望 | 5￥ | WeChat
J*o | 10.24￥ | WeChat
*休 | 10.24￥ | WeChat
**俊 | 80￥ | Alipay
**伟 | 1.1￥ | Alipay
**云 | 5￥ | Alipay
*航 | 3￥ | Alipay
*维 | 5￥ | WeChat
*鑫 | 188.88￥ | Alipay
*玉 | 10￥ | Alipay
**贺 | 100￥ | Alipay
**伟 | 65￥ | Alipay
*G | 1￥ | WeChat
M*u | 10.24￥ | WeChat
S*m | 10.24￥ | WeChat
T*g | 15￥ | WeChat
*边 | 10.24￥ | WeChat
*寻 | 20.48￥ | WeChat
*凉 | 10.24￥ | WeChat
S*y | 10.24￥ | WeChat
M*n | 1￥ | WeChat
J*e | 10.24￥ | WeChat
*、 | 10.24￥ | WeChat
禹*） | 1￥ | WeChat
X*？ | 18.88￥ | WeChat
*事 | 5￥ | WeChat
*之 | 10￥ | WeChat
*安 | 18.88￥ | WeChat
*🎵 | 10.24￥ | WeChat
*👔 | 10￥ | WeChat
*洲 | 10￥ | WeChat

## Contact

[![](https://img.shields.io/badge/XUIGroup1-695048677-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=a2ab505862c81f1528416b585832022e835ce0abe28eefa4b0d53f8094a5691d)

[![](https://img.shields.io/badge/XUIGroup2-700246750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=39497f13d5e456d219be785361a282d2d9c8cd9ba7745f6170def9d90643e164)

[![](https://img.shields.io/badge/XUIGroup3-1090612354-blue.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=nOY3GGJY-jiwzhQpR8E06G-yrOUsxCP1)

![](https://s1.ax1x.com/2022/04/27/LbGMJH.jpg)