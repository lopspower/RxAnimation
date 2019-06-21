RxAnimation
=================

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
<br>
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android library to make a simple way to animate your views on Android with Rx.

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

Add RxAnimation library via Gradle:

```groovy
implementation 'com.mikhaellopez:rxanimation:0.0.2'
```

KOTLIN
-----

<img src="/preview/0.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- Animate your views and handle it in [Completable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Completable.html). For example **`setAlphaToCompletable()`** and **`resizeToCompletable()`**:

```kotlin
view1.setAlphaToCompletable(1f)
    .andThen(view2.resizeToCompletable(100, 100))
```

<br/>

<img src="/preview/1.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- If you want to apply animation in the same time you can used **`RxAnimation.together()`**:

```kotlin
RxAnimation.together(
    view1.setAlphaToCompletable(1f),
    view1.setTranslationToCompletable(20f, 30f),
    view2.setBackgroundColorToCompletable(
        ContextCompat.getColor(this, R.color.accent),
        ContextCompat.getColor(this, R.color.primary)
    ),
    view2.resizeToCompletable(100, 100)
)
```

<img src="/preview/2.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- If you want to apply animation one by one you can used **`RxAnimation.sequentially()`** instead of multi `andThen()`:

```kotlin
RxAnimation.sequentially(
    view1.setAlphaToCompletable(1f),
    view1.setTranslationToCompletable(20f, 30f),
    view2.setBackgroundColorToCompletable(
        ContextCompat.getColor(this, R.color.accent),
        ContextCompat.getColor(this, R.color.primary)
    ),
    view2.resizeToCompletable(100, 100)
)
```

<img src="/preview/3.gif" alt="sample" title="sample" width="250" height="160" align="right" />

- You can also used **`RxAnimation.fromView(view)`** if you want to update multi properties one by one in the same view:

```kotlin
RxAnimation.fromView(view)
    .fadeIn()
    .setTranslation(20f, 30f)
    .setBackgroundColor(
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

RxAnimation.fromView(circularImageView)
    .rangeFloat(4f, 20f) { circularImageView.borderWidth = it }
```


ALL PROPERTIES
-----

Properties | View | Completable | RxAnimation.fromView(view)
------------ | ------------ | ------------- | -------------
alpha | animate(alpha = value) | setAlphaToCompletable | setAlpha
translationX | animate(translationX = value) | setTranslationXToCompletable | setTranslationX
translationY | animate(translationY = value) | setTranslationYToCompletable | setTranslationY
scaleX | animate(scaleX = value) | setScaleXToCompletable | setScaleX
scaleY | animate(scaleY = value) | setScaleYToCompletable | setScaleY
rotation | animate(rotation = value) | setRotationToCompletable | setRotation
rotationX | animate(rotationX = value) | setRotationXToCompletable | setRotationX
rotationY | animate(rotationY = value) | setRotationYToCompletable | setRotationY
X | animate(X = value) | setXToCompletable | setX
Y | animate(Y = value) | setYToCompletable | setY
Z | animate(Z = value) | setZToCompletable | setZ
backgroundColor | setBackgroundColorWithAnimation | setBackgroundColorToCompletable | setBackgroundColor
width | setWidth | setWidthToCompletable | setWidth
height | setHeight | setHeightToCompletable | setHeight
resize | - | resizeToCompletable | resize
customProperties | - | rangeFloatToCompletable | rangeFloat
customProperties | - | rangeIntToCompletable | rangeInt
customProperties | - | rangeIntToCompletable | rangeInt
ValueAnimator | start | startToCompletable | startValueAnimator

LICENCE
-----

RxAnimation by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
