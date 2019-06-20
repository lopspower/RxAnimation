package com.mikhaellopez.rxanimation

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.support.annotation.ColorInt
import android.view.View
import android.view.animation.Interpolator
import io.reactivex.Completable
import io.reactivex.Observable

//region PRIVATE UTILS
private fun Observable<View>.doCompletable(actionCompletable: (View) -> Completable): Observable<View> =
        flatMap { actionCompletable(it).toSingleDefault(it).toObservable() }
//endregion

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

fun Observable<View>.setAlpha(alpha: Float,
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null): Observable<View> =
        doCompletable { it.setAlphaToCompletable(alpha, duration, interpolator, startDelay) }

fun View.fadeIn(duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null): Completable =
        animateToCompletable(
                alpha = 1f,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.fadeIn(duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null): Observable<View> =
        doCompletable { it.fadeIn(duration, interpolator, startDelay) }

fun View.fadeOut(duration: Long? = null,
                 interpolator: TimeInterpolator? = null,
                 startDelay: Long? = null): Completable =
        animateToCompletable(
                alpha = 0f,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.fadeOut(duration: Long? = null,
                             interpolator: TimeInterpolator? = null,
                             startDelay: Long? = null): Observable<View> =
        doCompletable { it.fadeOut(duration, interpolator, startDelay) }
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

fun Observable<View>.setTranslation(translationX: Float,
                                    translationY: Float,
                                    duration: Long? = null,
                                    interpolator: TimeInterpolator? = null,
                                    startDelay: Long? = null): Observable<View> =
        doCompletable { it.setTranslationToCompletable(translationX, translationY, duration, interpolator, startDelay) }

fun View.setTranslationXToCompletable(translationX: Float,
                                      duration: Long? = null,
                                      interpolator: TimeInterpolator? = null,
                                      startDelay: Long? = null): Completable =
        animateToCompletable(
                translationX = translationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setTranslationX(translationX: Float,
                                     duration: Long? = null,
                                     interpolator: TimeInterpolator? = null,
                                     startDelay: Long? = null): Observable<View> =
        doCompletable { it.setTranslationXToCompletable(translationX, duration, interpolator, startDelay) }

fun View.setTranslationYToCompletable(translationY: Float,
                                      duration: Long? = null,
                                      interpolator: TimeInterpolator? = null,
                                      startDelay: Long? = null): Completable =
        animateToCompletable(
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setTranslationY(translationY: Float,
                                     duration: Long? = null,
                                     interpolator: TimeInterpolator? = null,
                                     startDelay: Long? = null): Observable<View> =
        doCompletable { it.setTranslationYToCompletable(translationY, duration, interpolator, startDelay) }
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

fun Observable<View>.setScale(scaleX: Float,
                              scaleY: Float,
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null): Observable<View> =
        doCompletable { it.setScaleToCompletable(scaleX, scaleY, duration, interpolator, startDelay) }

fun View.setScaleXToCompletable(scaleX: Float,
                                duration: Long? = null,
                                interpolator: TimeInterpolator? = null,
                                startDelay: Long? = null): Completable =
        animateToCompletable(
                scaleX = scaleX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setScaleX(scaleX: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Observable<View> =
        doCompletable { it.setScaleXToCompletable(scaleX, duration, interpolator, startDelay) }

fun View.setScaleYToCompletable(scaleY: Float,
                                duration: Long? = null,
                                interpolator: TimeInterpolator? = null,
                                startDelay: Long? = null): Completable =
        animateToCompletable(
                scaleY = scaleY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setScaleY(scaleY: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Observable<View> =
        doCompletable { it.setScaleYToCompletable(scaleY, duration, interpolator, startDelay) }
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

fun Observable<View>.setRotation(rotation: Float,
                                 duration: Long? = null,
                                 interpolator: TimeInterpolator? = null,
                                 startDelay: Long? = null): Observable<View> =
        doCompletable { it.setRotationToCompletable(rotation, duration, interpolator, startDelay) }

fun View.setRotationXToCompletable(rotationX: Float,
                                   duration: Long? = null,
                                   interpolator: TimeInterpolator? = null,
                                   startDelay: Long? = null): Completable =
        animateToCompletable(
                rotationX = rotationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setRotationX(rotationX: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null): Observable<View> =
        doCompletable { it.setRotationXToCompletable(rotationX, duration, interpolator, startDelay) }

fun View.setRotationYToCompletable(rotationY: Float,
                                   duration: Long? = null,
                                   interpolator: TimeInterpolator? = null,
                                   startDelay: Long? = null): Completable =
        animateToCompletable(
                rotationY = rotationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setRotationY(rotationY: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null): Observable<View> =
        doCompletable { it.setRotationYToCompletable(rotationY, duration, interpolator, startDelay) }
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

fun Observable<View>.setXYZ(x: Float? = null,
                            y: Float? = null,
                            z: Float? = null,
                            duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null): Observable<View> =
        doCompletable { it.setXYZToCompletable(x, y, z, duration, interpolator, startDelay) }

fun View.setXToCompletable(x: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                x = x,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setX(x: Float,
                          duration: Long? = null,
                          interpolator: TimeInterpolator? = null,
                          startDelay: Long? = null): Observable<View> =
        doCompletable { it.setXToCompletable(x, duration, interpolator, startDelay) }

fun View.setYToCompletable(y: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                y = y,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setY(y: Float,
                          duration: Long? = null,
                          interpolator: TimeInterpolator? = null,
                          startDelay: Long? = null): Observable<View> =
        doCompletable { it.setYToCompletable(y, duration, interpolator, startDelay) }

fun View.setZToCompletable(z: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Completable =
        animateToCompletable(
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.setZ(z: Float,
                          duration: Long? = null,
                          interpolator: TimeInterpolator? = null,
                          startDelay: Long? = null): Observable<View> =
        doCompletable { it.setZToCompletable(z, duration, interpolator, startDelay) }
//endregion

//region RESIZE
fun View.setHeightToCompletable(fromHeight: Int, toHeight: Int,
                                duration: Long? = null,
                                interpolator: Interpolator? = null): Completable =
        Completable.create {
            setHeightWithAnimation(
                    fromHeight, toHeight,
                    duration,
                    interpolator
            ) { it.onComplete() }
        }

fun Observable<View>.setHeight(fromHeight: Int, toHeight: Int,
                               duration: Long? = null,
                               interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.setHeightToCompletable(fromHeight, toHeight, duration, interpolator) }
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

fun Observable<View>.setBackgroundColor(@ColorInt colorFrom: Int,
                                        @ColorInt colorTo: Int,
                                        duration: Long? = null,
                                        interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.setBackgroundColorToCompletable(colorFrom, colorTo, duration, interpolator) }
//endregion