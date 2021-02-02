package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.helpers.Utils.openActivity
import com.google.firebase.auth.FirebaseAuth

import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    /**
     * Let's initialize our cards  and listen to their click events
     */
    private fun initializeWidgets() {
        viewScientistsCard.setOnClickListener {
            openActivity(
                this@DashboardActivity,
                ScientistsActivity::class.java
            )
        }
        addScientistCard.setOnClickListener {
            openActivity(
                this@DashboardActivity,
                CRUDActivity::class.java
            )
        }
        third.setOnClickListener {
            openActivity(
                    this@DashboardActivity,
                    TransporterDelivery::class.java
            )
        }
        qualityCheck.setOnClickListener {
            openActivity(
                this@DashboardActivity,
                QualityActivity::class.java
            )
        }

    }

    /**
     * Let's override the attachBaseContext() method so that custom fonts can
     * be used here as well
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * When the back button is pressed finish this activity
     */
    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    /**
     * Let's override the onCreate() and call our initializeWidgets()
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initializeWidgets()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                openActivity(this, LoginActivity::class.java)
                finish()
                return true
            }
            R.id.action_exit -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}