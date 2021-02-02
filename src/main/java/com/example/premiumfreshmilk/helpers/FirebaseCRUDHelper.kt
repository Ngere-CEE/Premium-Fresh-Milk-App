package com.example.premiumfreshmilk.helpers

import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.premiumfreshmilk.activities.QualityActivity
import com.example.premiumfreshmilk.interfaces.IFirebaseLoadDelivery
import com.example.premiumfreshmilk.interfaces.IFirebaseLoadDone
import com.example.premiumfreshmilk.activities.ScientistsActivity
import com.example.premiumfreshmilk.activities.TransporterDelivery
import com.example.premiumfreshmilk.data.*
import com.example.premiumfreshmilk.helpers.Utils.DataCache
import com.example.premiumfreshmilk.helpers.Utils.DataCache2
import com.example.premiumfreshmilk.helpers.Utils.DataCache3
import com.example.premiumfreshmilk.helpers.Utils.hideProgressBar
import com.example.premiumfreshmilk.helpers.Utils.openActivity
import com.example.premiumfreshmilk.helpers.Utils.show
import com.example.premiumfreshmilk.helpers.Utils.showInfoDialog
import com.example.premiumfreshmilk.helpers.Utils.showProgressBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FirebaseCRUDHelper {
    /**
     * This method will allow us post into firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param scientist
     */
    fun getFarmers(mDb: DatabaseReference, iFirebaseLoadDone: IFirebaseLoadDone){
        mDb.child("farmers").addValueEventListener(object : ValueEventListener {
            var farmerList:MutableList<farmer> = ArrayList<farmer>()
            override fun onCancelled(p0: DatabaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (farmerSnapshot in p0.children)
                    farmerList.add(farmerSnapshot.getValue<farmer>(farmer::class.java)!!)
                iFirebaseLoadDone.onFirebaseLoadSuccess(farmerList)
            }
        })
    }
    fun getTransporters(mDb: DatabaseReference, iFirebaseLoadDel: IFirebaseLoadDelivery){
        mDb.child("transporters").addValueEventListener(object : ValueEventListener {
            var transporterList:MutableList<Transporters> = ArrayList<Transporters>()
            override fun onCancelled(p0: DatabaseError) {
                iFirebaseLoadDel.onDelivLoadFailed(p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (transSnapshot in p0.children)
                    transporterList.add(transSnapshot.getValue<Transporters>(Transporters::class.java)!!)
                iFirebaseLoadDel.onDelivLoadSuccess(transporterList)
            }
        })
    }
    fun insert(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, scientist: Scientist?) { //check if they have passed us a valid scientist. If so then return false.
        if (scientist == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Milk data is null")
            return
        } else { //otherwise try to push data to firebase database.
            showProgressBar(pb!!)
            //push data to FirebaseDatabase. Table or Child called Scientist will be created
            mDatabaseRef.child("milkcollection").push().setValue(scientist)
                .addOnCompleteListener { task ->
                    hideProgressBar(pb)
                    if (task.isSuccessful) {
                        openActivity(a!!, ScientistsActivity::class.java)
                        show(a, "Bravo! RECORD SAVED SUCCESSFULLY")
                    } else {
                        showInfoDialog(a!!, "Failed!! UNSUCCESSFUL", task.exception!!.message)
                    }
                }
        }
    }

    fun insertNewDelivery(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, receivedmilk: ReceivedMilk?) { //check if they have passed us a valid scientist. If so then return false.
        if (receivedmilk == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Milk data is null")
            return
        } else { //otherwise try to push data to firebase database.
            showProgressBar(pb!!)
            //push data to FirebaseDatabase. Table or Child called Scientist will be created
            mDatabaseRef.child("milkdelivered").push().setValue(receivedmilk)
                .addOnCompleteListener { task ->
                    hideProgressBar(pb)
                    if (task.isSuccessful) {
                        openActivity(a!!, TransporterDelivery::class.java)
                        show(a, "Bravo! RECORD SAVED SUCCESSFULLY")
                    } else {
                        showInfoDialog(a!!, "Failed!! UNSUCCESSFUL", task.exception!!.message)
                    }
                }
        }
    }

    fun insertNewParam(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, milkparameter: MilkParameter?) { //check if they have passed us a valid scientist. If so then return false.
        if (milkparameter == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Parameter data is null")
            return
        } else { //otherwise try to push data to firebase database.
            showProgressBar(pb!!)
            //push data to FirebaseDatabase. Table or Child called Scientist will be created
            mDatabaseRef.child("milkparameters").push().setValue(milkparameter)
                .addOnCompleteListener { task ->
                    hideProgressBar(pb)
                    if (task.isSuccessful) {
                        openActivity(a!!, QualityActivity::class.java)
                        show(a, "Bravo! RECORD SAVED SUCCESSFULLY")
                    } else {
                        showInfoDialog(a!!, "Failed!! UNSUCCESSFUL", task.exception!!.message)
                    }
                }
        }
    }

    /**
     * This method will allow us retrieve from firebase realtime database
     * @param a
     * @param db
     * @param pb
     * @param rv
     * @param adapter
     */
    fun select( a: AppCompatActivity?, db: DatabaseReference, pb: ProgressBar?, rv: RecyclerView, adapter: MyAdapter) {
        showProgressBar(pb!!)
        val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        dateFormatter.isLenient = false
        var today = Date()
        var docln = dateFormatter.format(today)
        db.child("milkcollection").orderByChild("docln").equalTo(docln).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                DataCache.clear()
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    for (ds in dataSnapshot.children) { //Now get Scientist Objects and populate our arraylist.
                        val farmer: Scientist = ds.getValue(Scientist::class.java)!!
                        farmer.key=ds.key
                        DataCache.add(farmer)
                    }
                    adapter.notifyDataSetChanged()
                    Handler().post {
                        hideProgressBar(pb)
                        rv.smoothScrollToPosition(DataCache.size)
                    }
                } else {
                    show(a, "No more item found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FIREBASE CRUD", databaseError.message)
                hideProgressBar(pb)
                showInfoDialog(a!!, "CANCELLED", databaseError.message)
            }
        })
    }

    fun selectdelivery( a: AppCompatActivity?, db: DatabaseReference, pb: ProgressBar?, rvt: RecyclerView, adapter: ReceivedMilkAdapter) {
        showProgressBar(pb!!)
        val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        dateFormatter.isLenient = false
        var today = Date()
        var dodel = dateFormatter.format(today)
        db.child("milkdelivered").orderByChild("datedelivered").equalTo(dodel).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                DataCache2.clear()
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    for (ds in dataSnapshot.children) { //Now get Scientist Objects and populate our arraylist.
                        val milkdel: ReceivedMilk = ds.getValue(ReceivedMilk::class.java)!!
                        milkdel.key=ds.key
                        DataCache2.add(milkdel)
                    }
                    adapter.notifyDataSetChanged()
                    Handler().post {
                        hideProgressBar(pb)
                        rvt.smoothScrollToPosition(DataCache2.size)
                    }
                } else {
                    show(a, "No more item found")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FIREBASE CRUD", databaseError.message)
                hideProgressBar(pb)
                showInfoDialog(a!!, "CANCELLED", databaseError.message)
            }
        })
    }

    fun selectparameter( a: AppCompatActivity?, db: DatabaseReference, pb: ProgressBar?, rvq: RecyclerView, adapter: MilkParametersAdapter) {
        showProgressBar(pb!!)
        val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        dateFormatter.isLenient = false
        var today = Date()
        var dodel = dateFormatter.format(today)
        db.child("milkparameters").orderByChild("dateRecorded").equalTo(dodel).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                DataCache3.clear()
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    for (ds in dataSnapshot.children) { //Now get Scientist Objects and populate our arraylist.
                        val milkparam: MilkParameter = ds.getValue(MilkParameter::class.java)!!
                        milkparam.key=ds.key
                        DataCache3.add(milkparam)
                    }
                    adapter.notifyDataSetChanged()
                    Handler().post {
                        hideProgressBar(pb)
                        rvq.smoothScrollToPosition(DataCache3.size)
                    }
                } else {
                    show(a, "No more item found")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FIREBASE CRUD", databaseError.message)
                hideProgressBar(pb)
                showInfoDialog(a!!, "CANCELLED", databaseError.message)
            }
        })
    }

    /**
     * This method will allow us update existing data in firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param updatedScientist
     */
    fun update(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, updatedScientist: Scientist?) {
        if (updatedScientist == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Scientist is null")
            return
        }
        showProgressBar(pb!!)
        mDatabaseRef.child("milkcollection").child(updatedScientist.key!!).setValue(updatedScientist)
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, updatedScientist.fullName.toString() + " Bravo! Updated Successfully.")
                    openActivity(a!!, ScientistsActivity::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to update! Try again later", task.exception!!.message)
                }
            }
    }

    fun updateDelivered(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, updatedDelivered: ReceivedMilk?) {
        if (updatedDelivered == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Delivery is null")
            return
        }
        showProgressBar(pb!!)
        mDatabaseRef.child("milkdelivered").child(updatedDelivered.key!!).setValue(updatedDelivered)
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, updatedDelivered.transporterName.toString() + " Bravo! Updated Successfully.")
                    openActivity(a!!, TransporterDelivery::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to update! Try again later", task.exception!!.message)
                }
            }
    }

    fun updateParam(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, updateParameter: MilkParameter?) {
        if (updateParameter == null) {
            showInfoDialog(a!!, "VALIDATION FAILED", "Parameter is null")
            return
        }
        showProgressBar(pb!!)
        mDatabaseRef.child("milkparameters").child(updateParameter.key!!).setValue(updateParameter)
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, updateParameter.transporterQName.toString() + " Bravo! Updated Successfully.")
                    openActivity(a!!, QualityActivity::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to update! Try again later", task.exception!!.message)
                }
            }
    }

    /**
     * This method will allow us to delete from firebase realtime database
     * @param a
     * @param mDatabaseRef
     * @param pb
     * @param selectedScientist
     */
    fun delete(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, selectedScientist: Scientist) {
        showProgressBar(pb!!)
        val selectedScientistKey: String = selectedScientist.key!!
        mDatabaseRef.child("milkcollection").child(selectedScientistKey).removeValue()
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, selectedScientist.fullName.toString() + " Record successfully Deleted.")
                    openActivity(a!!, ScientistsActivity::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to delete record!", task.exception!!.message)
                }
            }
    }

    fun deletedelivery(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, selectedDevivery: ReceivedMilk) {
        showProgressBar(pb!!)
        val selectedDeviveryKey: String = selectedDevivery.key!!
        mDatabaseRef.child("milkdelivered").child(selectedDeviveryKey).removeValue()
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, selectedDevivery.transporterName.toString() + " Record successfully Deleted.")
                    openActivity(a!!, TransporterDelivery::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to delete record!", task.exception!!.message)
                }
            }
    }
    fun deleteparameter(a: AppCompatActivity?, mDatabaseRef: DatabaseReference, pb: ProgressBar?, selectedParam: MilkParameter) {
        showProgressBar(pb!!)
        val selectedParamKey: String = selectedParam.key!!
        mDatabaseRef.child("milkparameters").child(selectedParamKey).removeValue()
            .addOnCompleteListener { task ->
                hideProgressBar(pb)
                if (task.isSuccessful) {
                    show(a, selectedParam.transporterQName.toString() + " Record successfully Deleted.")
                    openActivity(a!!, QualityActivity::class.java)
                } else {
                    showInfoDialog(a!!, "Failed to delete record!", task.exception!!.message)
                }
            }
    }
}