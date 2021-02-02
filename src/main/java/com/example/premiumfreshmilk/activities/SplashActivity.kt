package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.helpers.Utils.openActivity

import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SplashActivity : AppCompatActivity() {
    //our splash screen views
    private var mLogo: ImageView? = null
    private var mainTitle: TextView? = null
    private var subTitle: TextView? = null
    /**
     * Let's initialize our widgets.
     */
    private fun initializeWidgets() {
        mLogo = findViewById(R.id.mLogo)
        mainTitle = findViewById(R.id.mainTitle)
        subTitle = findViewById(R.id.subTitle)
    }

    /**
     * Let's show our Splash animation using Animation class. We fade in our widgets.
     */
    private fun showSplashAnimation() {
        val animation = AnimationUtils.loadAnimation(
            this,
                R.anim.top_to_bottom
        )
        mLogo!!.startAnimation(animation)
        val fadeIn =
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        mainTitle!!.startAnimation(fadeIn)
        subTitle!!.startAnimation(fadeIn)
    }

    /**
     * Let's go to our DashBoard after 2 seconds
     */
    private fun goToDashboard() {
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                    openActivity(
                        this@SplashActivity,
                        DashboardActivity::class.java
                    )
                    finish()
                    super.run()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        t.start()
    }

    /**
     * Let's Override attachBaseContext method. This is needed if you want to
     * show the custom fonts in this activity as ell.
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * Let's create our onCreate method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initializeWidgets()
        showSplashAnimation()
        goToDashboard()
    }
}