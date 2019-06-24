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

    companion object {
        private const val ANIMATION_DURATION = 1000L // 1s
    }

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
            RxAnimation.from(view)
                    .fadeIn(ANIMATION_DURATION)
                    .translation(30f, 50f, ANIMATION_DURATION)
                    .backgroundColor(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            ANIMATION_DURATION
                    )
                    .resize(200, 200, ANIMATION_DURATION)
                    .rotation(180f, ANIMATION_DURATION)
                    .shake()
        }.subscribe().addTo(composite)
    }

    private fun sequentiallySample() {
        RxView.clicks(content).flatMapCompletable {
            RxAnimation.sequentially(
                    view.fadeIn(ANIMATION_DURATION),
                    view.translation(30f, 50f, ANIMATION_DURATION),
                    view.backgroundColor(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            ANIMATION_DURATION
                    ),
                    view.resize(200, 200, ANIMATION_DURATION),
                    view.rotation(180f, ANIMATION_DURATION),
                    view.shake()
            )
        }.subscribe().addTo(composite)
    }

    private fun togetherSample() {
        RxView.clicks(content).flatMapCompletable {
            RxAnimation.together(
                    view.alpha(1f, ANIMATION_DURATION),
                    view.translation(30f, 50f, ANIMATION_DURATION),
                    view.backgroundColor(
                            ContextCompat.getColor(this, R.color.accent),
                            ContextCompat.getColor(this, R.color.primary),
                            ANIMATION_DURATION
                    ),
                    view.resize(200, 200, ANIMATION_DURATION),
                    view.rotation(180f, ANIMATION_DURATION),
                    view.shake()
            )
        }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

}