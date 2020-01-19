package com.mikhaellopez.rxanimation

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.Interpolator
import android.widget.TextView
import io.reactivex.Completable
import io.reactivex.Observable

//region PRIVATE UTILS
private fun Observable<View>.doCompletable(actionCompletable: (View) -> Completable): Observable<View> =
    flatMap { actionCompletable(it).toSingleDefault(it).toObservable() }
//endregion

//region VALUE ANIMATOR
/**
 * Start [ValueAnimator] animation and handle it in [Observable].
 * You can add a specific duration.
 *
 * @param valueAnimator [ValueAnimator] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param action ([Any]) -> [Unit] the action to do on UpdateListener.
 * @return An [Observable] of [View].
 */
fun Observable<View>.startValueAnimator(
    valueAnimator: ValueAnimator,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    action: (Any) -> Unit
): Observable<View> =
    doCompletable { valueAnimator.start(duration, interpolator, action) }
//endregion

//region RANGE
/**
 * Apply animation to your [View] with [Float] range and handle it in [Observable].
 * Perfect to apply animation on custom properties.
 * You can add a specific duration and reverse the animation.
 *
 * @param start [Float] param mandatory.
 * @param end [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rangeInt
 * @return An [Observable] of [View].
 */
fun Observable<View>.rangeFloat(
    start: Float, end: Float,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false,
    action: (Float) -> Unit
): Observable<View> =
    doCompletable { (start to end).rangeFloatToCompletable(duration, interpolator, reverse, action) }

/**
 * Apply animation to your [View] with [Int] range and handle it in [Observable].
 * Perfect to apply animation on custom properties.
 * You can add a specific duration and reverse the animation.
 *
 * @param start [Int] param mandatory.
 * @param end [Int] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rangeFloat
 * @return An [Observable] of [View].
 */
fun Observable<View>.rangeInt(
    start: Int, end: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false,
    action: (Int) -> Unit
): Observable<View> =
    doCompletable { (start to end).rangeIntToCompletable(duration, interpolator, reverse, action) }
//endregion

//region ALPHA
/**
 * Set the alpha of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param alpha [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see fadeIn
 * @see fadeOut
 * @return An [Observable] of [View].
 */
fun Observable<View>.alpha(
    alpha: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.alpha(alpha, duration, interpolator, startDelay, reverse) }

/**
 * Set the alpha to 1f of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see alpha
 * @see fadeOut
 * @return An [Observable] of [View].
 */
fun Observable<View>.fadeIn(
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.fadeIn(duration, interpolator, startDelay, reverse) }

/**
 * Set the alpha to 0f of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see alpha
 * @see fadeIn
 * @return An [Observable] of [View].
 */
fun Observable<View>.fadeOut(
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.fadeOut(duration, interpolator, startDelay, reverse) }
//endregion

//region TRANSLATION
/**
 * Set the translation of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param translationX [Float] param mandatory, in DP
 * @param translationY [Float] param mandatory, in DP
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @return An [Observable] of [View].
 */
fun Observable<View>.translation(
    translationX: Float,
    translationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable {
        it.translation(
            translationX,
            translationY,
            duration,
            interpolator,
            startDelay,
            reverse
        )
    }

/**
 * Set the translationX of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param translationX [Float] param mandatory, in DP
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see translation
 * @see translationY
 * @return An [Observable] of [View].
 */
fun Observable<View>.translationX(
    translationX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.translationX(translationX, duration, interpolator, startDelay, reverse) }

/**
 * Set the translationY of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param translationY [Float] param mandatory, in DP
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see translation
 * @see translationX
 * @return An [Observable] of [View].
 */
fun Observable<View>.translationY(
    translationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.translationY(translationY, duration, interpolator, startDelay, reverse) }
//endregion

//region SCALE
/**
 * Set the scale of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scale [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see scaleX
 * @see scaleY
 * @return An [Observable] of [View].
 */
fun Observable<View>.scale(
    scale: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.scale(scale, duration, interpolator, startDelay, reverse) }

/**
 * Set the scaleX of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scaleX [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see scale
 * @see scaleY
 * @return An [Observable] of [View].
 */
fun Observable<View>.scaleX(
    scaleX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.scaleX(scaleX, duration, interpolator, startDelay, reverse) }

/**
 * Set the scaleY of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scaleY [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see scale
 * @see scaleX
 * @return An [Observable] of [View].
 */
fun Observable<View>.scaleY(
    scaleY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.scaleY(scaleY, duration, interpolator, startDelay, reverse) }
//endregion

//region ROTATION
/**
 * Set the rotation of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotation [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rotationX
 * @see rotationY
 * @return An [Observable] of [View].
 */
fun Observable<View>.rotation(
    rotation: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.rotation(rotation, duration, interpolator, startDelay, reverse) }

/**
 * Set the rotationX of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotationX [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rotation
 * @see rotationY
 * @return An [Observable] of [View].
 */
fun Observable<View>.rotationX(
    rotationX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.rotationX(rotationX, duration, interpolator, startDelay, reverse) }

/**
 * Set the rotationY of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotationY [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rotation
 * @see rotationX
 * @return An [Observable] of [View].
 */
fun Observable<View>.rotationY(
    rotationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.rotationY(rotationY, duration, interpolator, startDelay, reverse) }
//endregion

//region X, Y, Z
/**
 * Set the X, Y & Z of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param x [Float] optional, null by default.
 * @param y [Float] optional, null by default.
 * @param z [Float] optional, null by default.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @return An [Observable] of [View].
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Observable<View>.xyz(
    x: Float? = null,
    y: Float? = null,
    z: Float? = null,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.xyz(x, y, z, duration, interpolator, startDelay, reverse) }

/**
 * Set the X of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param x [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see xyz
 * @see y
 * @see z
 * @return An [Observable] of [View].
 */
fun Observable<View>.x(
    x: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.x(x, duration, interpolator, startDelay, reverse) }

/**
 * Set the Y of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param y [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see xyz
 * @see x
 * @see z
 * @return An [Observable] of [View].
 */
fun Observable<View>.y(
    y: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.y(y, duration, interpolator, startDelay, reverse) }

/**
 * Set the Z of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param z [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see xyz
 * @see x
 * @see y
 * @return An [Observable] of [View].
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Observable<View>.z(
    z: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.z(z, duration, interpolator, startDelay, reverse) }
//endregion

//region RESIZE
/**
 * Set the width and height of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param width [Int] param mandatory, in DP.
 * @param height [Int] param mandatory, in DP.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @return An [Observable] of [View].
 */
fun Observable<View>.resize(
    width: Int, height: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.resize(width, height, duration, interpolator, reverse) }

/**
 * Set the width of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param width [Int] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see resize
 * @see height
 * @return An [Observable] of [View].
 */
fun Observable<View>.width(
    width: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.width(width, duration, interpolator, reverse) }

/**
 * Set the height of your [View] in DP with animation and handle it in [Observable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param height [Int] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see resize
 * @see width
 * @return An [Observable] of [View].
 */
fun Observable<View>.height(
    height: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.height(height, duration, interpolator, reverse) }
//endregion

//region COLOR
/**
 * Set the background color of your [View] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param colorFrom [Int] param mandatory.
 * @param colorTo [Int] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.backgroundColor
 * @return An [Observable] of [View].
 */
fun Observable<View>.backgroundColor(
    colorFrom: Int,
    colorTo: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Observable<View> =
    doCompletable { it.backgroundColor(colorFrom, colorTo, duration, interpolator, reverse) }
//endregion

//region SHAKE
/**
 * Apply shake animation to your [View] and handle it in [Observable].
 *
 * @param duration [Long] in millisecond, 300ms by default.
 * @param nbShake [Float] for [CycleInterpolator], 2f by default.
 * @param shakeTranslation [Float] in DP for translation, 5f by default.
 * @see View.shake
 * @return An [Observable] of [View].
 */
fun Observable<View>.shake(
    duration: Long = 300,
    nbShake: Float = 2f,
    shakeTranslation: Float = 5f
): Observable<View> =
    doCompletable { it.shake(duration, nbShake, shakeTranslation) }
//endregion

//region PRESS
/**
 * Apply press animation to your [View] and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 *
 * @param depth [Float] 0.95f by default.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @see View.press
 * @return An [Observable] of [View].
 */
fun Observable<View>.press(
    depth: Float = 0.95f,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null
): Observable<View> =
    doCompletable { it.press(depth, duration, interpolator, startDelay) }
//endregion

//region TEXT
/**
 * Set the text of your [TextView] with animation and handle it in [Observable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param text [String] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see TextView.text
 * @return An [Observable] of [TextView].
 */
fun Observable<TextView>.text(
    text: String,
    duration: Long = 300L,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Observable<View> =
    flatMap {
        it.text(text, duration, interpolator, startDelay, reverse).toSingleDefault(it)
            .toObservable()
    }
//endregion