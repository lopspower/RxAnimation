package com.mikhaellopez.rxanimation

import io.reactivex.Completable

object RxAnimation {

    fun sequentially(vararg completables: Completable): Completable =
            completables.firstOrNull()?.apply {
                val data = completables.toMutableList().let {
                    it.removeAt(0)
                    sequentially(*it.toTypedArray())
                }
                andThen(data)
            } ?: Completable.complete()

}