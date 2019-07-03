package com.mikhaellopez.rxanimationsample

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.clicks
import com.mikhaellopez.rxanimation.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ANIMATION_DURATION = 700L // 0,7s
    }

    private val composite = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        RxAnimation.sequentially(
            cardView.scale(1f, ANIMATION_DURATION),
            fab.scale(1f, ANIMATION_DURATION),
            RxAnimation.together(
                fab.rotation(360f, ANIMATION_DURATION),
                text.fadeIn(ANIMATION_DURATION)
            ),
            progressBar.fadeIn(ANIMATION_DURATION)
        ).subscribe().addTo(composite)

        // DEMO
        btnAlpha.clicks().throttle()
            .switchMap {
                RxAnimation.from(btnAlpha)
                    .fadeOut(ANIMATION_DURATION)
                    .fadeIn(ANIMATION_DURATION)
            }.subscribe().addTo(composite)

        btnTranslation.clicks().throttle()
            .switchMap {
                RxAnimation.from(btnTranslation)
                    .translation(500f, 500f, ANIMATION_DURATION)
                    .translation(0f, 0f, ANIMATION_DURATION)
            }.subscribe().addTo(composite)

        btnScale.clicks().throttle()
            .switchMap {
                RxAnimation.from(btnScale)
                    .scale(0f, ANIMATION_DURATION)
                    .scale(1f, ANIMATION_DURATION)
            }.subscribe().addTo(composite)

        btnRotation.clicks().throttle()
            .switchMap {
                RxAnimation.from(btnRotation)
                    .rotation(360f, ANIMATION_DURATION)
                    .rotation(0f, ANIMATION_DURATION)
            }.subscribe().addTo(composite)

        btnBackgroundColor.clicks().throttle()
            .switchMap {
                RxAnimation.from(btnBackgroundColor)
                    .backgroundColor(
                        ContextCompat.getColor(this, R.color.primary),
                        ContextCompat.getColor(this, R.color.accent),
                        ANIMATION_DURATION
                    )
                    .backgroundColor(
                        ContextCompat.getColor(this, R.color.accent),
                        ContextCompat.getColor(this, R.color.primary),
                        ANIMATION_DURATION
                    )
            }.subscribe().addTo(composite)

        btnResize.clicks().throttle()
            .switchMap {
                val width = btnResize.width.pxToDp()
                val height = btnResize.height.pxToDp()

                RxAnimation.from(btnResize)
                    .resize(0, 0, ANIMATION_DURATION)
                    .resize(width, height, ANIMATION_DURATION)
            }.subscribe().addTo(composite)

        btnShake.clicks().throttle()
            .switchMapCompletable {
                btnShake.shake()
            }.subscribe().addTo(composite)

        btnPress.clicks().throttle()
            .switchMapCompletable {
                btnPress.press()
            }.subscribe().addTo(composite)

        btnCustomProperties.clicks().throttle()
            .flatMapCompletable {
                (0f to 30f).rangeFloatToCompletable(ANIMATION_DURATION / 2) {
                    cardCustomProperties.radius = it.dpToPx()
                }.andThen((30f to 0f).rangeFloatToCompletable(ANIMATION_DURATION / 2) {
                    cardCustomProperties.radius = it.dpToPx()
                })
            }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

    private fun Observable<Unit>.throttle(): Observable<Unit> =
        throttleFirst(ANIMATION_DURATION, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())

    private fun Int.pxToDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Float.dpToPx(): Float =
        this * Resources.getSystem().displayMetrics.density

}