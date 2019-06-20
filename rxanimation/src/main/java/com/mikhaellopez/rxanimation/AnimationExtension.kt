package com.mikhaellopez.rxanimation

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator

private fun (() -> Any).withDelay(delay: Long = 300) {
    Handler().postDelayed({ this.invoke() }, delay)
}

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
                 startDelay: Long? = null,
                 animationEnd: (() -> Unit)? = null) {
    animate().apply {
        alpha?.also { alpha(it) }
        translationX?.also { translationX(it) }
        translationY?.also { translationY(it) }
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
    }.withEndAction { animationEnd?.invoke() }.start()
}

fun ValueAnimator.start(duration: Long? = null,
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

fun View.setBackgroundColorWithAnimation(colorFrom: Int,
                                         colorTo: Int,
                                         duration: Long? = null,
                                         interpolator: Interpolator? = null,
                                         animationEnd: (() -> Unit)? = null) {
    ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .start(duration, interpolator, animationEnd) { setBackgroundColor(it as Int) }
}

//region RESIZE
private fun View.dpToPixel(dp: Int): Int =
        (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun View.setWidthWithAnimation(width: Int,
                               duration: Long? = null,
                               interpolator: Interpolator? = null,
                               animationEnd: (() -> Unit)? = null) {
    ValueAnimator.ofInt(this.width, dpToPixel(width))
            .start(duration, interpolator, animationEnd) {
                layoutParams.width = it as Int
                requestLayout()
            }
}

fun View.setHeightWithAnimation(height: Int,
                                duration: Long? = null,
                                interpolator: Interpolator? = null,
                                animationEnd: (() -> Unit)? = null) {
    ValueAnimator.ofInt(this.height, dpToPixel(height))
            .start(duration, interpolator, animationEnd) {
                layoutParams.height = it as Int
                requestLayout()
            }
}
//endregion