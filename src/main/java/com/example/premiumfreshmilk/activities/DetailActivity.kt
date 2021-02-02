package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.Scientist
import com.example.premiumfreshmilk.helpers.Utils.receiveScientist
import com.example.premiumfreshmilk.helpers.Utils.sendScientistToActivity

import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_content.*

class DetailActivity : AppCompatActivity() {
    private var receivedScientist: Scientist? = null

    /**
     * Let's receive our data and show them in their respective widgets
     */
    private fun receiveAndShowData() {
        receivedScientist = receiveScientist(intent, this@DetailActivity)
        if (receivedScientist != null) {
            doclnTV.text = receivedScientist!!.docln
            nameTV.text = receivedScientist!!.fullName
            fnumberTV.text = receivedScientist!!.farmerNumber
            weightTV.text = receivedScientist!!.weight
            mCollapsingToolbarLayout!!.title = receivedScientist!!.fullName
        }
        mCollapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))

        editFAB.setOnClickListener {
            sendScientistToActivity(this, receivedScientist, CRUDActivity::class.java)
            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_page_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_edit -> {
                sendScientistToActivity(this, receivedScientist, CRUDActivity::class.java)
                return true
            }
            android.R.id.home -> {
                // Navigate back to parent activity
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Inject custom font to this activity as well
     * @param newBase
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        receiveAndShowData()

    }
}