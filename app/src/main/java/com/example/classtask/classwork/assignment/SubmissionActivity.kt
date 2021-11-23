package com.example.classtask.classwork.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.classtask.R
import com.example.classtask.classwork.assignment.submissions.TotalSubmissionsActivity

class SubmissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)

        val intent = intent
        title = intent.getStringExtra("className")

        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvDesc: TextView = findViewById(R.id.tvDescription)
        val tvPoints: TextView = findViewById(R.id.tvPoints)
        val submitBtn: Button = findViewById(R.id.uploadBtn)
        val submitLinkLayout: ConstraintLayout = findViewById(R.id.clStudentSubmit)

        //set values accordingly
        tvTitle.text = intent.getStringExtra("title")
        tvDesc.text = intent.getStringExtra("description")
        tvPoints.text = intent.getStringExtra("points")

        if(intent.getBooleanExtra("isTeacher", false)){
            submitBtn.text = "Submissions"
            submitLinkLayout.visibility = View.GONE

            submitBtn.setOnClickListener {
                startActivity(Intent(this, TotalSubmissionsActivity::class.java))
            }
        }
        else{
            submitBtn.text = "Upload"
            submitLinkLayout.visibility = View.VISIBLE

            submitBtn.setOnClickListener {
                //open files of phone
            }
        }
    }
}