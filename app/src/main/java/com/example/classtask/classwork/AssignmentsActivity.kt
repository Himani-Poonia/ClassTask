package com.example.classtask.classwork

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.classtask.MainActivity
import com.example.classtask.ProfileActivity
import com.example.classtask.R
import com.example.classtask.classwork.assignment.AssignmentFragment
import com.example.classtask.classwork.people.PeopleFragment
import com.example.classtask.student.StudentFragment
import com.example.classtask.teacher.TeacherFragment
import com.google.android.material.tabs.TabLayout

class AssignmentsActivity : AppCompatActivity() {

    companion object {
        var tabLayout: TabLayout? = null
        var viewPager: ViewPager? = null
        var isTeacher = false
        var className = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignments)

        val intent = intent
        className = intent.getStringExtra("className").toString()
        title = className
        isTeacher = intent.getBooleanExtra("isTeacher",false)

        tabLayout = findViewById(R.id.tabLayoutMain)
        viewPager = findViewById(R.id.viewPagerMain)

        setViewPager()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.itemProfile) {
//            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
//            intent.putExtra("name",currentUserName)
//            intent.putExtra("email",currentUserEmail)
//            startActivity(intent)
//        }
//        return super.onOptionsItemSelected(item)
//    }

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
                fragment.arguments = bundle

                fragment
            }else{
                val fragment = PeopleFragment()
                fragment
            }
        }
    }
}