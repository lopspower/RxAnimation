package com.mikhaellopez.rxanimation

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.support.annotation.ColorInt
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.Transformation

fun View.animate(alpha: Float? = null,
                 translationX: Float? = null,
                 translationY: Float? = null,
                 duration: Long? = null,
                 interpolator: TimeInterpolator? = null,
                 startDelay: Long? = null,
                 animationEnd: (() -> Unit)? = null) {
    animate().apply {
        alpha?.also { alpha(it) }
        translationX?.also { translationX(it) }
        translationY?.also { translationY(it) }
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

fun View.resizeHeightWithAnimation(fromHeight: Int, toHeight: Int,
                                   duration: Long? = null,
                                   interpolator: Interpolator? = null,
                                   animationEnd: (() -> Unit)? = null) {
    startAnimation(object : Animation() {
        override fun applyTransformation(interpolatedTime: Float,
                                         transformation: Transformation) {
            layoutParams.height =
                    if (interpolatedTime == 1f) toHeight
                    else (fromHeight + ((toHeight - fromHeight) * interpolatedTime)).toInt()
            requestLayout()
        }
    }.apply {
        duration?.also { this.duration = it }
        interpolator?.also { this.interpolator = it }
        animationEnd?.also { it.withDelay(this.duration) }
    })
}

fun View.setBackgroundColorWithAnimation(@ColorInt colorFrom: Int,
                                         @ColorInt colorTo: Int,
                                         duration: Long? = null,
                                         interpolator: Interpolator? = null,
                                         animationEnd: (() -> Unit)? = null) {
    ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .start(duration, interpolator, animationEnd) {
                setBackgroundColor(it as Int)
            }
}