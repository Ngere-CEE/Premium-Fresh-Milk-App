package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.MilkParameter
import com.example.premiumfreshmilk.helpers.Utils.receiveMilkParameter
import com.example.premiumfreshmilk.helpers.Utils.sendMilkParametersActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_quality_detail.*
import kotlinx.android.synthetic.main.activity_trans_detail_ativity.*
import kotlinx.android.synthetic.main.detail_content_trans.*
import kotlinx.android.synthetic.main.quality_detail_content.*

class QualityDetailActivity : AppCompatActivity() {
    private var receiveMilkParameter: MilkParameter? = null

    /**
     * Let's receive our data and show them in their respective widgets
     */
    private fun receiveAndShowData() {
        receiveMilkParameter = receiveMilkParameter(intent, this@QualityDetailActivity)
        if (receiveMilkParameter != null) {
            doclnqltyTV.text = receiveMilkParameter!!.dateRecorded
            nameqltyTV.text = receiveMilkParameter!!.transporterQName
            tnumberqltyTV.text = receiveMilkParameter!!.transporterQNumber
            qltyWeightTV.text = receiveMilkParameter!!.qualityWeight
            butterfatTV.text = receiveMilkParameter!!.qualityButterfat
            densityTV.text = receiveMilkParameter!!.qualityDensity
            smellTV.text = receiveMilkParameter!!.qualitySmell
            tasteTV.text = receiveMilkParameter!!.qualityTaste
            qCollapsingToolbarLayout!!.title = receiveMilkParameter!!.transporterQName
        }
        qCollapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))

        editQP.setOnClickListener {
            sendMilkParametersActivity(this, receiveMilkParameter, QualityCrudActivity::class.java)
            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.quality_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_editqlty -> {
                sendMilkParametersActivity(this, receiveMilkParameter, QualityCrudActivity::class.java)
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
        setContentView(R.layout.activity_quality_detail)
        receiveAndShowData()

    }
}