package com.mikhaellopez.rxanimation

import android.view.View
import io.reactivex.Completable
import io.reactivex.Observable

object RxAnimation {

    fun from(view: View): Observable<View> = Observable.just(view)

    fun together(vararg completables: Completable): Completable =
            Completable.mergeArray(*completables)

    fun sequentially(vararg completables: Completable): Completable =
            completables.first().run {
                if (completables.size > 1) {
                    andThen(sequentially(*completables.toMutableList()
                            .apply { removeAt(0) }.toTypedArray()
                    ))
                } else this
            }

}