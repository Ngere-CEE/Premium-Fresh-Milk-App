package com.example.premiumfreshmilk.activities


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.MilkParameter
import com.example.premiumfreshmilk.data.Transporters
import com.example.premiumfreshmilk.helpers.FirebaseCRUDHelper
import com.example.premiumfreshmilk.helpers.Utils.databaseRefence
import com.example.premiumfreshmilk.helpers.Utils.openActivity
import com.example.premiumfreshmilk.helpers.Utils.receiveMilkParameter
import com.example.premiumfreshmilk.helpers.Utils.show
import com.example.premiumfreshmilk.helpers.Utils.showInfoDialog
import com.example.premiumfreshmilk.helpers.Utils.valOf
import com.example.premiumfreshmilk.helpers.Utils.validateparam
import com.example.premiumfreshmilk.interfaces.IFirebaseLoadDelivery
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_crud.pb
import kotlinx.android.synthetic.main.activity_quality_crud.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QualityCrudActivity : AppCompatActivity(), IFirebaseLoadDelivery {
    lateinit var iFirebaseLoadDel: IFirebaseLoadDelivery
    //we'll have several instance fields
    private val c: Context = this@QualityCrudActivity
    private var milkParameter: MilkParameter? = null
    private val crudHelper = FirebaseCRUDHelper()
    private val db = databaseRefence
    var transporterList : List<Transporters> = ArrayList<Transporters>()
    /**
     * Delete data from firebase
     */
    private fun deleteData() {
        crudHelper.deleteparameter(this, db, pb, milkParameter!!)
    }
    /**
     * Get farmers list
     */
    private fun getTransporterData() {
        crudHelper.getTransporters( db, iFirebaseLoadDel)
    }
    /**
     * Warn user if he clicks the back button
     */
    override fun onBackPressed() {
        showInfoDialog(
            this,
            "Warning",
            "Are you sure you want to exit?"
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (milkParameter == null) {
            menuInflater.inflate(R.menu.new_quality_menu, menu)
            btn_saveparam!!.text = "Save"
        } else {
            menuInflater.inflate(R.menu.edit_quality_menu, menu)
            btn_saveparam!!.text = "Update"
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insertQMenuItem -> {
                insertData()
                return true
            }
            R.id.editQMenuItem -> {
                if (milkParameter != null) {
                    updateData()
                } else {
                    show(this, "EDIT ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.deleteQMenuItem -> {
                if (milkParameter != null) {
                    deleteData()
                } else {
                    show(this, "DELETE ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.viewAllQMenuItem -> {
                openActivity(this, QualityActivity::class.java)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Attach Base Context
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
    /**
     * When our activity is resumed we will receive our data and set them to their editing
     * widgets.
     */
    override fun onResume() {
        super.onResume()
        val s = receiveMilkParameter(intent, c)
        if (s != null) {
            milkParameter = s
            tqltynameTxt.setText(milkParameter!!.transporterQName)
            tqltynumTxt.setText(milkParameter!!.transporterQNumber)
            qltyweightTxt.setText(milkParameter!!.qualityWeight)
            butterfatTxt.setText(milkParameter!!.qualityButterfat)
            densityTxt.setText(milkParameter!!.qualityDensity)
            if(milkParameter!!.qualitySmell == "Good"){
                checkbox_smell!!.isChecked = true
            }
            if(milkParameter!!.qualityTaste == "Good"){
                checkbox_taste!!.isChecked = true
            }
        }
    }
    /**
     * Let's override our onCreate() method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_crud)
        val tsearchablespinner = findViewById<SearchableSpinner>(R.id.tQuality_spinner)
        val transnameTxt = findViewById<EditText>(R.id.tqltynameTxt)
        val transnumTxt = findViewById<EditText>(R.id.tqltynumTxt)
        transnameTxt.keyListener = null
        transnumTxt.keyListener = null
        //init interface
        iFirebaseLoadDel = this
        //load farmers data
        getTransporterData()
        tsearchablespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val transporter = transporterList[position]
                transnumTxt.setText(transporter.transporterNumber)
                transnameTxt.setText(transporter.transporterName)
            }
        }
        btn_saveparam.setOnClickListener {
            if (milkParameter == null) {
                insertData()
            }else{
                updateData()
            }
        }
    }
    override fun onDelivLoadSuccess(transporterList: List<Transporters>) {
        this.transporterList = transporterList
        //get all farmer number from list
        val trans = getTransNumberList(transporterList)
        /* create adapter */
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, trans)
        val search = findViewById<SearchableSpinner>(R.id.tQuality_spinner)
            search.adapter = adapter
    }

    private fun getTransNumberList(transporterList: List<Transporters>): List<String> {
        val result = ArrayList<String>()
        for (transporter in transporterList) {
            val finTrans: String = transporter.transporterNumber!! + " - "  + transporter.transporterName
            result.add(finTrans)
        }
        return  result
    }

    override fun onDelivLoadFailed(message: String) {

    }

    /**
     * Validate then insert data
     */
    @SuppressLint("SimpleDateFormat")
    private fun insertData() {
        val qualitySmell: String
        val qualityTaste: String
        if (validateparam(tqltynameTxt,tqltynumTxt,qltyweightTxt,butterfatTxt,densityTxt)) {
            if(checkbox_smell.isChecked) {
                qualitySmell = "Good"
            }else{
                qualitySmell = "Bad"
            }
            if(checkbox_taste.isChecked) {
                qualityTaste = "Good"
            }else{
                qualityTaste = "Bad"
            }
            //Get current date
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.isLenient = false
            val today = Date()
            val dateRecorded = dateFormatter.format(today)

            val newParam = MilkParameter(
                "",dateRecorded,
                valOf(tqltynameTxt),valOf(tqltynumTxt),valOf(qltyweightTxt),valOf(butterfatTxt),valOf(densityTxt),
                qualitySmell, qualityTaste
            )
            crudHelper.insertNewParam(this, db, pb, newParam)
        }
    }
    /**
     * Validate then update data
     */
    @SuppressLint("SimpleDateFormat")
    private fun updateData() {
        val qualitySmell: String
        val qualityTaste: String
        if (validateparam(tqltynameTxt,tqltynumTxt,qltyweightTxt,butterfatTxt,densityTxt)) {
            if(checkbox_smell.isChecked) {
                qualitySmell = "Good"
            }else{
                qualitySmell = "Bad"
            }
            if(checkbox_taste.isChecked) {
                qualityTaste = "Good"
            }else{
                qualityTaste = "Bad"
            }
            //Get current date
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.isLenient = false
            val today = Date()
            val dateRecorded = dateFormatter.format(today)
            val updatedParam = MilkParameter(
                milkParameter!!.key, dateRecorded,
                milkParameter!!.transporterQName, milkParameter!!.transporterQNumber, valOf(qltyweightTxt), valOf(butterfatTxt), valOf(densityTxt),
                qualitySmell, qualityTaste
            )
            crudHelper.updateParam(this, db, pb, updatedParam)
        }
    }
}