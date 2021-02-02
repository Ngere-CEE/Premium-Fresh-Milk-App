package com.example.premiumfreshmilk.helpers

import android.widget.Filter
import com.example.premiumfreshmilk.data.MyAdapter
import com.example.premiumfreshmilk.data.Scientist

import java.util.*

class FilterHelper : Filter() {
    /*
    - Perform actual filtering.
     */
    override fun performFiltering(constraint: CharSequence): FilterResults {
        var constraint: CharSequence? = constraint
        val filterResults = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) { //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase()
            //HOLD FILTERS WE FIND
            val foundFilters: ArrayList<Scientist> = ArrayList()
            var name: String
            var description: String
            //ITERATE CURRENT LIST
            for (i in currentList!!.indices) {
                name = currentList!![i].fullName!!
                //SEARCH
             if (name.toUpperCase().contains(constraint)) {
                    foundFilters.add(currentList!![i])
                }
            }
            //SET RESULTS TO FILTER LIST
            filterResults.count = foundFilters.size
            filterResults.values = foundFilters
        } else { //NO ITEM FOUND.LIST REMAINS INTACT
            filterResults.count = currentList!!.size
            filterResults.values = currentList
        }
        //RETURN RESULTS
        return filterResults
    }

    override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
        adapter!!.scientists = filterResults.values as ArrayList<Scientist>
        adapter!!.notifyDataSetChanged()
    }

    companion object {
        var currentList: List<Scientist>? = null
        var adapter: MyAdapter? = null
        fun newInstance(
            currentList: List<Scientist>,
            adapter: MyAdapter?
        ): FilterHelper {
            Companion.adapter = adapter
            Companion.currentList = currentList
            return FilterHelper()
        }
    }
}