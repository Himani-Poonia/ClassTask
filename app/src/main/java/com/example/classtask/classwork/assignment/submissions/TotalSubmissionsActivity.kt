package com.example.classtask.classwork.assignment.submissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TotalSubmissionsActivity : AppCompatActivity() {

    private lateinit var submissionRecyclerView: RecyclerView
    private var submissionAdapter: SubmissionAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var submissionModelList: ArrayList<SubmissionModel>
    private var rootReference = FirebaseDatabase.getInstance().reference
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_submissions)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true) //back arrow on action bar
            actionBar.elevation = 0F
        }

        val classId = intent.getStringExtra("classId").toString()
        val assignTitle = intent.getStringExtra("title").toString()
        val teacherId = intent.getStringExtra("teacherId").toString()

        title = assignTitle

        submissionRecyclerView = findViewById(R.id.submissionRv)
        emptyListTextView = findViewById(R.id.tvNoSubmission)
        progressBar = findViewById(R.id.listProgressBar)

        submissionRecyclerView.layoutManager = LinearLayoutManager(this)

        submissionModelList = ArrayList()
        submissionAdapter = SubmissionAdapter(this, submissionModelList, classId, teacherId, assignTitle)

        submissionRecyclerView.adapter = submissionAdapter

        emptyListTextView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        rootReference.child(NodeNames.STUDENT).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                submissionModelList.clear()
                submissionAdapter!!.notifyDataSetChanged()

                for(snaps in snapshot.children){
                    val studentId = snaps.key.toString()

                    rootReference.child(NodeNames.STUDENT).child(studentId).child(classId)
                        .child(NodeNames.ASSIGNMENTS).child(assignTitle).child(NodeNames.IS_SUBMIT).get().addOnCompleteListener {curAssign ->
                            val isSubmit = curAssign.result.value.toString()

                            if(isSubmit == "1"){

                                rootReference.child(NodeNames.STUDENT).child(studentId).child(classId)
                                    .child(NodeNames.ASSIGNMENTS).child(assignTitle)
                                    .child(NodeNames.SUBMISSION_LINK).get().addOnCompleteListener { assignLink ->
                                        val fileLink = assignLink.result.value.toString()

                                        rootReference.child(NodeNames.USERS).child(studentId).child(NodeNames.NAME)
                                            .get().addOnCompleteListener {studentDetails ->
                                                val studentName = studentDetails.result.value.toString()

                                                submissionModelList.add(SubmissionModel(studentId, studentName, fileLink))

                                                if (submissionModelList.isNotEmpty())
                                                    emptyListTextView.visibility = View.GONE
                                                submissionAdapter!!.notifyDataSetChanged()
                                            }
                                    }
                            }
                        }
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }

        })
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