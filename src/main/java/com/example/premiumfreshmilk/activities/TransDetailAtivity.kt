package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.ReceivedMilk
import com.example.premiumfreshmilk.helpers.Utils.sendReceivedmilkToActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import com.example.premiumfreshmilk.helpers.Utils.receiveDeliveredMilk
import kotlinx.android.synthetic.main.activity_trans_detail_ativity.*
import kotlinx.android.synthetic.main.detail_content_trans.*

class TransDetailAtivity : AppCompatActivity() {
    private var receiveDeliveredMilk: ReceivedMilk? = null

    /**
     * Let's receive our data and show them in their respective widgets
     */
    private fun receiveAndShowData() {
        receiveDeliveredMilk = receiveDeliveredMilk(intent, this@TransDetailAtivity)
        if (receiveDeliveredMilk != null) {
            datedeliveredTV.text = receiveDeliveredMilk!!.datedelivered
            tnameTV.text = receiveDeliveredMilk!!.transporterName
            tnumberTV.text = receiveDeliveredMilk!!.transporterNumber
            tweightTV.text = receiveDeliveredMilk!!.deliveredWeight
            mtCollapsingToolbarLayout!!.title = receiveDeliveredMilk!!.transporterName
        }
        mtCollapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))

        editReceived.setOnClickListener {
            sendReceivedmilkToActivity(this, receiveDeliveredMilk, TransCrudActivity::class.java)
            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.trans_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_editt -> {
                sendReceivedmilkToActivity(this, receiveDeliveredMilk, TransCrudActivity::class.java)
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
        setContentView(R.layout.activity_trans_detail_ativity)
        receiveAndShowData()

    }
}