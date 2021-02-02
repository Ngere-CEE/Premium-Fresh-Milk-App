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
import com.example.premiumfreshmilk.interfaces.IFirebaseLoadDone
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.Scientist
import com.example.premiumfreshmilk.data.farmer
import com.example.premiumfreshmilk.helpers.FirebaseCRUDHelper
import com.example.premiumfreshmilk.helpers.Utils.databaseRefence
import com.example.premiumfreshmilk.helpers.Utils.openActivity
import com.example.premiumfreshmilk.helpers.Utils.receiveScientist
import com.example.premiumfreshmilk.helpers.Utils.show
import com.example.premiumfreshmilk.helpers.Utils.showInfoDialog
import com.example.premiumfreshmilk.helpers.Utils.valOf
import com.example.premiumfreshmilk.helpers.Utils.validate
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_crud.*

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CRUDActivity : AppCompatActivity(),
    IFirebaseLoadDone {
    lateinit var iFirebaseLoadDone: IFirebaseLoadDone
    //we'll have several instance fields
    private val c: Context = this@CRUDActivity
    private var receivedScientist: Scientist? = null
    private val crudHelper = FirebaseCRUDHelper()
    private val db = databaseRefence
    var isFirstTimeClick = true
    var farmerList : List<farmer> = ArrayList<farmer>()
    /**
     * Delete data from firebase
     */
    private fun deleteData() {
        crudHelper.delete(this, db, pb, receivedScientist!!)
    }
    /**
     * Get farmers list
     */
    private fun getFarmerData() {
        crudHelper.getFarmers( db, iFirebaseLoadDone)
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
        if (receivedScientist == null) {
            menuInflater.inflate(R.menu.new_item_menu, menu)
            btn_savecollection!!.text = "save"
        } else {
            menuInflater.inflate(R.menu.edit_item_menu, menu)
            btn_savecollection!!.text = "Update"
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insertMenuItem -> {
                insertData()
                return true
            }
            R.id.editMenuItem -> {
                if (receivedScientist != null) {
                    updateData()
                } else {
                    show(this, "EDIT ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.deleteMenuItem -> {
                if (receivedScientist != null) {
                    deleteData()
                } else {
                    show(this, "DELETE ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.viewAllMenuItem -> {
                openActivity(this, ScientistsActivity::class.java)
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
        val s = receiveScientist(intent, c)
        if (s != null) {
            receivedScientist = s
            weightTxt.setText(receivedScientist!!.weight)
            farmernamTxt.setText(receivedScientist!!.fullName)
            farmernumTxt.setText(receivedScientist!!.farmerNumber)
        }
    }
    /**
     * Let's override our onCreate() method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)
        val searchablespinner = findViewById<SearchableSpinner>(R.id.searchable_spinner)
        val farmernamTxt = findViewById<EditText>(R.id.farmernamTxt)
        val farmernumTxt = findViewById<EditText>(R.id.farmernumTxt)
        farmernamTxt.setKeyListener(null);
        farmernumTxt.setKeyListener(null);
        //init interface
        iFirebaseLoadDone = this
        //load farmers data
        getFarmerData()
        searchablespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val farmer = farmerList[position]
                    farmernumTxt.setText(farmer.farmerNumber)
                    farmernamTxt.setText(farmer.fullName)
            }
        }
        btn_savecollection.setOnClickListener {
            if (receivedScientist == null) {
                insertData()
            }else{
                updateData()
            }
        }
    }
    override fun onFirebaseLoadSuccess(farmerList: List<farmer>) {
        this.farmerList = farmerList
        //get all farmer number from list
        var farmer_number = getFarmerNumberList(farmerList)
        //create adapter
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, farmer_number)
        val searchablespinner = findViewById<SearchableSpinner>(R.id.searchable_spinner)
        searchablespinner.adapter = adapter

    }

    private fun getFarmerNumberList(farmerList: List<farmer>): List<String> {
        val result = ArrayList<String>()
        for (farmer in farmerList) {
            val finFarmer: String = farmer.farmerNumber!! + " - "  + farmer.fullName
            result.add(finFarmer)
        }
        return  result
    }

    override fun onFirebaseLoadFailed(message: String) {

    }

    /**
     * Validate then insert data
     */
    @SuppressLint("SimpleDateFormat")
    private fun insertData() {
        if (validate(farmernamTxt,farmernumTxt,weightTxt)) {
                //Get current date
                val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                dateFormatter.isLenient = false
                val today = Date()
                val docln = dateFormatter.format(today)
                val newScientist = Scientist("",docln,valOf(farmernamTxt),valOf(farmernumTxt),valOf(weightTxt))
                crudHelper.insert(this, db, pb, newScientist)
        }
    }
    /**
     * Validate then update data
     */
    @SuppressLint("SimpleDateFormat")
    private fun updateData() {
        if (validate(farmernamTxt,farmernumTxt,weightTxt)) {
            //Get current date
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.isLenient = false
            val today = Date()
            val docln = dateFormatter.format(today)

            val updatedScientist =
                    Scientist(receivedScientist!!.key,docln,receivedScientist!!.fullName,
                            receivedScientist!!.farmerNumber,valOf(weightTxt))
            crudHelper.update(this, db, pb, updatedScientist)
        }
    }

}