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
implementation 'com.mikhaellopez:rxanimation:0.0.1'
```

KOTLIN
-----

- Animate your views and handle it in [Completable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Completable.html). For example **`setAlphaToCompletable()`** and **`setTranslationXToCompletable()`**:

```kotlin
view1.setAlphaToCompletable(1f)
        .andThen(view2.setTranslationXToCompletable(300f))
```

- If you want to apply animation in the same time you can used **`RxAnimation.together()`**:

```kotlin
RxAnimation.together(
        view1.setAlphaToCompletable(1f),
        view1.setTranslationToCompletable(300f, 500f),
        view2.setBackgroundColorToCompletable(
                ContextCompat.getColor(this, R.color.accent),
                ContextCompat.getColor(this, R.color.primary)),
        view2.resizeToCompletable(200, 200))
```

- If you want to apply animation one by one you can used **`RxAnimation.sequentially()`** instead of multi `andThen()`:

```kotlin
RxAnimation.sequentially(
        view1.setAlphaToCompletable(1f),
        view1.setTranslationToCompletable(300f, 500f),
        view2.setBackgroundColorToCompletable(
                ContextCompat.getColor(this, R.color.accent),
                ContextCompat.getColor(this, R.color.primary)),
        view2.resizeToCompletable(200, 200))
```

- You can also used **`RxAnimation.fromView(view)`** if you want to update multi properties in the same view:

```kotlin
RxAnimation.fromView(view)
        .fadeIn()
        .setTranslation(300f, 500f)
        .setBackgroundColor(
                ContextCompat.getColor(this, R.color.accent),
                ContextCompat.getColor(this, R.color.primary))
        .resize(200, 200)
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

LICENCE
-----

RxAnimation by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
