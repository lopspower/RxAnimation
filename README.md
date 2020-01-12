<p align="center"><img src="/preview/header.png"></p>

<img src="/preview/title.gif" alt="title" title="title" width="300" height="87.8" />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Download](https://api.bintray.com/packages/lopspower/maven/com.mikhaellopez:rxanimation/images/download.svg?version=0.0.7)](https://bintray.com/lopspower/maven/com.mikhaellopez:rxanimation/0.0.7/link)
<br>
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c793e3842c79477399967e2443c76990)](https://app.codacy.com/app/lopspower/RxAnimation?utm_source=github.com&utm_medium=referral&utm_content=lopspower/CircleView&utm_campaign=Badge_Grade_Dashboard)

This is an Android library to make a simple way to animate your views on Android with Rx.

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

Add RxAnimation library via Gradle:

```groovy
implementation 'com.mikhaellopez:rxanimation:0.0.7'
```

KOTLIN
-----

<img src="/preview/0.gif" alt="sample" title="sample" width="250" height="160" align="right" />

-   Animate your views and handle it in [Completable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Completable.html). For example **`alpha()`** and **`resize()`**:

```kotlin
view1.alpha(1f)
    .andThen(view2.resize(100, 100))
```

<br/>

<img src="/preview/1.gif" alt="sample" title="sample" width="250" height="160" align="right" />

-   If you want to apply animation in the same time you can used **`RxAnimation.together()`**:

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

-   If you want to apply animation one by one you can used **`RxAnimation.sequentially()`** instead of multi `andThen()`:

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

-   You can also used **`RxAnimation.from(view)`** if you want to update multi properties one by one in the same view:

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

-   You can also use the **`range()`** function to animate a change on a custom property:

```kotlin
(4f to 20f).rangeFloatToCompletable { 
    circularImageView.borderWidth = it 
}

// or

RxAnimation.from(circularImageView)
    .rangeFloat(4f, 20f) { circularImageView?.borderWidth = it }
```

-   Use `reverse` properties to back to the initial value in all methods:

```kotlin
view.fadeIn(reverse = true)
```

-   If you want to repeat an animation you can use the native method [`repeat`](http://reactivex.io/documentation/operators/repeat.html) from Rx like this:

```kotlin
RxAnimation.from(view)
    .fadeIn()
    .shake()
    .fadeOut()
    .repeat(NB_REPEAT)
    .subscribe()
```

ALL PROPERTIES
-----

### Default

| Properties           | View to Completable | RxAnimation.from(view) |
| -------------------- | ------------------- | ---------------------- |
| alpha                | alpha               | alpha                  |
| translationX         | translationX        | translationX           |
| translationY         | translationY        | translationY           |
| translation X + Y    | translation(X, Y)   | translation(X, Y)      |
| scaleX               | scaleX              | scaleX                 |
| scaleY               | scaleY              | scaleY                 |
| scale X = Y          | scale               | scale                  |
| rotation             | rotation            | rotation               |
| rotationX            | rotationX           | rotationX              |
| rotationY            | rotationY           | rotationY              |
| X                    | x                   | x                      |
| Y                    | y                   | y                      |
| Z                    | z                   | z                      |
| X + Y + Z            | xyz                 | xyz                    |
| backgroundColor      | backgroundColor     | backgroundColor        |
| width                | width               | width                  |
| height               | height              | height                 |
| width + height       | resize              | resize                 |
| ValueAnimator        | start               | startValueAnimator     |
| ViewPropertyAnimator | animate             | -                      |

### Custom Properties

| View to Completable     | RxAnimation.from(view) |
| ----------------------- | ---------------------- |
| rangeFloatToCompletable | rangeFloat             |
| rangeIntToCompletable   | rangeInt               |

### Smart function

| Animation | View to Completable | RxAnimation.from(view) |
| --------- | ------------------- | ---------------------- |
| alpha=1f  | fadeIn              | fadeIn                 |
| alpha=0f  | fadeOut             | fadeOut                |
| shake     | shake               | shake                  |
| press     | press               | press                  |
| text      | text                | text                   |

:information_source: All the functions have `duration: Long`, `interpolator: TimeInterpolator`, `startDelay: Long` and `reverse: Boolean` properties.

SUPPORT ❤️
-----

Find this library useful? Support it by joining [**stargazers**](https://github.com/lopspower/RxAnimation/stargazers) for this repository ⭐️
<br/>
And [**follow me**](https://github.com/lopspower?tab=followers) for my next creations 👍

LICENCE
-----

RxAnimation by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
