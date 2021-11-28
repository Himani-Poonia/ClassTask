package com.example.classtask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    private var splashImageView: ImageView? = null
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        supportActionBar?.hide()
        splashImageView = findViewById(R.id.splashImageView)

        animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onStart() {
        super.onStart()
        splashImageView?.startAnimation(animation)
    }
}