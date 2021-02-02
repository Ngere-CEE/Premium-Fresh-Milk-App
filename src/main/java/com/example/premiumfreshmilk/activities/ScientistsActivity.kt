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
import com.example.premiumfreshmilk.data.MyAdapter
import com.example.premiumfreshmilk.helpers.FirebaseCRUDHelper
import com.example.premiumfreshmilk.helpers.Utils
import com.example.premiumfreshmilk.helpers.Utils.databaseRefence
import com.example.premiumfreshmilk.helpers.Utils.sendScientistToActivity
import com.example.premiumfreshmilk.helpers.Utils.showProgressBar
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_scientists.*

class ScientistsActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private val crudHelper = FirebaseCRUDHelper()
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: MyAdapter? = null
    private fun initializeViews() {
        pb.isIndeterminate = true
        showProgressBar(pb)
        layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context,
            layoutManager!!.orientation
        )
        rv.addItemDecoration(dividerItemDecoration)
        adapter = MyAdapter(this, Utils.DataCache)
        rv.adapter = adapter
    }

    /**
     * Bind data to recyclerview
     */
    private fun bindData() {
        crudHelper.select(this, databaseRefence, pb, rv!!, adapter!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu options from the res/menu/menu_editor.xml file.
       // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.scientists_page_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView =
            searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = true
        searchView.queryHint = "Search"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            R.id.action_new -> {
                sendScientistToActivity(
                    this,
                    null,
                    CRUDActivity::class.java
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
            MyAdapter(this, Utils.DataCache)
        adapter.filter.filter(query)
        rv!!.layoutManager = LinearLayoutManager(this)
        rv!!.adapter = adapter
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scientists)
        initializeViews()
        bindData()
    }
}