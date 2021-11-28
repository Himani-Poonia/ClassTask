package com.example.classtask.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.classtask.R
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classtask.NodeNames
import com.example.classtask.teacher.TeacherClassModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentFragment : Fragment() {

    private lateinit var studentRecyclerView: RecyclerView
    private var studentAdapter: StudentAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var studentClassModelList: ArrayList<StudentClassModel>
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentRecyclerView = view.findViewById(R.id.studentRecyclerView)
        emptyListTextView = view.findViewById(R.id.tvNoClassJoined)
        progressBar = view.findViewById(R.id.stProgressBar)

        studentRecyclerView.layoutManager = LinearLayoutManager(activity)

        studentClassModelList = ArrayList()
        studentAdapter = StudentAdapter(activity, studentClassModelList)

        studentRecyclerView.adapter = studentAdapter

        emptyListTextView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        val studentListRef = rootReference.child(NodeNames.STUDENT).child(userID)
        studentListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentClassModelList.clear()
                emptyListTextView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                studentAdapter!!.notifyDataSetChanged()

                for(snaps in snapshot.children) {

                    val codeToJoin = snaps.key.toString()

                    val teacherId = snaps.child(NodeNames.TEACHERID).value.toString()

                    val teacherRef = rootReference.child(NodeNames.TEACHER).child(teacherId).child(codeToJoin)
                    teacherRef.get().addOnCompleteListener {
                        val classDetailsSnapShot = it.result
                        val thisSection = classDetailsSnapShot.child(NodeNames.SECTION).value.toString()
                        val thisSubject = classDetailsSnapShot.child(NodeNames.SUBJECT).value.toString()

                        rootReference.child(NodeNames.USERS).child(teacherId).get().addOnCompleteListener{ userDetails ->
                            val teacherName = userDetails.result.child(NodeNames.NAME).value.toString()

                            val curElement = StudentClassModel(thisSubject, thisSection, teacherName,userID, codeToJoin, teacherId)
                            studentClassModelList.add(curElement)

                            if (studentClassModelList.isNotEmpty())
                                emptyListTextView.visibility = View.GONE
                            studentAdapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })
    }
}