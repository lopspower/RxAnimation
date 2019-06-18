package com.mikhaellopez.rxanimation

import android.animation.ValueAnimator
import android.os.Handler
import io.reactivex.Completable
import io.reactivex.functions.BiFunction

fun Completable.zipWith(completable: Completable): Completable =
        toSingleDefault(Unit).zipWith(completable.toSingleDefault(Unit),
                BiFunction { _: Unit, _: Unit -> Unit }
        ).ignoreElement()

fun Pair<Float, Float>.applyFloatAnimation(duration: Long? = null,
                                           animationEnd: (() -> Unit)? = null,
                                           action: (Float) -> Unit) {
    ValueAnimator.ofFloat(first, second).apply {
        duration?.also { this.duration = it }
        addUpdateListener { animation -> action(animation.animatedValue as Float) }
        animationEnd?.also { Handler().postDelayed({ it.invoke() }, duration ?: this.duration) }
    }.start()
}

fun Pair<Float, Float>.completableAnimation(duration: Long? = null, action: (Float) -> Unit): Completable =
        Completable.create {
            applyFloatAnimation(duration, animationEnd = {
                it.onComplete()
            }, action = action)
        }