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
import com.example.classtask.teacher.TeacherClassModel

class StudentFragment : Fragment() {

    private lateinit var studentRecyclerView: RecyclerView
    private var studentAdapter: StudentAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var studentClassModelList: ArrayList<StudentClassModel>
//    private val progressBar: View? = null

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
//        progressBar = view.findViewById(R.id.progressBar)

        studentRecyclerView.layoutManager = LinearLayoutManager(activity)

        studentClassModelList = ArrayList()
        studentAdapter = StudentAdapter(activity, studentClassModelList)

        studentRecyclerView.adapter = studentAdapter

        emptyListTextView.visibility = View.VISIBLE

        val bundle = arguments
        val teacherIdValue = bundle!!.getString("teacherId")
        val codeToJoinValue = bundle!!.getString("codeToJoin")
        Log.i("entered","hurrah!")

        studentClassModelList.add(StudentClassModel("DSA","7th sem","ABCD"))
        studentClassModelList.add(StudentClassModel("Algo","7th sem","efgh"))

        if(teacherIdValue!="" && codeToJoinValue!="") {

            studentClassModelList.add(
                StudentClassModel(
                    teacherIdValue.toString(),
                    codeToJoinValue.toString(),
                    ""
                )
            )
        }

        if (studentClassModelList.isNotEmpty()) emptyListTextView.visibility = View.GONE
    }
}