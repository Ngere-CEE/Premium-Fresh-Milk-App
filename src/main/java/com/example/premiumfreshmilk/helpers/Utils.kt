package com.example.premiumfreshmilk.helpers

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.activities.DashboardActivity
import com.example.premiumfreshmilk.data.MilkParameter
import com.example.premiumfreshmilk.data.ReceivedMilk
import com.example.premiumfreshmilk.data.Scientist
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yarolegovich.lovelydialog.LovelyStandardDialog

import java.util.*

object Utils {
    const val DATE_FORMAT = "yyyy-MM-dd"
    var DataCache: ArrayList<Scientist> = ArrayList()
    var DataCache2: ArrayList<ReceivedMilk> = ArrayList()
    var DataCache3: ArrayList<MilkParameter> = ArrayList()
    var searchString = ""
    /**
     * This method allows you easily show a toast message from any activity
     * @param c
     * @param message
     */
    @JvmStatic
    fun show(c: Context?, message: String?) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * You can pass an arbitrary number of edittexts into this method, then
     * we pick the first three and validate them
     * @param editTexts
     * @return
     */
    fun validate(vararg editTexts: EditText): Boolean {
        val farmernamTxt = editTexts[0]
        val farmernumTxt = editTexts[1]
        val weightTxt = editTexts[2]

        if (farmernamTxt.text == null || farmernamTxt.text.toString().isEmpty()) {
            farmernamTxt.error = "Kindly select farmer!"
            return false
        }
        if (farmernumTxt.text == null || farmernumTxt.text.toString().isEmpty()) {
            farmernumTxt.error = "Kindly select farmer!"
            return false
        }
        if (weightTxt.text == null || weightTxt.text.toString().isEmpty()) {
            weightTxt.error = "Weight is Required Please!"
            return false
        }

        return true
    }
    fun validatedel(vararg editTexts: EditText): Boolean {
        val transnameTxt = editTexts[0]
        val transnumTxt = editTexts[1]
        val delvweightTxt = editTexts[2]

        if (transnameTxt.text == null || transnameTxt.text.toString().isEmpty()) {
            transnameTxt.error = "Kindly select transporter!"
            return false
        }
        if (transnumTxt.text == null || transnumTxt.text.toString().isEmpty()) {
            transnumTxt.error = "Kindly select transporter!"
            return false
        }
        if (delvweightTxt.text == null || delvweightTxt.text.toString().isEmpty()) {
            delvweightTxt.error = "Weight is Required Please!"
            return false
        }
        return true
    }

    fun validateparam(vararg editTexts: EditText): Boolean {
        val tqltynameTxt = editTexts[0]
        val tqltynumTxt = editTexts[1]
        val qltyweightTxt = editTexts[2]
        val butterfatTxt = editTexts[3]
        val densityTxt = editTexts[4]

        if (tqltynameTxt.text == null || tqltynameTxt.text.toString().isEmpty()) {
            tqltynameTxt.error = "Kindly select transporter!"
            return false
        }
        if (tqltynumTxt.text == null || tqltynumTxt.text.toString().isEmpty()) {
            tqltynumTxt.error = "Kindly select transporter!"
            return false
        }
        if (qltyweightTxt.text == null || qltyweightTxt.text.toString().isEmpty()) {
            qltyweightTxt.error = "Weight is Required Please!"
            return false
        }
        if (butterfatTxt.text == null || butterfatTxt.text.toString().isEmpty()) {
            butterfatTxt.error = "Butterfat is required!"
            return false
        }
        if (densityTxt.text == null || densityTxt.text.toString().isEmpty()) {
            densityTxt.error = "Density is required!"
            return false
        }
        return true
    }

    /**
     * This method will allow easily open any activity
     * @param c
     * @param clazz
     */
    @JvmStatic
    fun openActivity(c: Context, clazz: Class<*>?) {
        val intent = Intent(c, clazz)
        c.startActivity(intent)
    }

    /**
     * This method will allow us show an Info dialog anywhere in our app.
     */
    @JvmStatic
    fun showInfoDialog(activity: AppCompatActivity, title: String?, message: String?) {
        LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
            .setTopColorRes(R.color.indigo)
            .setButtonsColorRes(R.color.darkDeepOrange)
            .setIcon(R.drawable.m_info)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { }
            .setNeutralButton("Home") {
                openActivity(activity, DashboardActivity::class.java)
            }
            .setNegativeButton("Back") { activity.finish() }
            .show()
    }
    /**
     * This method will allow us send a serialized scientist objec  to a specified
     * activity
     */
    fun sendScientistToActivity(c: Context, scientist: Scientist?, clazz: Class<*>?) {
        val i = Intent(c, clazz)
        i.putExtra("SCIENTIST_KEY", scientist)
        c.startActivity(i)
    }
    fun sendReceivedmilkToActivity(c: Context, receivedMilk: ReceivedMilk?, clazz: Class<*>?) {
        val i = Intent(c, clazz)
        i.putExtra("RECEIVEDMILK_KEY", receivedMilk)
        c.startActivity(i)
    }

    fun sendMilkParametersActivity(c: Context, milkParameter: MilkParameter?, clazz: Class<*>?) {
        val i = Intent(c, clazz)
        i.putExtra("MILKPARAMETER_KEY", milkParameter)
        c.startActivity(i)
    }

    /**
     * This method will allow us receive a serialized scientist, deserialize it and return it,.
     */
    fun receiveScientist(intent: Intent, c: Context?): Scientist? {
        try {
            return intent.getSerializableExtra("SCIENTIST_KEY") as Scientist
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun receiveDeliveredMilk(intent: Intent, c: Context?): ReceivedMilk? {
        try {
            return intent.getSerializableExtra("RECEIVEDMILK_KEY") as ReceivedMilk
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun receiveMilkParameter(intent: Intent, c: Context?): MilkParameter? {
        try {
            return intent.getSerializableExtra("MILKPARAMETER_KEY") as MilkParameter
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun showProgressBar(pb: ProgressBar) {
        pb.visibility = View.VISIBLE
    }

    @JvmStatic
    fun hideProgressBar(pb: ProgressBar) {
        pb.visibility = View.GONE
    }

    @JvmStatic
    fun valOf(editText: EditText): String{
        if (editText.text==null){
            return ""
        }
        return editText.text.toString()
    }

    val databaseRefence: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference
}