package com.mikhaellopez.rxanimationsample

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding4.view.clicks
import com.mikhaellopez.rxanimation.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
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
        val cardView = findViewById<CardView>(R.id.cardView)
        val fab = findViewById<FrameLayout>(R.id.fab)
        val text = findViewById<LinearLayout>(R.id.text)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val btnAlpha = findViewById<Button>(R.id.btnAlpha)
        val btnTranslation = findViewById<Button>(R.id.btnTranslation)
        val btnScale = findViewById<Button>(R.id.btnScale)
        val btnRotation = findViewById<Button>(R.id.btnRotation)
        val btnBackgroundColor = findViewById<Button>(R.id.btnBackgroundColor)
        val btnResize = findViewById<Button>(R.id.btnResize)
        val btnShake = findViewById<Button>(R.id.btnShake)
        val btnPress = findViewById<Button>(R.id.btnPress)
        val btnCustomProperties = findViewById<Button>(R.id.btnCustomProperties)
        val cardCustomProperties = findViewById<CardView>(R.id.cardCustomProperties)
        val btnText = findViewById<CardView>(R.id.btnText)
        val textToSetText = findViewById<TextView>(R.id.textToSetText)

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
        btnAlpha.throttleClick()
            .flatMapCompletable {
                btnAlpha.fadeOut(ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)

        btnTranslation.throttleClick()
            .switchMapCompletable {
                btnTranslation.translation(500f, 500f, ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)

        btnScale.throttleClick()
            .switchMapCompletable {
                btnScale.scale(0f, ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)

        btnRotation.throttleClick()
            .switchMapCompletable {
                btnRotation.rotation(360f, ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)

        btnBackgroundColor.throttleClick()
            .switchMapCompletable {
                btnBackgroundColor.backgroundColor(
                    ContextCompat.getColor(this, R.color.primary),
                    ContextCompat.getColor(this, R.color.accent),
                    ANIMATION_DURATION, reverse = true
                )
            }.subscribe().addTo(composite)

        btnResize.throttleClick()
            .switchMapCompletable {
                btnResize.resize(0, 0, ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)

        btnShake.throttleClick(ANIMATION_DURATION)
            .switchMapCompletable {
                btnShake.shake()
            }.subscribe().addTo(composite)

        btnPress.throttleClick(ANIMATION_DURATION)
            .switchMapCompletable {
                btnPress.press()
            }.subscribe().addTo(composite)

        btnCustomProperties.throttleClick()
            .flatMapCompletable {
                (0f to 30f).rangeFloatToCompletable(ANIMATION_DURATION, reverse = true) {
                    cardCustomProperties?.radius = it.dpToPx()
                }
            }.subscribe().addTo(composite)

        btnText.throttleClick(ANIMATION_DURATION)
            .switchMapCompletable {
                textToSetText.text("Amazing", ANIMATION_DURATION, reverse = true)
            }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

    private fun View.throttleClick(duration: Long = ANIMATION_DURATION * 2): Observable<Unit> =
        clicks().throttleFirst(duration, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())

    private fun Float.dpToPx(): Float =
        this * Resources.getSystem().displayMetrics.density

}