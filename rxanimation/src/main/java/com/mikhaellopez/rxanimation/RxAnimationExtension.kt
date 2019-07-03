package com.mikhaellopez.rxanimation

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.os.Handler
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.CycleInterpolator
import android.view.animation.Interpolator
import io.reactivex.Completable
import io.reactivex.Observable

//region PRIVATE UTILS
private fun Observable<View>.doCompletable(actionCompletable: (View) -> Completable): Observable<View> =
        flatMap { actionCompletable(it).toSingleDefault(it).toObservable() }

private fun Float.dpToPx(): Float =
    this * Resources.getSystem().displayMetrics.density

private fun Int.dpToPx(): Int =
    (this * Resources.getSystem().displayMetrics.density).toInt()

private fun (() -> Any).withDelay(delay: Long = 300) {
    Handler().postDelayed({ this.invoke() }, delay)
}

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
        animationEnd?.also { it.withDelay(this.duration) }
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
                                               action: (Float) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofFloat(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Float) })
        }

fun Observable<View>.rangeFloat(start: Float, end: Float,
                                duration: Long? = null,
                                action: (Float) -> Unit): Observable<View> =
        doCompletable { (start to end).rangeFloatToCompletable(duration, action) }

fun Pair<Int, Int>.rangeIntToCompletable(duration: Long? = null,
                                         action: (Int) -> Unit): Completable =
        Completable.create {
            ValueAnimator.ofInt(first, second)
                    .start(duration,
                            animationEnd = { it.onComplete() },
                            action = { action(it as Int) })
        }

fun Observable<View>.rangeInt(start: Int, end: Int,
                              duration: Long? = null,
                              action: (Int) -> Unit): Observable<View> =
        doCompletable { (start to end).rangeIntToCompletable(duration, action) }
//endregion

//region ALPHA
fun View.alpha(alpha: Float,
               duration: Long? = null,
               interpolator: TimeInterpolator? = null,
               startDelay: Long? = null): Completable =
        animate(alpha = alpha,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.alpha(alpha: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Observable<View> =
        doCompletable { it.alpha(alpha, duration, interpolator, startDelay) }

fun View.fadeIn(duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null): Completable =
        animate(alpha = 1f,
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
        animate(alpha = 0f,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.fadeOut(duration: Long? = null,
                             interpolator: TimeInterpolator? = null,
                             startDelay: Long? = null): Observable<View> =
        doCompletable { it.fadeOut(duration, interpolator, startDelay) }
//endregion

//region TRANSLATION
fun View.translation(translationX: Float,
                     translationY: Float,
                     duration: Long? = null,
                     interpolator: TimeInterpolator? = null,
                     startDelay: Long? = null): Completable =
        animate(translationX = translationX,
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.translation(translationX: Float,
                                 translationY: Float,
                                 duration: Long? = null,
                                 interpolator: TimeInterpolator? = null,
                                 startDelay: Long? = null): Observable<View> =
        doCompletable { it.translation(translationX, translationY, duration, interpolator, startDelay) }

fun View.translationX(translationX: Float,
                      duration: Long? = null,
                      interpolator: TimeInterpolator? = null,
                      startDelay: Long? = null): Completable =
        animate(translationX = translationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.translationX(translationX: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null): Observable<View> =
        doCompletable { it.translationX(translationX, duration, interpolator, startDelay) }

fun View.translationY(translationY: Float,
                      duration: Long? = null,
                      interpolator: TimeInterpolator? = null,
                      startDelay: Long? = null): Completable =
        animate(translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.translationY(translationY: Float,
                                  duration: Long? = null,
                                  interpolator: TimeInterpolator? = null,
                                  startDelay: Long? = null): Observable<View> =
        doCompletable { it.translationY(translationY, duration, interpolator, startDelay) }
//endregion

//region SCALE
fun View.scale(scale: Float,
               duration: Long? = null,
               interpolator: TimeInterpolator? = null,
               startDelay: Long? = null): Completable =
        animate(scaleX = scale,
                scaleY = scale,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.scale(scale: Float,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Observable<View> =
        doCompletable { it.scale(scale, duration, interpolator, startDelay) }

fun View.scaleX(scaleX: Float,
                duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null): Completable =
        animate(scaleX = scaleX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.scaleX(scaleX: Float,
                            duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null): Observable<View> =
        doCompletable { it.scaleX(scaleX, duration, interpolator, startDelay) }

fun View.scaleY(scaleY: Float,
                duration: Long? = null,
                interpolator: TimeInterpolator? = null,
                startDelay: Long? = null): Completable =
        animate(scaleY = scaleY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.scaleY(scaleY: Float,
                            duration: Long? = null,
                            interpolator: TimeInterpolator? = null,
                            startDelay: Long? = null): Observable<View> =
        doCompletable { it.scaleY(scaleY, duration, interpolator, startDelay) }
//endregion

//region ROTATION
fun View.rotation(rotation: Float,
                  duration: Long? = null,
                  interpolator: TimeInterpolator? = null,
                  startDelay: Long? = null): Completable =
        animate(rotation = rotation,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.rotation(rotation: Float,
                              duration: Long? = null,
                              interpolator: TimeInterpolator? = null,
                              startDelay: Long? = null): Observable<View> =
        doCompletable { it.rotation(rotation, duration, interpolator, startDelay) }

fun View.rotationX(rotationX: Float,
                   duration: Long? = null,
                   interpolator: TimeInterpolator? = null,
                   startDelay: Long? = null): Completable =
        animate(rotationX = rotationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.rotationX(rotationX: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Observable<View> =
        doCompletable { it.rotationX(rotationX, duration, interpolator, startDelay) }

fun View.rotationY(rotationY: Float,
                   duration: Long? = null,
                   interpolator: TimeInterpolator? = null,
                   startDelay: Long? = null): Completable =
        animate(rotationY = rotationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.rotationY(rotationY: Float,
                               duration: Long? = null,
                               interpolator: TimeInterpolator? = null,
                               startDelay: Long? = null): Observable<View> =
        doCompletable { it.rotationY(rotationY, duration, interpolator, startDelay) }
//endregion

//region X, Y, Z
fun View.xyz(x: Float? = null,
             y: Float? = null,
             z: Float? = null,
             duration: Long? = null,
             interpolator: TimeInterpolator? = null,
             startDelay: Long? = null): Completable =
        animate(x = x,
                y = y,
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.xyz(x: Float? = null,
                         y: Float? = null,
                         z: Float? = null,
                         duration: Long? = null,
                         interpolator: TimeInterpolator? = null,
                         startDelay: Long? = null): Observable<View> =
        doCompletable { it.xyz(x, y, z, duration, interpolator, startDelay) }

fun View.x(x: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null): Completable =
        animate(x = x,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.x(x: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null): Observable<View> =
        doCompletable { it.x(x, duration, interpolator, startDelay) }

fun View.y(y: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null): Completable =
        animate(y = y,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.y(y: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null): Observable<View> =
        doCompletable { it.y(y, duration, interpolator, startDelay) }

fun View.z(z: Float,
           duration: Long? = null,
           interpolator: TimeInterpolator? = null,
           startDelay: Long? = null): Completable =
        animate(z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay)

fun Observable<View>.z(z: Float,
                       duration: Long? = null,
                       interpolator: TimeInterpolator? = null,
                       startDelay: Long? = null): Observable<View> =
        doCompletable { it.z(z, duration, interpolator, startDelay) }
//endregion

//region RESIZE
fun View.width(width: Int,
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

fun Observable<View>.width(width: Int,
                           duration: Long? = null,
                           interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.width(width, duration, interpolator) }

fun View.height(height: Int,
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

fun Observable<View>.height(height: Int,
                            duration: Long? = null,
                            interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.height(height, duration, interpolator) }

fun View.resize(width: Int, height: Int,
                duration: Long? = null,
                interpolator: Interpolator? = null): Completable =
        width(width, duration, interpolator)
                .mergeWith(height(height, duration, interpolator))

fun Observable<View>.resize(width: Int, height: Int,
                            duration: Long? = null,
                            interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.resize(width, height, duration, interpolator) }
//endregion

//region COLOR
fun View.backgroundColor(colorFrom: Int,
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

fun Observable<View>.backgroundColor(colorFrom: Int,
                                     colorTo: Int,
                                     duration: Long? = null,
                                     interpolator: Interpolator? = null): Observable<View> =
        doCompletable { it.backgroundColor(colorFrom, colorTo, duration, interpolator) }
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
        scale(depth, (duration ?: 300) / 2, interpolator, startDelay)
                .andThen(scale(1f, (duration ?: 300) / 2, interpolator))

fun Observable<View>.press(depth: Float = 0.95f,
                           duration: Long? = null,
                           interpolator: TimeInterpolator? = null,
                           startDelay: Long? = null): Observable<View> =
        doCompletable { it.press(depth, duration, interpolator, startDelay) }
//endregion