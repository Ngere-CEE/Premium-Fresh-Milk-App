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
import com.example.premiumfreshmilk.data.ReceivedMilk
import com.example.premiumfreshmilk.data.Transporters
import com.example.premiumfreshmilk.helpers.FirebaseCRUDHelper
import com.example.premiumfreshmilk.helpers.Utils.databaseRefence
import com.example.premiumfreshmilk.helpers.Utils.openActivity
import com.example.premiumfreshmilk.helpers.Utils.show
import com.example.premiumfreshmilk.helpers.Utils.showInfoDialog
import com.example.premiumfreshmilk.helpers.Utils.receiveDeliveredMilk
import com.example.premiumfreshmilk.helpers.Utils.valOf
import com.example.premiumfreshmilk.helpers.Utils.validatedel
import com.example.premiumfreshmilk.interfaces.IFirebaseLoadDelivery
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_crud.pb
import kotlinx.android.synthetic.main.activity_trans_crud.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransCrudActivity : AppCompatActivity(), IFirebaseLoadDelivery {
    lateinit var iFirebaseLoadDel: IFirebaseLoadDelivery
    //we'll have several instance fields
    private val c: Context = this@TransCrudActivity
    private var receiveDeliveredMilk: ReceivedMilk? = null
    private val crudHelper = FirebaseCRUDHelper()
    private val db = databaseRefence
    var transporterList : List<Transporters> = ArrayList<Transporters>()
    /**
     * Delete data from firebase
     */
    private fun deleteData() {
        crudHelper.deletedelivery(this, db, pb, receiveDeliveredMilk!!)
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
        if (receiveDeliveredMilk == null) {
            menuInflater.inflate(R.menu.new_del_menu, menu)
            btn_savedelivery!!.text = "Save"
        } else {
            menuInflater.inflate(R.menu.edit_del_menu, menu)
            btn_savedelivery!!.text = "Update"
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insertTMenuItem -> {
                insertData()
                return true
            }
            R.id.editTMenuItem -> {
                if (receiveDeliveredMilk != null) {
                    updateData()
                } else {
                    show(this, "EDIT ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.deleteTMenuItem -> {
                if (receiveDeliveredMilk != null) {
                    deleteData()
                } else {
                    show(this, "DELETE ONLY WORKS IN EDITING MODE")
                }
                return true
            }
            R.id.viewAllTMenuItem -> {
                openActivity(this, TransporterDelivery::class.java)
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
        val s = receiveDeliveredMilk(intent, c)
        if (s != null) {
            receiveDeliveredMilk = s
            delvweightTxt.setText(receiveDeliveredMilk!!.deliveredWeight)
            transnameTxt.setText(receiveDeliveredMilk!!.transporterName)
            transnumTxt.setText(receiveDeliveredMilk!!.transporterNumber)
        }
    }
    /**
     * Let's override our onCreate() method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_crud)
        val tsearchablespinner = findViewById<SearchableSpinner>(R.id.trans_searchable_spinner)
        val transnameTxt = findViewById<EditText>(R.id.transnameTxt)
        val transnumTxt = findViewById<EditText>(R.id.transnumTxt)
        transnameTxt.setKeyListener(null);
        transnumTxt.setKeyListener(null);
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
        btn_savedelivery.setOnClickListener {
            if (receiveDeliveredMilk == null){
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
        //create adapter
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, trans)
        val search = findViewById<SearchableSpinner>(R.id.trans_searchable_spinner)
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

    override fun onDelivLoadFailed(message: String) {    }
    /**
     * Validate then insert data
     */
    @SuppressLint("SimpleDateFormat")
    private fun insertData() {
        if (validatedel(transnameTxt,transnumTxt,delvweightTxt)) {
            //Get current date
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.isLenient = false
            val today = Date()
            val datedelivered = dateFormatter.format(today)
            val newDelivery= ReceivedMilk("",datedelivered,valOf(transnameTxt),valOf(transnumTxt),valOf(delvweightTxt))
            crudHelper.insertNewDelivery(this, db, pb, newDelivery)
        }
    }
    /**
     * Validate then update data
     */
    @SuppressLint("SimpleDateFormat")
    private fun updateData() {
        if (validatedel(transnameTxt,transnumTxt,delvweightTxt)) {
            //Get current date
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.isLenient = false
            val today = Date()
            val datedelivered = dateFormatter.format(today)
            val updatedDelivered =
                ReceivedMilk(receiveDeliveredMilk!!.key,datedelivered,receiveDeliveredMilk!!.transporterName,
                    receiveDeliveredMilk!!.transporterNumber,valOf(delvweightTxt))
            crudHelper.updateDelivered(this, db, pb, updatedDelivered)
        }
    }

}