package com.mikhaellopez.rxanimation

import android.os.Handler

fun (() -> Any).withDelay(delay: Long = 300) {
    Handler().postDelayed({ this.invoke() }, delay)
}