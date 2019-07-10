package com.mikhaellopez.rxanimation

import android.animation.*
import android.annotation.TargetApi
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.CycleInterpolator
import android.view.animation.Interpolator
import android.widget.TextView
import io.reactivex.Completable
import io.reactivex.Observable

//region PRIVATE UTILS
private fun Observable<View>.doCompletable(actionCompletable: (View) -> Completable): Observable<View> =
        flatMap { actionCompletable(it).toSingleDefault(it).toObservable() }

private fun Completable.reverse(reverse: Boolean, reverseCompletable: () -> Completable): Completable =
        run { if (reverse) andThen(reverseCompletable()) else this }

private fun Float.dpToPx(): Float =
        this * Resources.getSystem().displayMetrics.density

private fun Int.dpToPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

private fun Int.pxToDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

private fun ViewPropertyAnimator.animate(animationEnd: (() -> Unit)? = null) {
    withEndAction { animationEnd?.invoke() }.start()
}
//endregion

//region DEFAULT
fun View.animate(alpha: Float? = null,
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
            animate().apply {
                alpha?.also { alpha(it) }
                translationX?.also { translationX(it.dpToPx()) }
                translationY?.also { translationY(it.dpToPx()) }
                scaleX?.also { scaleX(it) }
                scaleY?.also { scaleY(it) }
                rotation?.also { rotation(it) }
                rotationX?.also { rotationX(it) }
                rotationY?.also { rotationY(it) }
                x?.also { x(it) }
                y?.also { x(it) }
                z?.also { x(it) }
                duration?.also { this.duration = it }
                interpolator?.also { this.interpolator = it }
                startDelay?.also { this.startDelay = it }
            }.animate { it.onComplete() }
        }
//endregion

//region VALUE ANIMATOR
private fun ValueAnimator.start(duration: Long? = null,
                                interpolator: Interpolator? = null,
                                animationEnd: (() -> Unit)? = null,
                                action: (Any) -> Unit) {
    apply {
        duration?.also { this.duration = it }
        interpolator?.also { this.interpolator = interpolator }
        addUpdateListener { action(it.animatedValue) }
        animationEnd?.also {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    it()
                }
            })
        }
    }.start()
}

fun ValueAnimator.start(duration: Long? = null,
                        action: (Any) -> Unit): Completable =
        Completable.create {
            start(duration,
                    animationEnd = { it.onComplete() },
                    action = action)
        }

fun Observable<View>.startValueAnimator(valueAnimator: ValueAnimator,
                                        duration: Long? = null,
                                        action: (Any) -> Unit): Observable<View> =
        doCompletable { valueAnimator.start(duration, action) }
//endregion

//region VIEW PROPERTY ANIMATOR
fun ViewPropertyAnimator.animate(): Completable =
        Completable.create {
            animate { it.onComplete() }
        }
//endregion

//region RANGE
fun Pair<Float, Float>.rangeFloatToCompletable(duration: Long? = null,
                                               reverse: Boolean = false,
                                               action: (Float) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofFloat(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Float) })
        }.reverse(reverse) {
            (second to first).rangeFloatToCompletable(duration, action = action)
        }

fun Observable<View>.rangeFloat(start: Float, end: Float,
                                duration: Long? = null,
                                reverse: Boolean = false,
                                action: (Float) -> Unit): Observable<View> =
        doCompletable { (start to end).rangeFloatToCompletable(duration, reverse, action) }

fun Pair<Int, Int>.rangeIntToCompletable(duration: Long? = null,
                                         reverse: Boolean = false,
                                         action: (Int) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofInt(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Int) })
        }.run {
            if (reverse) {
                andThen((second to first).rangeIntToCompletable(duration, action = action))
            } else this
        }

fun Observable<View>.rangeInt(start: Int, end: Int,
                              duration: Long? = null,
                              reverse: Boolean = false,
                              action: (Int) -> Unit): Observable<View> =
        doCompletable { (start to end).rangeIntToCompletable(duration, reverse, action) }
//endregion

//region ALPHA
fun View.alpha(alpha: Float,
               duration: Long? = null,
               interpolator: TimeInterpolator? = null,
               startDelay: Long? = null,
               reverse: Boolean = false): Completable =
        Observable.just(this.alpha)
                .flatMapCompletable { defaultAlpha ->
                    animate(alpha = alpha,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                alpha(defaultAlpha, duration, interpolator)
                            }
                }

fun Observable<View>.alpha(alpha: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null,
                           reverse: Boolean = false): Observable<View> =
        doCompletable { it.alpha(alpha, duration, interpolator, startDelay, reverse) }

fun View.fadeIn(duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null,
                reverse: Boolean = false): Completable =
        alpha(1f, duration, interpolator, startDelay, reverse)

fun Observable<View>.fadeIn(duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null,
                            reverse: Boolean = false): Observable<View> =
        doCompletable { it.fadeIn(duration, interpolator, startDelay, reverse) }

fun View.fadeOut(duration: Long? = null,
                 interpolator: TimeInterpolator? = null,
                 startDelay: Long? = null,
                 reverse: Boolean = false): Completable =
        alpha(0f, duration, interpolator, startDelay, reverse)

fun Observable<View>.fadeOut(duration: Long? = null,
                             interpolator: TimeInterpolator? = null,
                             startDelay: Long? = null,
                             reverse: Boolean = false): Observable<View> =
        doCompletable { it.fadeOut(duration, interpolator, startDelay, reverse) }
//endregion

//region TRANSLATION
fun View.translation(translationX: Float,
                     translationY: Float,
                     duration: Long? = null,
                     interpolator: TimeInterpolator? = null,
                     startDelay: Long? = null,
                     reverse: Boolean = false): Completable =
        Observable.just(this.translationX to this.translationY)
                .flatMapCompletable { (defaultTranslationX, defaultTranslationY) ->
                    animate(translationX = translationX,
                            translationY = translationY,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                translation(defaultTranslationX, defaultTranslationY, duration, interpolator)
                            }
                }

fun Observable<View>.translation(translationX: Float,
                                 translationY: Float,
                                 duration: Long? = null,
                                 interpolator: TimeInterpolator? = null,
                                 startDelay: Long? = null,
                                 reverse: Boolean = false): Observable<View> =
        doCompletable { it.translation(translationX, translationY, duration, interpolator, startDelay, reverse) }

fun View.translationX(translationX: Float,
                      duration: Long? = null,
                      interpolator: TimeInterpolator? = null,
                      startDelay: Long? = null,
                      reverse: Boolean = false): Completable =
        Observable.just(this.translationX)
                .flatMapCompletable { defaultTranslationX ->
                    animate(translationX = translationX,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                translationX(defaultTranslationX, duration, interpolator)
                            }
                }

fun Observable<View>.translationX(translationX: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null,
                                  reverse: Boolean = false): Observable<View> =
        doCompletable { it.translationX(translationX, duration, interpolator, startDelay, reverse) }

fun View.translationY(translationY: Float,
                      duration: Long? = null,
                      interpolator: TimeInterpolator? = null,
                      startDelay: Long? = null,
                      reverse: Boolean = false): Completable =
        Observable.just(this.translationY)
                .flatMapCompletable { defaultTranslationY ->
                    animate(translationY = translationY,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                translationY(defaultTranslationY, duration, interpolator)
                            }
                }

fun Observable<View>.translationY(translationY: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null,
                                  reverse: Boolean = false): Observable<View> =
        doCompletable { it.translationY(translationY, duration, interpolator, startDelay, reverse) }
//endregion

//region SCALE
fun View.scale(scale: Float,
               duration: Long? = null,
               interpolator: TimeInterpolator? = null,
               startDelay: Long? = null,
               reverse: Boolean = false): Completable =
        Observable.just(this.scaleX to this.scaleY)
                .flatMapCompletable { (defaultScaleX, defaultScaleY) ->
                    animate(scaleX = scale,
                            scaleY = scale,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                animate(scaleX = defaultScaleX,
                                        scaleY = defaultScaleY,
                                        duration = duration,
                                        interpolator = interpolator)
                            }
                }

fun Observable<View>.scale(scale: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null,
                           reverse: Boolean = false): Observable<View> =
        doCompletable { it.scale(scale, duration, interpolator, startDelay, reverse) }

fun View.scaleX(scaleX: Float,
                duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null,
                reverse: Boolean = false): Completable =
        Observable.just(this.scaleX)
                .flatMapCompletable { defaultScaleX ->
                    animate(scaleX = scaleX,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                scaleX(defaultScaleX, duration, interpolator)
                            }
                }

fun Observable<View>.scaleX(scaleX: Float,
                            duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null,
                            reverse: Boolean = false): Observable<View> =
        doCompletable { it.scaleX(scaleX, duration, interpolator, startDelay, reverse) }

fun View.scaleY(scaleY: Float,
                duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null,
                reverse: Boolean = false): Completable =
        Observable.just(this.scaleY)
                .flatMapCompletable { defaultScaleY ->
                    animate(scaleY = scaleY,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                scaleY(defaultScaleY, duration, interpolator)
                            }
                }

fun Observable<View>.scaleY(scaleY: Float,
                            duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null,
                            reverse: Boolean = false): Observable<View> =
        doCompletable { it.scaleY(scaleY, duration, interpolator, startDelay, reverse) }
//endregion

//region ROTATION
fun View.rotation(rotation: Float,
                  duration: Long? = null,
                  interpolator: TimeInterpolator? = null,
                  startDelay: Long? = null,
                  reverse: Boolean = false): Completable =
        Observable.just(this.rotation)
                .flatMapCompletable { defaultRotation ->
                    animate(rotation = rotation,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                rotation(defaultRotation, duration, interpolator)
                            }
                }

fun Observable<View>.rotation(rotation: Float,
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null,
                              reverse: Boolean = false): Observable<View> =
        doCompletable { it.rotation(rotation, duration, interpolator, startDelay, reverse) }

fun View.rotationX(rotationX: Float,
                   duration: Long? = null,
                   interpolator: TimeInterpolator? = null,
                   startDelay: Long? = null,
                   reverse: Boolean = false): Completable =
        Observable.just(this.rotationX)
                .flatMapCompletable { defaultRotationX ->
                    animate(rotationX = rotationX,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                rotationX(defaultRotationX, duration, interpolator)
                            }
                }

fun Observable<View>.rotationX(rotationX: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null,
                               reverse: Boolean = false): Observable<View> =
        doCompletable { it.rotationX(rotationX, duration, interpolator, startDelay, reverse) }

fun View.rotationY(rotationY: Float,
                   duration: Long? = null,
                   interpolator: TimeInterpolator? = null,
                   startDelay: Long? = null,
                   reverse: Boolean = false): Completable =
        Observable.just(this.rotationY)
                .flatMapCompletable { defaultRotationY ->
                    animate(rotationY = rotationY,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                rotationY(defaultRotationY, duration, interpolator)
                            }
                }

fun Observable<View>.rotationY(rotationY: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null,
                               reverse: Boolean = false): Observable<View> =
        doCompletable { it.rotationY(rotationY, duration, interpolator, startDelay, reverse) }
//endregion

//region X, Y, Z
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.xyz(x: Float? = null,
             y: Float? = null,
             z: Float? = null,
             duration: Long? = null,
             interpolator: TimeInterpolator? = null,
             startDelay: Long? = null,
             reverse: Boolean = false): Completable =
        Observable.just(Triple(this.x, this.y, this.z))
                .flatMapCompletable { (defaultX, defaultY, defaultZ) ->
                    animate(x = x,
                            y = y,
                            z = z,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                xyz(defaultX, defaultY, defaultZ, duration, interpolator)
                            }
                }

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Observable<View>.xyz(x: Float? = null,
                         y: Float? = null,
                         z: Float? = null,
                         duration: Long? = null,
                         interpolator: TimeInterpolator? = null,
                         startDelay: Long? = null,
                         reverse: Boolean = false): Observable<View> =
        doCompletable { it.xyz(x, y, z, duration, interpolator, startDelay, reverse) }

fun View.x(x: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null,
           reverse: Boolean = false): Completable =
        Observable.just(this.x)
                .flatMapCompletable { defaultX ->
                    animate(x = x,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                x(defaultX, duration, interpolator)
                            }
                }

fun Observable<View>.x(x: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null,
                       reverse: Boolean = false): Observable<View> =
        doCompletable { it.x(x, duration, interpolator, startDelay, reverse) }

fun View.y(y: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null,
           reverse: Boolean = false): Completable =
        Observable.just(this.y)
                .flatMapCompletable { defaultY ->
                    animate(y = y,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                y(defaultY, duration, interpolator)
                            }
                }

fun Observable<View>.y(y: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null,
                       reverse: Boolean = false): Observable<View> =
        doCompletable { it.y(y, duration, interpolator, startDelay, reverse) }

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.z(z: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null,
           reverse: Boolean = false): Completable =
        Observable.just(this.z)
                .flatMapCompletable { defaultZ ->
                    animate(z = z,
                            duration = duration,
                            interpolator = interpolator,
                            startDelay = startDelay)
                            .reverse(reverse) {
                                z(defaultZ, duration, interpolator)
                            }
                }

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Observable<View>.z(z: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null,
                       reverse: Boolean = false): Observable<View> =
        doCompletable { it.z(z, duration, interpolator, startDelay, reverse) }
//endregion

//region RESIZE
private fun View.widthToCompletable(width: Int,
                                    duration: Long? = null,
                                    interpolator: Interpolator? = null): Completable =
        Completable.create {
            ValueAnimator.ofInt(this.width, width.dpToPx())
                    .start(duration, interpolator,
                            animationEnd = { it.onComplete() }) {
                        layoutParams.width = it as Int
                        requestLayout()
                    }
        }

fun View.width(width: Int,
               duration: Long? = null,
               interpolator: Interpolator? = null,
               reverse: Boolean = false): Completable =
        Observable.just(this.width)
                .flatMapCompletable { defaultWidth ->
                    widthToCompletable(width, duration, interpolator)
                            .reverse(reverse) {
                                widthToCompletable(defaultWidth, duration, interpolator)
                            }
                }

fun Observable<View>.width(width: Int,
                           duration: Long? = null,
                           interpolator: Interpolator? = null,
                           reverse: Boolean = false): Observable<View> =
        doCompletable { it.width(width, duration, interpolator, reverse) }

fun View.heightToCompletable(height: Int,
                             duration: Long? = null,
                             interpolator: Interpolator? = null): Completable =
        Completable.create {
            ValueAnimator.ofInt(this.height, height.dpToPx())
                    .start(duration, interpolator,
                            animationEnd = { it.onComplete() }) {
                        layoutParams.height = it as Int
                        requestLayout()
                    }
        }

fun View.height(height: Int,
                duration: Long? = null,
                interpolator: Interpolator? = null,
                reverse: Boolean = false): Completable =
        Observable.just(this.height)
                .flatMapCompletable { defaultHeight ->
                    heightToCompletable(height, duration, interpolator)
                            .reverse(reverse) {
                                heightToCompletable(defaultHeight, duration, interpolator)
                            }
                }

fun Observable<View>.height(height: Int,
                            duration: Long? = null,
                            interpolator: Interpolator? = null,
                            reverse: Boolean = false): Observable<View> =
        doCompletable { it.height(height, duration, interpolator, reverse) }

fun View.resize(width: Int, height: Int,
                duration: Long? = null,
                interpolator: Interpolator? = null,
                reverse: Boolean = false): Completable =
        Observable.just(this.width.pxToDp() to this.height.pxToDp())
                .flatMapCompletable { (defaultWidth, defaultHeight) ->
                    width(width, duration, interpolator)
                            .mergeWith(height(height, duration, interpolator))
                            .reverse(reverse) {
                                resize(defaultWidth, defaultHeight, duration, interpolator)
                            }
                }

fun Observable<View>.resize(width: Int, height: Int,
                            duration: Long? = null,
                            interpolator: Interpolator? = null,
                            reverse: Boolean = false): Observable<View> =
        doCompletable { it.resize(width, height, duration, interpolator, reverse) }
//endregion

//region COLOR
private fun View.backgroundColorToCompletable(colorFrom: Int,
                                              colorTo: Int,
                                              duration: Long? = null,
                                              interpolator: Interpolator? = null): Completable =
        Completable.create {
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                    .start(duration, interpolator,
                            animationEnd = { it.onComplete() }) {
                        setBackgroundColor(it as Int)
                    }
        }

fun View.backgroundColor(colorFrom: Int,
                         colorTo: Int,
                         duration: Long? = null,
                         interpolator: Interpolator? = null,
                         reverse: Boolean = false): Completable =
        backgroundColorToCompletable(colorFrom, colorTo, duration, interpolator)
                .reverse(reverse) {
                    backgroundColorToCompletable(colorTo, colorFrom, duration, interpolator)
                }

fun Observable<View>.backgroundColor(colorFrom: Int,
                                     colorTo: Int,
                                     duration: Long? = null,
                                     interpolator: Interpolator? = null,
                                     reverse: Boolean = false): Observable<View> =
        doCompletable { it.backgroundColor(colorFrom, colorTo, duration, interpolator, reverse) }
//endregion

//region SHAKE
fun View.shake(duration: Long = 300,
               nbShake: Float = 2f,
               shakeTranslation: Float = 5f): Completable =
        Completable.create {
            animate().apply {
                this.duration = duration
                interpolator = CycleInterpolator(nbShake)
                translationX(-shakeTranslation.dpToPx())
                translationX(shakeTranslation.dpToPx())
            }.animate { it.onComplete() }
        }

fun Observable<View>.shake(duration: Long = 300,
                           nbShake: Float = 2f,
                           shakeTranslation: Float = 5f): Observable<View> =
        doCompletable { it.shake(duration, nbShake, shakeTranslation) }
//endregion

//region PRESS
fun View.press(depth: Float = 0.95f,
               duration: Long? = null,
               interpolator: TimeInterpolator? = null,
               startDelay: Long? = null): Completable =
        scale(depth, (duration ?: 300) / 2, interpolator, startDelay, reverse = true)

fun Observable<View>.press(depth: Float = 0.95f,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Observable<View> =
        doCompletable { it.press(depth, duration, interpolator, startDelay) }
//endregion

//region TEXT
fun TextView.text(text: String,
                  duration: Long = 300L,
                  interpolator: TimeInterpolator? = null,
                  startDelay: Long? = null,
                  reverse: Boolean = false): Completable =
        Observable.just(this.text.toString())
                .flatMapCompletable { defaultText ->
                    fadeOut(duration / 2, interpolator, startDelay)
                            .doOnComplete { this.text = text }
                            .andThen(fadeIn(duration / 2, interpolator, startDelay = 300L))
                            .reverse(reverse) {
                                text(defaultText, duration, interpolator)
                            }
                }

fun Observable<TextView>.text(text: String,
                              duration: Long = 300L,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null,
                              reverse: Boolean = false): Observable<View> =
        flatMap { it.text(text, duration, interpolator, startDelay, reverse).toSingleDefault(it).toObservable() }
//endregion