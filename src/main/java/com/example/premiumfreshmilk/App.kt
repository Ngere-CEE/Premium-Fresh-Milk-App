package com.example.premiumfreshmilk

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class App : Application() {
    /**
     * Set application wide font to use here. This method gets called even before
     * our activities are created. Hence it is good for initializations.
     * Don't forget to register in the androidmanifest: android:name=".App"
     */
    override fun onCreate() {
        super.onCreate()
        //permanently cache data to disk so that we can access data even when offline
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        //initialize calligraphy, our fonts library before UI is ready
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/RobotoBold.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}