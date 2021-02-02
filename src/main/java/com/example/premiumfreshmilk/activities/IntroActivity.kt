package com.example.premiumfreshmilk.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.premiumfreshmilk.R
import com.example.premiumfreshmilk.fragments.SliderFragment

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
    val fragment1 = SliderFragment()
    val fragment2 = SliderFragment()
    val fragment3 = SliderFragment()
    lateinit var adapter : myPagerAdapter
    lateinit var activity : Activity
    lateinit var preference : SharedPreferences
    val pref_show_intro = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        //initialize the buttons and textviews
        val btnNext = findViewById<Button>(R.id.btn_next)
        val btnSkip = findViewById<Button>(R.id.btn_skip)
        val indicator1 = findViewById<TextView>(R.id.indicator1)
        val indicator2 = findViewById<TextView>(R.id.indicator2)
        val indicator3 = findViewById<TextView>(R.id.indicator3)
        activity = this
        preference = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)

        if (!preference.getBoolean(pref_show_intro,true)){
            startActivity(Intent(activity, LoginActivity::class.java))
            finish()
        }
        fragment1.setTitle("Welcome")
        fragment2.setTitle("To Lucky Dairies Farmers App")
        fragment3.setTitle("The easiest way to collect milk from farmers")

        //initialize adapter
        adapter = myPagerAdapter(supportFragmentManager)
        adapter.list.add(fragment1)
        adapter.list.add(fragment2)
        adapter.list.add(fragment3)

        //initialize viewPager
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = adapter
        btnNext.setOnClickListener {
            viewPager.currentItem++
        }
        btnSkip.setOnClickListener { goToDashboard() }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int,positionOffset: Float,positionOffsetPixels: Int){
            }
            override fun onPageSelected(position: Int) {
               if (position == adapter.list.size-1){
                   //last page
                   btnNext.text = "DONE"
                   btnNext.setOnClickListener {
                      goToDashboard()
                   }
               }else{
                   //has next page
                   btnNext.text = "NEXT"
                   btnNext.setOnClickListener {
                       viewPager.currentItem++
                   }
               }
                when(viewPager.currentItem){
                    0->{
                        indicator1.setTextColor(Color.BLACK)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.GRAY)
                    }
                    1->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.BLACK)
                        indicator3.setTextColor(Color.GRAY)
                    }
                    2->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.BLACK)
                    }

                }
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
    fun goToDashboard(){
        startActivity(Intent(activity, LoginActivity::class.java))
        finish()
        val editor = preference.edit()
        editor.putBoolean(pref_show_intro,false)
        editor.apply()
    }
    class myPagerAdapter(manager:FragmentManager) : FragmentPagerAdapter(manager){
        val list : MutableList<Fragment> = ArrayList()
        override fun getCount(): Int {
            return list.size
        }
        override fun getItem(position: Int): Fragment {
            return list[position]
        }
    }
}
