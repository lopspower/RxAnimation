package com.mikhaellopez.rxanimation

import io.reactivex.Completable
import io.reactivex.functions.BiFunction

fun Completable.zipWith(completable: Completable): Completable =
        toSingleDefault(Unit).zipWith(completable.toSingleDefault(Unit),
                BiFunction { _: Unit, _: Unit -> Unit }
        ).ignoreElement()