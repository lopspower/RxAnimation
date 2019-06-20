package com.mikhaellopez.rxanimationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.mikhaellopez.rxanimation.*
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
        fromViewSample()
        //sequentiallySample()
        //togetherSample()
    }

    private fun fromViewSample() {
        RxView.clicks(content).flatMap {
            RxAnimation.fromView(view)
                    .fadeIn(2000)
                    .setTranslation(300f, 500f, 2000)
                    .setBackgroundColor(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            2000
                    )
                    .resize(200, 200, 2000)
        }.subscribe().addTo(composite)
    }

    private fun sequentiallySample() {
        RxView.clicks(content).flatMapCompletable {
            RxAnimation.sequentially(
                    view.fadeIn(2000),
                    view.setTranslationToCompletable(300f, 500f, 2000),
                    view.setBackgroundColorToCompletable(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            2000
                    ),
                    view.resizeToCompletable(200, 200, 2000)
            )
        }.subscribe().addTo(composite)
    }

    private fun togetherSample() {
        RxView.clicks(content).flatMapCompletable {
            RxAnimation.together(
                    view.setAlphaToCompletable(1f, 2000),
                    view.setTranslationToCompletable(300f, 500f, 2000),
                    view.setBackgroundColorToCompletable(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            2000
                    ),
                    view.resizeToCompletable(200, 200, 2000)
            )
        }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

}