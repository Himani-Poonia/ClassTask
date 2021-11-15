package com.example.classtask.classwork.assignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R
import com.example.classtask.student.StudentAdapter
import com.example.classtask.student.StudentClassModel

class AssignmentFragment : Fragment() {
    private lateinit var assignRecyclerView: RecyclerView
    private var assignmentAdapter: AssignmentAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var assignmentClassModelList: ArrayList<AssignmentClassModel>
//    private val progressBar: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assignRecyclerView = view.findViewById(R.id.assignRecyclerView)
        emptyListTextView = view.findViewById(R.id.tvNoAssign)
//        progressBar = view.findViewById(R.id.progressBar)

        val bundle = arguments
        val isTeacher = bundle!!.getBoolean("isTeacher")

        assignRecyclerView.layoutManager = LinearLayoutManager(activity)

        assignmentClassModelList = ArrayList()
        assignmentAdapter = AssignmentAdapter(activity, assignmentClassModelList, isTeacher)

        assignRecyclerView.adapter = assignmentAdapter

        emptyListTextView.visibility = View.VISIBLE

//        val bundle = arguments
//        val classSubject = bundle!!.getString("classTitle")

        assignmentClassModelList.add(AssignmentClassModel("First","Posted on 12:30"))
        assignmentClassModelList.add(AssignmentClassModel("Second","Posted on 11:00"))


        if (assignmentClassModelList.isNotEmpty()) emptyListTextView.visibility = View.GONE
    }
}
