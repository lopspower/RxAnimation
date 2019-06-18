package com.mikhaellopez.rxanimationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.mikhaellopez.rxanimation.completableAnimation
import com.mikhaellopez.rxanimation.zipWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val composite = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        RxView.clicks(view)
                .flatMapCompletable {
                    (0f to 1f).completableAnimation(3000) {
                        view?.alpha = it
                    }.andThen(
                            (0f to 100f).completableAnimation(2000) { view?.translationX = it }
                                    .zipWith(
                                            (0f to 200f).completableAnimation(2000) { view?.translationY = it }
                                    )
                    )
                }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

}