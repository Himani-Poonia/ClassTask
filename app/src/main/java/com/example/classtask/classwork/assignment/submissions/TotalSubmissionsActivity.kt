package com.example.classtask.classwork.assignment.submissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R

class TotalSubmissionsActivity : AppCompatActivity() {

    private lateinit var submissionRecyclerView: RecyclerView
    private var submissionAdapter: SubmissionAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var submissionModelList: ArrayList<SubmissionModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_submissions)

        submissionRecyclerView = findViewById(R.id.submissionRv)
        emptyListTextView = findViewById(R.id.tvNoSubmission)
//        progressBar = view.findViewById(R.id.progressBar)

        submissionRecyclerView.layoutManager = LinearLayoutManager(this)

        submissionModelList = ArrayList()
        submissionAdapter = SubmissionAdapter(this, submissionModelList)

        submissionRecyclerView.adapter = submissionAdapter

        emptyListTextView.visibility = View.VISIBLE

        submissionModelList.add(SubmissionModel("dsgvd","Himani","affgvv.pdf"))
        emptyListTextView.visibility = View.GONE
    }
}