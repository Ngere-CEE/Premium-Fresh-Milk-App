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
import com.example.premiumfreshmilk.activities.QualityDetailActivity
import com.example.premiumfreshmilk.helpers.FilterMilkParameter
import com.example.premiumfreshmilk.helpers.Utils
import com.example.premiumfreshmilk.helpers.Utils.searchString
import com.github.ivbaranov.mli.MaterialLetterIcon
import java.util.*

class MilkParametersAdapter(private val c: Context, var milkParameter: ArrayList<MilkParameter>) : RecyclerView.Adapter<MilkParametersAdapter.ViewHolder>(), Filterable {
    private val qMaterialColors: IntArray
    private val filterList: List<MilkParameter>
    private var filterHelper: FilterMilkParameter? = null

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
        val qTnameTxt: TextView
        val mqweightTxT: TextView
        val mbutterfatTxT: TextView
        val mdensityTxT: TextView
        val mIcon: MaterialLetterIcon = itemView.findViewById(R.id.qMaterialLetterIcon)
        private var itemClickListener: ItemClickListener? = null
        override fun onClick(view: View) {
            itemClickListener!!.onItemClick(this.layoutPosition)
        }

        fun setItemClickListener(itemClickListener: ItemClickListener?) {
            this.itemClickListener = itemClickListener
        }

        init {
            qTnameTxt = itemView.findViewById(R.id.qTnameTxt)
            mqweightTxT = itemView.findViewById(R.id.mqweightTxT)
            mbutterfatTxT = itemView.findViewById(R.id.mbutterfatTxT)
            mdensityTxT = itemView.findViewById(R.id.mdensityTxT)
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
        val view: View = LayoutInflater.from(c).inflate(R.layout.quality_model, parent, false)
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
        val s = milkParameter[position]
        //bind data to widgets
        holder.qTnameTxt.text = s.transporterQName
        holder.mqweightTxT.text = s.qualityWeight
        holder.mbutterfatTxT.text = s.qualityButterfat
        holder.mdensityTxT.text = s.qualityDensity
        holder.mIcon.isInitials = true
        holder.mIcon.initialsNumber = 2
        holder.mIcon.letterSize = 25
        //set random color to our material letter icons
        holder.mIcon.shapeColor = qMaterialColors[Random().nextInt(
            qMaterialColors.size
        )]
        holder.mIcon.letter = s.transporterQName
        //if you want to set alternating row background colors
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"))
        }
        //get name and galaxy
        val name = s.transporterQName!!.toLowerCase(Locale.getDefault())
        //highlight name text while searching
        if (name.contains(searchString) && searchString.isNotEmpty()) {
            val startPos: Int = name.indexOf(searchString)
            val endPos: Int = startPos + searchString.length
            val spanString =
                Spannable.Factory.getInstance().newSpannable(holder.qTnameTxt.text)
            spanString.setSpan(
                ForegroundColorSpan(Color.RED), startPos, endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.qTnameTxt.text = spanString
        }
        //highlight galaxy text while searching
        //open detailactivity when clicked
        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                Utils.sendMilkParametersActivity(
                    c, s,
                    QualityDetailActivity::class.java
                )
            }
        })
    }

    override fun getItemCount(): Int {
        return milkParameter.size
    }

    override fun getFilter(): Filter {
        if (filterHelper == null) {
            filterHelper = FilterMilkParameter.newInstance(filterList, this)
        }
        return filterHelper as FilterMilkParameter
    }

    /**
     * Our Constructor
     * ROLES:
     * 1. Initialize some stuff for us
     * 2. Receive a context obj we need for inflation
     * 3. Receive the list to rendered in our recyclerview
     */
    init {
        filterList = milkParameter
        qMaterialColors = c.resources.getIntArray(R.array.colors)
    }
}