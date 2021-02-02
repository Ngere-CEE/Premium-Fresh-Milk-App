package com.example.premiumfreshmilk.data

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.activities.TransDetailAtivity
import com.example.premiumfreshmilk.helpers.FilterReceivedmilk
import com.example.premiumfreshmilk.helpers.Utils
import com.example.premiumfreshmilk.helpers.Utils.searchString
import com.github.ivbaranov.mli.MaterialLetterIcon
import java.util.*

class ReceivedMilkAdapter(private val c: Context, var receivedMilk: ArrayList<ReceivedMilk>) : RecyclerView.Adapter<ReceivedMilkAdapter.ViewHolder>(), Filterable {
    private val mMaterialColors: IntArray
    private val filterList: List<ReceivedMilk>
    private var filterHelper: FilterReceivedmilk? = null

    interface ItemClickListener {
        fun onItemClick(pos: Int)
    }

    /**
     * Our ViewHolder class
     * ROLES
     * 1. Hold inflated widgets in memory so that we can recycle them
     */
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tnameTxt: TextView
        val delWeightTxt: TextView
        val mIcon: MaterialLetterIcon = itemView.findViewById(R.id.transMaterialLetterIcon)
        private var itemClickListener: ItemClickListener? = null
        override fun onClick(view: View) {
            itemClickListener!!.onItemClick(this.layoutPosition)
        }

        fun setItemClickListener(itemClickListener: ItemClickListener?) {
            this.itemClickListener = itemClickListener
        }

        init {
            tnameTxt = itemView.findViewById(R.id.tnameTxt)
            delWeightTxt = itemView.findViewById(R.id.delWeightTxt)
            itemView.setOnClickListener(this)
        }
    }

    /**
     * ROLES:
     * 1. Inflate model.xml into a view object that we can work with in code
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.received_milk_model, parent, false)
        return ViewHolder(view)
    }

    /**
     * Our onBindViewHolder method
     * ROLES:
     * 1. Bind data to our inflated recyclerview items
     * 2. Highlight search results using Spannable class
     * 3. Attach click listener to recyclerview items so that we open
     * detail activity
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) { //get current scientist
        val s = receivedMilk[position]
        //bind data to widgets
        holder.tnameTxt.text = s.transporterName
        holder.delWeightTxt.text = s.deliveredWeight
        holder.mIcon.isInitials = true
        holder.mIcon.initialsNumber = 2
        holder.mIcon.letterSize = 25
        //set random color to our material letter icons
        holder.mIcon.shapeColor = mMaterialColors[Random().nextInt(
            mMaterialColors.size
        )]
        holder.mIcon.letter = s.transporterName
        //if you want to set alternating row background colors
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"))
        }
        //get name and galaxy
        val name = s.transporterName!!.toLowerCase(Locale.getDefault())
        //highlight name text while searching
        if (name.contains(searchString) && searchString.isNotEmpty()) {
            val startPos: Int = name.indexOf(searchString)
            val endPos: Int = startPos + searchString.length
            val spanString =
                Spannable.Factory.getInstance().newSpannable(holder.tnameTxt.text)
            spanString.setSpan(
                ForegroundColorSpan(Color.RED), startPos, endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.tnameTxt.text = spanString
        }
        //highlight galaxy text while searching
        //open detailactivity when clicked
        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                Utils.sendReceivedmilkToActivity(
                    c, s,
                    TransDetailAtivity::class.java
                )
            }
        })
    }

    override fun getItemCount(): Int {
        return receivedMilk.size
    }

    override fun getFilter(): Filter {
        if (filterHelper == null) {
            filterHelper = FilterReceivedmilk.newInstance(filterList, this)
        }
        return filterHelper as FilterReceivedmilk
    }

    /**
     * Our Constructor
     * ROLES:
     * 1. Initialize some stuff for us
     * 2. Receive a context obj we need for inflation
     * 3. Receive the list to rendered in our recyclerview
     */
    init {
        filterList = receivedMilk
        mMaterialColors = c.resources.getIntArray(R.array.colors)
    }
}