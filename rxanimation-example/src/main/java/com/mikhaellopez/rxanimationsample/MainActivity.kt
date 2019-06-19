package com.mikhaellopez.rxanimationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.setAlphaToCompletable
import com.mikhaellopez.rxanimation.setBackgroundColorToCompletable
import com.mikhaellopez.rxanimation.setTranslationToCompletable
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

        RxView.clicks(content)
                .flatMapCompletable {
                    RxAnimation.sequentially(
                            view.setAlphaToCompletable(1f, 2000),
                            view.setTranslationToCompletable(300f, 500f, 2000),
                            view.setBackgroundColorToCompletable(
                                    ContextCompat.getColor(this, R.color.accent),
                                    ContextCompat.getColor(this, R.color.primary),
                                    1000)
                    )
                }.subscribe().addTo(composite)
    }

    override fun onPause() {
        super.onPause()
        composite.clear()
    }

}