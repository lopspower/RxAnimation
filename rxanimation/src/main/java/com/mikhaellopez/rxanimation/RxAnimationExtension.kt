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
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null): Completable =
        Completable.create {
            animate(alpha,
                    translationX,
                    translationY,
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
                    action = action
            )
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