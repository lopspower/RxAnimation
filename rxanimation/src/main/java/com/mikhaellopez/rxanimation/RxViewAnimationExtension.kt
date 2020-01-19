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
private fun Float.dpToPx(): Float =
    this * Resources.getSystem().displayMetrics.density

private fun Int.dpToPx(): Int =
    (this * Resources.getSystem().displayMetrics.density).toInt()

private fun Int.pxToDp(): Int =
    (this / Resources.getSystem().displayMetrics.density).toInt()

private fun ViewPropertyAnimator.animate(animationEnd: (() -> Unit)? = null) {
    withEndAction { animationEnd?.invoke() }.start()
}

private fun Completable.reverse(
    reverse: Boolean,
    reverseCompletable: () -> Completable
): Completable =
    run { if (reverse) andThen(reverseCompletable()) else this }
//endregion

//region DEFAULT
/**
 * Animate your [View] and handle it in [Completable].
 * All properties from [ViewPropertyAnimator] are available
 * as optional, null by default.
 * You can also add a specific duration, interpolator or startDelay.
 *
 * @param alpha [Float] optional, null by default.
 * @param translationX [Float] optional, null by default.
 * @param translationY [Float] optional, null by default.
 * @param scaleX [Float] optional, null by default.
 * @param scaleY [Float] optional, null by default.
 * @param rotation [Float] optional, null by default.
 * @param rotationX [Float] optional, null by default.
 * @param rotationY [Float] optional, null by default.
 * @param x [Float] optional, null by default.
 * @param y [Float] optional, null by default.
 * @param z [Float] optional, null by default.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @see View
 * @see Completable
 * @see ViewPropertyAnimator
 * @return A [Completable].
 */
fun View.animate(
    alpha: Float? = null,
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
    startDelay: Long? = null
): Completable =
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
private fun ValueAnimator.start(
    duration: Long? = null,
    interpolator: Interpolator? = null,
    animationEnd: (() -> Unit)? = null,
    action: (Any) -> Unit
) {
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

/**
 * Start [ValueAnimator] animation and handle it in [Completable].
 * You can add a specific duration.
 *
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param action ([Any]) -> [Unit] the action to do on UpdateListener.
 * @see startValueAnimator
 * @return A [Completable].
 */
fun ValueAnimator.start(
    duration: Long? = null,
    interpolator: Interpolator? = null,
    action: (Any) -> Unit
): Completable =
    Completable.create {
        start(
            duration,
            interpolator,
            animationEnd = { it.onComplete() },
            action = action
        )
    }
//endregion

//region VIEW PROPERTY ANIMATOR
/**
 * Animate [ViewPropertyAnimator] and handle it in [Completable].
 *
 * @return A [Completable].
 */
fun ViewPropertyAnimator.animate(): Completable =
    Completable.create {
        animate { it.onComplete() }
    }
//endregion

//region RANGE
/**
 * Create [Float] range to apply animation to your [View] and handle it in [Completable].
 * Perfect to apply animation on custom properties.
 * You can add a specific duration and reverse the animation.
 *
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rangeIntToCompletable
 * @return A [Completable].
 */
fun Pair<Float, Float>.rangeFloatToCompletable(
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false,
    action: (Float) -> Unit
): Completable =
    Completable.create {
        ValueAnimator.ofFloat(first, second)
            .start(duration, interpolator,
                animationEnd = { it.onComplete() },
                action = { value -> (value as? Float)?.also { action(it) } })
    }.reverse(reverse) {
        (second to first).rangeFloatToCompletable(duration, interpolator, action = action)
    }

/**
 * Create [Int] range to apply animation to your [View] and handle it in [Completable].
 * Perfect to apply animation on custom properties.
 * You can add a specific duration and reverse the animation.
 *
 * @param duration [Long] optional, null by default.
 * @param interpolator [Interpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see rangeFloatToCompletable
 * @return A [Completable].
 */
fun Pair<Int, Int>.rangeIntToCompletable(
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false,
    action: (Int) -> Unit
): Completable =
    Completable.create {
        ValueAnimator.ofInt(first, second)
            .start(duration, interpolator,
                animationEnd = { it.onComplete() },
                action = { value -> (value as? Int)?.also { action(it) } })
    }.run {
        if (reverse) {
            andThen(
                (second to first).rangeIntToCompletable(
                    duration,
                    interpolator,
                    action = action
                )
            )
        } else this
    }
//endregion

//region ALPHA
/**
 * Set the alpha of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param alpha [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.fadeIn
 * @see View.fadeOut
 * @return A [Completable].
 */
fun View.alpha(
    alpha: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.alpha)
        .flatMapCompletable { defaultAlpha ->
            animate(
                alpha = alpha,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                alpha(defaultAlpha, duration, interpolator)
            }
        }

/**
 * Set the alpha to 1f of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param duration [Long] optional, optional, null by default.
 * @param interpolator [TimeInterpolator] optional, optional, null by default.
 * @param startDelay [Long] optional, optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.fadeOut
 * @see View.alpha
 * @return A [Completable].
 */
fun View.fadeIn(
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    alpha(1f, duration, interpolator, startDelay, reverse)


/**
 * Set the alpha to Of of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param duration [Long] optional, optional, null by default.
 * @param interpolator [TimeInterpolator] optional, optional, null by default.
 * @param startDelay [Long] optional, optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.fadeIn
 * @see View.alpha
 * @return A [Completable].
 */
fun View.fadeOut(
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    alpha(0f, duration, interpolator, startDelay, reverse)
//endregion

//region TRANSLATION
/**
 * Set the translation X and Y of your [View] in DP with animation and handle it in [Completable].
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
 * @see View.translationX
 * @see View.translationY
 * @return A [Completable].
 */
fun View.translation(
    translationX: Float,
    translationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.translationX to this.translationY)
        .flatMapCompletable { (defaultTranslationX, defaultTranslationY) ->
            animate(
                translationX = translationX,
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                translation(defaultTranslationX, defaultTranslationY, duration, interpolator)
            }
        }

/**
 * Set the translation X of your [View] in DP with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param translationX [Float] param mandatory, in DP
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.translation
 * @see View.translationY
 * @return A [Completable].
 */
fun View.translationX(
    translationX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.translationX)
        .flatMapCompletable { defaultTranslationX ->
            animate(
                translationX = translationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                translationX(defaultTranslationX, duration, interpolator)
            }
        }

/**
 * Set the translation Y of your [View] in DP with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param translationY [Float] param mandatory, in DP
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.translation
 * @see View.translationX
 * @return A [Completable].
 */
fun View.translationY(
    translationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.translationY)
        .flatMapCompletable { defaultTranslationY ->
            animate(
                translationY = translationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                translationY(defaultTranslationY, duration, interpolator)
            }
        }
//endregion

//region SCALE
/**
 * Set the scale of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scale [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.scaleX
 * @see View.scaleY
 * @return A [Completable].
 */
fun View.scale(
    scale: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.scaleX to this.scaleY)
        .flatMapCompletable { (defaultScaleX, defaultScaleY) ->
            animate(
                scaleX = scale,
                scaleY = scale,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                animate(
                    scaleX = defaultScaleX,
                    scaleY = defaultScaleY,
                    duration = duration,
                    interpolator = interpolator
                )
            }
        }

/**
 * Set the scale X of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scaleX [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.scale
 * @see View.scaleY
 * @return A [Completable].
 */
fun View.scaleX(
    scaleX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.scaleX)
        .flatMapCompletable { defaultScaleX ->
            animate(
                scaleX = scaleX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                scaleX(defaultScaleX, duration, interpolator)
            }
        }

/**
 * Set the scale Y of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param scaleY [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.scale
 * @see View.scaleX
 * @return A [Completable].
 */
fun View.scaleY(
    scaleY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.scaleY)
        .flatMapCompletable { defaultScaleY ->
            animate(
                scaleY = scaleY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                scaleY(defaultScaleY, duration, interpolator)
            }
        }
//endregion

//region ROTATION
/**
 * Set the rotation of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotation [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.rotationX
 * @see View.rotationY
 * @return A [Completable].
 */
fun View.rotation(
    rotation: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.rotation)
        .flatMapCompletable { defaultRotation ->
            animate(
                rotation = rotation,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                rotation(defaultRotation, duration, interpolator)
            }
        }

/**
 * Set the rotation X of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotationX [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.rotation
 * @see View.rotationY
 * @return A [Completable].
 */
fun View.rotationX(
    rotationX: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.rotationX)
        .flatMapCompletable { defaultRotationX ->
            animate(
                rotationX = rotationX,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                rotationX(defaultRotationX, duration, interpolator)
            }
        }

/**
 * Set the rotation Y of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param rotationY [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.rotation
 * @see View.rotationX
 * @return A [Completable].
 */
fun View.rotationY(
    rotationY: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.rotationY)
        .flatMapCompletable { defaultRotationY ->
            animate(
                rotationY = rotationY,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                rotationY(defaultRotationY, duration, interpolator)
            }
        }
//endregion

//region X, Y, Z
/**
 * Set the X, Y & Z of your [View] with animation and handle it in [Completable].
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
 * @see View.x
 * @see View.y
 * @see View.z
 * @return A [Completable].
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.xyz(
    x: Float? = null,
    y: Float? = null,
    z: Float? = null,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(Triple(this.x, this.y, this.z))
        .flatMapCompletable { (defaultX, defaultY, defaultZ) ->
            animate(
                x = x,
                y = y,
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                xyz(defaultX, defaultY, defaultZ, duration, interpolator)
            }
        }

/**
 * Set the X of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param x [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.xyz
 * @see View.y
 * @see View.z
 * @return A [Completable].
 */
fun View.x(
    x: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.x)
        .flatMapCompletable { defaultX ->
            animate(
                x = x,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                x(defaultX, duration, interpolator)
            }
        }

/**
 * Set the Y of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param y [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.xyz
 * @see View.x
 * @see View.z
 * @return A [Completable].
 */
fun View.y(
    y: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.y)
        .flatMapCompletable { defaultY ->
            animate(
                y = y,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                y(defaultY, duration, interpolator)
            }
        }

/**
 * Set the Z of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param z [Float] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.xyz
 * @see View.x
 * @see View.y
 * @return A [Completable].
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.z(
    z: Float,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.z)
        .flatMapCompletable { defaultZ ->
            animate(
                z = z,
                duration = duration,
                interpolator = interpolator,
                startDelay = startDelay
            ).reverse(reverse) {
                z(defaultZ, duration, interpolator)
            }
        }
//endregion

//region RESIZE
private fun View.widthToCompletable(
    width: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null
): Completable =
    Completable.create {
        ValueAnimator.ofInt(this.width, width.dpToPx())
            .start(duration, interpolator,
                animationEnd = { it.onComplete() }) { value ->
                (value as? Int)?.also { layoutParams.width = it }
                requestLayout()
            }
    }

private fun View.heightToCompletable(
    height: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null
): Completable =
    Completable.create {
        ValueAnimator.ofInt(this.height, height.dpToPx())
            .start(duration, interpolator,
                animationEnd = { it.onComplete() }) { value ->
                (value as? Int)?.also { layoutParams.height = it }
                requestLayout()
            }
    }

/**
 * Set the width and height of your [View] in DP with animation and handle it in [Completable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param width [Int] param mandatory, in DP.
 * @param height [Int] param mandatory, in DP.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.

 * @param reverse [Boolean] optional, false by default.
 * @see View.width
 * @see View.height
 * @return A [Completable].
 */
fun View.resize(
    width: Int, height: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.width.pxToDp() to this.height.pxToDp())
        .flatMapCompletable { (defaultWidth, defaultHeight) ->
            width(width, duration, interpolator)
                .mergeWith(height(height, duration, interpolator))
                .reverse(reverse) {
                    resize(defaultWidth, defaultHeight, duration, interpolator)
                }
        }

/**
 * Set the width of your [View] in DP with animation and handle it in [Completable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param width [Int] param mandatory, in DP.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.resize
 * @see View.height
 * @return A [Completable].
 */
fun View.width(
    width: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.width)
        .flatMapCompletable { defaultWidth ->
            widthToCompletable(width, duration, interpolator)
                .reverse(reverse) {
                    widthToCompletable(defaultWidth, duration, interpolator)
                }
        }

/**
 * Set the height of your [View] in DP with animation and handle it in [Completable].
 * You can add a specific duration or interpolator.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param height [Int] param mandatory, in DP.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see View.resize
 * @see View.width
 * @return A [Completable].
 */
fun View.height(
    height: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.height)
        .flatMapCompletable { defaultHeight ->
            heightToCompletable(height, duration, interpolator)
                .reverse(reverse) {
                    heightToCompletable(defaultHeight, duration, interpolator)
                }
        }
//endregion

//region COLOR
private fun View.backgroundColorToCompletable(
    colorFrom: Int,
    colorTo: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null
): Completable =
    Completable.create {
        ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .start(duration, interpolator,
                animationEnd = { it.onComplete() }) { value ->
                (value as? Int)?.also { setBackgroundColor(it) }
            }
    }

/**
 * Set the background color of your [View] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param colorFrom [Int] param mandatory.
 * @param colorTo [Int] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @see backgroundColor
 * @return A [Completable].
 */
fun View.backgroundColor(
    colorFrom: Int,
    colorTo: Int,
    duration: Long? = null,
    interpolator: Interpolator? = null,
    reverse: Boolean = false
): Completable =
    backgroundColorToCompletable(colorFrom, colorTo, duration, interpolator)
        .reverse(reverse) {
            backgroundColorToCompletable(colorTo, colorFrom, duration, interpolator)
        }
//endregion

//region SHAKE
/**
 * Apply shake animation to your [View] and handle it in [Completable].
 *
 * @param duration [Long] in millisecond, 300ms by default.
 * @param nbShake [Float] for [CycleInterpolator], 2f by default.
 * @param shakeTranslation [Float] in DP for translation, 5f by default.
 * @see shake
 * @return A [Completable].
 */
fun View.shake(
    duration: Long = 300,
    nbShake: Float = 2f,
    shakeTranslation: Float = 5f
): Completable =
    Completable.create {
        animate().apply {
            this.duration = duration
            interpolator = CycleInterpolator(nbShake)
            translationX(-shakeTranslation.dpToPx())
            translationX(shakeTranslation.dpToPx())
        }.animate { it.onComplete() }
    }
//endregion

//region PRESS
/**
 * Apply press animation to your [View] and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 *
 * @param depth [Float] 0.95f by default.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @see press
 * @return A [Completable].
 */
fun View.press(
    depth: Float = 0.95f,
    duration: Long? = null,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null
): Completable =
    scale(depth, (duration ?: 300) / 2, interpolator, startDelay, reverse = true)
//endregion

//region TEXT
/**
 * Set the text of your [TextView] with animation and handle it in [Completable].
 * You can add a specific duration, interpolator or startDelay.
 * Used the reverse property to back to the init state of your
 * view with animation.
 *
 * @param text [String] param mandatory.
 * @param duration [Long] optional, null by default.
 * @param interpolator [TimeInterpolator] optional, null by default.
 * @param startDelay [Long] optional, null by default.
 * @param reverse [Boolean] optional, false by default.
 * @return A [Completable].
 */
fun TextView.text(
    text: String,
    duration: Long = 300L,
    interpolator: TimeInterpolator? = null,
    startDelay: Long? = null,
    reverse: Boolean = false
): Completable =
    Observable.just(this.text.toString())
        .flatMapCompletable { defaultText ->
            fadeOut(duration / 2, interpolator, startDelay)
                .doOnComplete { this.text = text }
                .andThen(fadeIn(duration / 2, interpolator, startDelay = 300L))
                .reverse(reverse) {
                    text(defaultText, duration, interpolator)
                }
        }
//endregion