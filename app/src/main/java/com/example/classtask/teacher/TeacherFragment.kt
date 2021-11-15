package com.example.classtask.teacher

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R

class TeacherFragment : Fragment() {

    private lateinit var teacherRecyclerView: RecyclerView
    private var teacherAdapter: TeacherAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var teacherClassModelList: ArrayList<TeacherClassModel>
    private lateinit var thisContext: Context
//    private val progressBar: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        thisContext = container!!.context
        return inflater.inflate(R.layout.fragment_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teacherRecyclerView = view.findViewById(R.id.teacherRecyclerView)
        emptyListTextView = view.findViewById(R.id.tvNoClassCreated)
//        progressBar = view.findViewById(R.id.progressBar)

        teacherRecyclerView.layoutManager = LinearLayoutManager(activity)

        teacherClassModelList = ArrayList()
        teacherAdapter = TeacherAdapter(thisContext, teacherClassModelList)

        teacherRecyclerView.adapter = teacherAdapter

        emptyListTextView.visibility = View.VISIBLE

        //take data from mainActivity and add to list
        val bundle = arguments
        val subjectValue = bundle!!.getString("subject")
        val sectionValue = bundle!!.getString("section")
        Log.i("entered","hurrah!")

        teacherClassModelList.add(TeacherClassModel("DSA","7th sem",14))
        teacherClassModelList.add(TeacherClassModel("Algo","7th sem",50))

        if(subjectValue!="" && sectionValue!="") {

            teacherClassModelList.add(
                TeacherClassModel(
                    subjectValue.toString(),
                    sectionValue.toString(),
                    0
                )
            )
        }
        if (teacherClassModelList.isNotEmpty()) emptyListTextView.visibility = View.GONE
    }
}