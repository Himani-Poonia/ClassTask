package com.example.classtask.classwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.classtask.R
import com.example.classtask.classwork.chats.ChatFragment
import com.example.classtask.classwork.assignment.AssignmentFragment
import com.example.classtask.classwork.people.PeopleFragment
import com.google.android.material.tabs.TabLayout

class AssignmentsActivity : AppCompatActivity() {

    companion object {
        var tabLayout: TabLayout? = null
        var viewPager: ViewPager? = null
        var isTeacher = false
        var className = ""
        var classId = ""
        var teacherId=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignments)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true) //back arrow on action bar
            actionBar.elevation = 0F
        }

        val intent = intent
        className = intent.getStringExtra("className").toString()
        title = className
        isTeacher = intent.getBooleanExtra("isTeacher",false)
        classId = intent.getStringExtra("classId").toString()
        teacherId = intent.getStringExtra("teacherId").toString()

        tabLayout = findViewById(R.id.tabLayoutMain)
        viewPager = findViewById(R.id.viewPagerMain)

        setViewPager()
    }

    //this function will set the view pager according to the tab selection
    private fun setViewPager() {
        val mainAdapter = Adapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager?.adapter = mainAdapter

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(
            tabLayout
        ))
    }

    //Fragment adapter class which will handle tab switching
    class Adapter(fm: FragmentManager, behavior: Int): FragmentPagerAdapter(fm, behavior) {
        override fun getCount(): Int {
            return tabLayout!!.tabCount
        }

        override fun getItem(position: Int): Fragment {
            return if(position==0) {
                val fragment = AssignmentFragment()

                val bundle = Bundle()
                bundle.putBoolean("isTeacher", isTeacher)
                bundle.putString("className",className)
                bundle.putString("classId", classId)
                bundle.putString("teacherId", teacherId)
                fragment.arguments = bundle

                fragment
            }
            else if(position==1){
                val fragment = ChatFragment()

                val bundle = Bundle()
                bundle.putString("classId", classId)
                bundle.putString("teacherId", teacherId)
                fragment.arguments = bundle

                fragment
            }
            else{
                val fragment = PeopleFragment()

                val bundle = Bundle()
                bundle.putString("classId", classId)
                bundle.putString("teacherId", teacherId)
                fragment.arguments = bundle

                fragment
            }
        }
    }

    //to handle back arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}