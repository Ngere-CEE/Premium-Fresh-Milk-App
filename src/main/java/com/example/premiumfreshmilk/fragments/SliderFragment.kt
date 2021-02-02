package com.example.premiumfreshmilk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.premiumfreshmilk.R


class SliderFragment : Fragment() {
    var pageTitle : String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentTitle = view!!.findViewById<TextView>(R.id.fragment_title)
        fragmentTitle.text = pageTitle
    }
    fun setTitle(title: String){
        pageTitle = title
    }
}
