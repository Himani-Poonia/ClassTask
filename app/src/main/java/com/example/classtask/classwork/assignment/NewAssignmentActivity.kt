package com.example.classtask.classwork.assignment

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.classtask.R
import com.example.classtask.classwork.AssignmentsActivity

class NewAssignmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_assignment)

        val intent = intent
        title = intent.getStringExtra("className")
        val actionBar = actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true) //back arrow on action bar
        actionBar?.elevation = 0F
//        val isTeacher = intent.getBooleanExtra("isTeacher",false)
    }
}