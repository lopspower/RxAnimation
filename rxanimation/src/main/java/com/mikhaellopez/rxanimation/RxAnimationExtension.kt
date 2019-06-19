package com.mikhaellopez.rxanimation

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.support.annotation.ColorInt
import android.view.View
import android.view.animation.Interpolator
import io.reactivex.Completable

//region DEFAULT
fun View.animateToCompletable(alpha: Float? = null,
                              translationX: Float? = null,
                              translationY: Float? = null,
                              scaleX: Float? = null,
                              scaleY: Float? = null,
                              rotation: Float? = null,
                              rotationX: Float? = null,
                              rotationY: Float? = null,
                              x: Float? = null,
                              y: Float? = null,
                              z: Float? = null,
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null): Completable =
        Completable.create {
            animate(alpha,
                    translationX,
                    translationY,
                    scaleX,
                    scaleY,
                    rotation,
                    rotationX,
                    rotationY,
                    x, y, z,
                    duration,
                    interpolator,
                    startDelay
            ) { it.onComplete() }
        }

fun ValueAnimator.startToCompletable(duration: Long? = null,
                                     action: (Any) -> Unit): Completable =
        Completable.create {
            start(duration,
                    animationEnd = { it.onComplete() },
                    action = action)
        }

fun Pair<Float, Float>.rangeFLoatToCompletable(duration: Long? = null,
                                               action: (Float) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofFloat(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Float) })
        }

fun Pair<Int, Int>.rangeIntToCompletable(duration: Long? = null,
                                         action: (Int) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofInt(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Int) })
        }
//endregion

//region ALPHA
fun View.setAlphaToCompletable(alpha: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Completable =
        animateToCompletable(
                alpha = alpha,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.fadeIn(duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null): Completable =
        animateToCompletable(
                alpha = 1f,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.fadeOut(duration: Long? = null,
                 interpolator: TimeInterpolator? = null,
                 startDelay: Long? = null): Completable =
        animateToCompletable(
                alpha = 0f,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)
//endregion

//region TRANSLATION
fun View.setTranslationToCompletable(translationX: Float,
                                     translationY: Float,
                                     duration: Long? = null,
                                     interpolator: TimeInterpolator? = null,
                                     startDelay: Long? = null): Completable =
        animateToCompletable(
                translationX = translationX,
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setTranslationXToCompletable(translationX: Float,
                                      duration: Long? = null,
                                      interpolator: TimeInterpolator? = null,
                                      startDelay: Long? = null): Completable =
        animateToCompletable(
                translationX = translationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setTranslationYToCompletable(translationY: Float,
                                      duration: Long? = null,
                                      interpolator: TimeInterpolator? = null,
                                      startDelay: Long? = null): Completable =
        animateToCompletable(
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)
//endregion

//region SCALE
fun View.setScaleToCompletable(scaleX: Float,
                               scaleY: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Completable =
        animateToCompletable(
                scaleX = scaleX,
                scaleY = scaleY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setScaleXToCompletable(scaleX: Float,
                                duration: Long? = null,
                                interpolator: TimeInterpolator? = null,
                                startDelay: Long? = null): Completable =
        animateToCompletable(
                scaleX = scaleX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setScaleYToCompletable(scaleY: Float,
                                duration: Long? = null,
                                interpolator: TimeInterpolator? = null,
                                startDelay: Long? = null): Completable =
        animateToCompletable(
                scaleY = scaleY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)
//endregion

//region ROTATION
fun View.setRotationToCompletable(rotation: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null): Completable =
        animateToCompletable(
                rotation = rotation,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setRotationXToCompletable(rotationX: Float,
                                   duration: Long? = null,
                                   interpolator: TimeInterpolator? = null,
                                   startDelay: Long? = null): Completable =
        animateToCompletable(
                rotationX = rotationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setRotationYToCompletable(rotationY: Float,
                                   duration: Long? = null,
                                   interpolator: TimeInterpolator? = null,
                                   startDelay: Long? = null): Completable =
        animateToCompletable(
                rotationY = rotationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)
//endregion

//region X, Y, Z
fun View.setXYZToCompletable(x: Float? = null,
                             y: Float? = null,
                             z: Float? = null,
                             duration: Long? = null,
                             interpolator: TimeInterpolator? = null,
                             startDelay: Long? = null): Completable =
        animateToCompletable(
                x = x,
                y = y,
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setXToCompletable(x: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                x = x,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setYToCompletable(y: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                y = y,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun View.setZToCompletable(z: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)
//endregion

//region RESIZE
fun View.resizeHeightToCompletable(fromHeight: Int, toHeight: Int,
                                   duration: Long? = null,
                                   interpolator: Interpolator? = null): Completable =
        Completable.create {
            resizeHeightWithAnimation(
                    fromHeight, toHeight,
                    duration,
                    interpolator
            ) { it.onComplete() }
        }
//endregion

//region COLOR
fun View.setBackgroundColorToCompletable(@ColorInt colorFrom: Int,
                                         @ColorInt colorTo: Int,
                                         duration: Long? = null,
                                         interpolator: Interpolator? = null): Completable =
        Completable.create {
            setBackgroundColorWithAnimation(
                    colorFrom,
                    colorTo,
                    duration,
                    interpolator
            ) { it.onComplete() }
        }
//endregion