package com.mikhaellopez.rxanimation

import android.view.View
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * RxAnimation is an object that gives you access to static methods
 * to enhance your experience for animating your views with Rx.
 */
object RxAnimation {

    /**
     * Create an [Observable] of your [View].
     *
     * @param view [View] param mandatory.
     * @return An [Observable] of [View].
     */
    fun from(view: View): Observable<View> = Observable.just(view)

    /**
     * Run all the [Completable] together (simultaneously).
     *
     * @param completables vararg of [Completable] param mandatory.
     * @see from
     * @return A [Completable].
     */
    fun together(vararg completables: Completable): Completable =
        Completable.mergeArray(*completables)

    /**
     * Run all the [Completable] sequentially (one by one).
     *
     * @param completables vararg of [Completable] param mandatory.
     * @see from
     * @return A [Completable].
     */
    fun sequentially(vararg completables: Completable): Completable =
        completables.first().run {
            if (completables.size > 1) {
                andThen(
                    sequentially(
                        *completables.toMutableList()
                            .apply { removeAt(0) }.toTypedArray()
                    )
                )
            } else this
        }

}