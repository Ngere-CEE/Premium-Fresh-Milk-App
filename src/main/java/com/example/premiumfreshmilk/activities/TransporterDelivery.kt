package com.example.premiumfreshmilk.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.data.ReceivedMilkAdapter
import com.example.premiumfreshmilk.helpers.FirebaseCRUDHelper
import com.example.premiumfreshmilk.helpers.Utils
import com.example.premiumfreshmilk.helpers.Utils.databaseRefence
import com.example.premiumfreshmilk.helpers.Utils.sendScientistToActivity
import com.example.premiumfreshmilk.helpers.Utils.showProgressBar
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_scientists.pb
import kotlinx.android.synthetic.main.activity_transporter_delivery.*

class TransporterDelivery : AppCompatActivity(),
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private val crudHelper = FirebaseCRUDHelper()
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ReceivedMilkAdapter? = null
    private fun initializeViews() {
        pb.isIndeterminate = true
        showProgressBar(pb)
        layoutManager = LinearLayoutManager(this)
        rvt.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
                rvt.context,
                layoutManager!!.orientation
        )
        rvt.addItemDecoration(dividerItemDecoration)
        adapter = ReceivedMilkAdapter(this, Utils.DataCache2)
        rvt.adapter = adapter
    }

    /**
     * Bind data to recyclerview
     */
    private fun bindData() {
        crudHelper.selectdelivery(this, databaseRefence, pb, rvt!!, adapter!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.transporter_delivery_menu, menu)
        val searchItem = menu.findItem(R.id.action_searcht)
        val searchView =
                searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.queryHint = "Search"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_newt -> {
                sendScientistToActivity(
                        this,
                        null,
                        TransCrudActivity::class.java
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Inject custom fonts into this activity as well
     * @param newBase
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return false
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        Utils.searchString = query
        val adapter =
                ReceivedMilkAdapter(this, Utils.DataCache2)
        adapter.filter.filter(query)
        rvt!!.layoutManager = LinearLayoutManager(this)
        rvt!!.adapter = adapter
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transporter_delivery)
        initializeViews()
        bindData()
    }
}