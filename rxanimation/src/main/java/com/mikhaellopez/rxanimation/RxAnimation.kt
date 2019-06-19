package com.mikhaellopez.rxanimation

import io.reactivex.Completable

object RxAnimation {

    fun sequentially(vararg completables: Completable): Completable =
            completables.first().run {
                if (completables.size > 1) {
                    andThen(sequentially(*completables.toMutableList()
                            .apply { removeAt(0) }.toTypedArray()))
                } else this
            }

}