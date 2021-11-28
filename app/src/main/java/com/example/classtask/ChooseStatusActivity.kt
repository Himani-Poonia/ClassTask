package com.example.classtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class ChooseStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_status)

        supportActionBar?.hide()

        val clTeacher: ConstraintLayout = findViewById(R.id.clChooseTeacher)
        val clStudent: ConstraintLayout = findViewById(R.id.clChooseStudent)

        val intent = Intent(this, MainActivity::class.java)
        clTeacher.setOnClickListener {
            intent.putExtra("isTeacher", true)
            startActivity(intent)
        }

        clStudent.setOnClickListener {
            intent.putExtra("isTeacher", false)
            startActivity(intent)
        }
    }
}