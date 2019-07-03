RxAnimation
=================

<img src="/preview/preview.gif" alt="preview" title="preview" width="350" height="215,25" align="right" />

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxAnimation-lightgrey.svg?style=flat)](https://android-arsenal.com/details/1/7736)
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android library to make a simple way to animate your views on Android with Rx.

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

Add RxAnimation library via Gradle:

```groovy
implementation 'com.mikhaellopez:rxanimation:0.0.3'
```

KOTLIN
-----

<img src="/preview/0.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- Animate your views and handle it in [Completable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Completable.html). For example **`alpha()`** and **`resize()`**:

```kotlin
view1.alpha(1f)
    .andThen(view2.resize(100, 100))
```

<br/>

<img src="/preview/1.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- If you want to apply animation in the same time you can used **`RxAnimation.together()`**:

```kotlin
RxAnimation.together(
    view1.fadeIn(),
    view1.translation(20f, 30f),
    view2.backgroundColor(
        ContextCompat.getColor(this, R.color.accent),
        ContextCompat.getColor(this, R.color.primary)
    ),
    view2.resize(100, 100)
)
```

<img src="/preview/2.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- If you want to apply animation one by one you can used **`RxAnimation.sequentially()`** instead of multi `andThen()`:

```kotlin
RxAnimation.sequentially(
    view1.fadeIn(),
    view1.translation(20f, 30f),
    view2.backgroundColor(
        ContextCompat.getColor(this, R.color.accent),
        ContextCompat.getColor(this, R.color.primary)
    ),
    view2.resize(100, 100)
)
```

<img src="/preview/3.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- You can also used **`RxAnimation.from(view)`** if you want to update multi properties one by one in the same view:

```kotlin
RxAnimation.from(view)
    .fadeIn()
    .translation(20f, 30f)
    .backgroundColor(
        ContextCompat.getColor(this, R.color.accent),
        ContextCompat.getColor(this, R.color.primary)
    )
    .resize(100, 100)
```

<img src="/preview/4.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- You can also use the **`range()`** function to animate a change on a custom property:

```kotlin
(4f, 20f).rangeFloatToCompletable { 
    circularImageView.borderWidth = it 
}

// or

RxAnimation.from(circularImageView)
    .rangeFloat(4f, 20f) { circularImageView.borderWidth = it }
```


ALL PROPERTIES
-----

Properties | View to Completable | RxAnimation.from(view)
------------ | ------------ | -------------
alpha | alpha | alpha
alpha=1 | fadeIn | fadeIn
alpha=0 | fadeOut | fadeOut
translationX | translationX | translationX
translationY | translationY | translationY
translationX + Y | translation(X, Y) | translation(X, Y)
scaleX | scaleX | scaleX
scaleY | scaleY | scaleY
rotation | rotation | rotation
rotationX | rotationX | rotationX
rotationY | rotationY | rotationY
X | x | x
Y | y | y
Z | z | z
X + Y + Z | xyz | xyz
backgroundColor | backgroundColor | backgroundColor
width | width | width
height | height | height
width + height | resize | resize
customProperties | rangeFloatToCompletable | rangeFloat
customProperties | rangeIntToCompletable | rangeInt
customProperties | rangeIntToCompletable | rangeInt
ValueAnimator | start | startValueAnimator
ViewPropertyAnimator | animate | -
shake | shake | shake

:information_source: All the functions have `duration: Long`, `interpolator: TimeInterpolator` & `startDelay: Long` properties.

LICENCE
-----

RxAnimation by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
