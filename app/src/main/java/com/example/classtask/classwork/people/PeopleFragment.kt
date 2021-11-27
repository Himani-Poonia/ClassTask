package com.example.classtask.classwork.people

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.example.classtask.student.StudentAdapter
import com.example.classtask.student.StudentClassModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeopleFragment : Fragment() {

    private lateinit var peopleRecyclerView: RecyclerView
    private lateinit var teacherRecyclerView: RecyclerView
    private var peopleAdapter: PeopleAdapter? = null
    private var teacherAdapter: PeopleAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var peopleModelList: ArrayList<PeopleClassModel>
    private lateinit var teacherModelList: ArrayList<PeopleClassModel>
    private var rootReference = FirebaseDatabase.getInstance().reference
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peopleRecyclerView = view.findViewById(R.id.peopleRecyclerView)
        teacherRecyclerView = view.findViewById(R.id.trpplRecyclerView)
        emptyListTextView = view.findViewById(R.id.tvNobody)
        progressBar = view.findViewById(R.id.pplProgressBar)

        val bundle = arguments
        val classId = bundle?.getString("classId").toString()
        val teacherId = bundle?.getString("teacherId").toString()

        //top element is teacher
        teacherRecyclerView.layoutManager = LinearLayoutManager(activity)
        teacherModelList = ArrayList()
        teacherAdapter = PeopleAdapter(activity, teacherModelList)
        teacherRecyclerView.adapter = teacherAdapter

        rootReference.child(NodeNames.USERS).child(teacherId)
            .child(NodeNames.NAME).get().addOnCompleteListener { it ->
                val teacherName = it.result.value.toString()

                rootReference.child(NodeNames.USERS).child(teacherId)
                    .child(NodeNames.EMAIL).get().addOnCompleteListener { curEmail ->
                        val teacherEmail = curEmail.result.value.toString()
                        teacherModelList.add(PeopleClassModel(teacherName, teacherEmail))
                        Log.i("teacher",teacherModelList.size.toString())
                        teacherAdapter!!.notifyDataSetChanged()
                    }
            }

        //student list appears next

        peopleRecyclerView.layoutManager = LinearLayoutManager(activity)

        peopleModelList = ArrayList()
        peopleAdapter = PeopleAdapter(activity, peopleModelList)

        peopleRecyclerView.adapter = peopleAdapter

        emptyListTextView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        val studentListRef = rootReference.child(NodeNames.STUDENT)
        studentListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                peopleModelList.clear()
                progressBar.visibility = View.GONE
                peopleAdapter!!.notifyDataSetChanged()

                for(snaps in snapshot.children) {

                    val studentId = snaps.key.toString()

                    val teacherRef = rootReference.child(NodeNames.STUDENT).child(studentId).child(classId)
                    teacherRef.get().addOnCompleteListener {
                        val classDetailsSnapShot = it.result

                        if(classDetailsSnapShot.value != null){
                            //get details from users db
                            rootReference.child(NodeNames.USERS).child(studentId)
                                .child(NodeNames.NAME).get().addOnCompleteListener { curStudentName ->
                                val studentName = curStudentName.result.value.toString()

                                rootReference.child(NodeNames.USERS).child(studentId)
                                    .child(NodeNames.EMAIL).get().addOnCompleteListener { curStudentEmail ->
                                        val studentEmail = curStudentEmail.result.value.toString()

                                        peopleModelList.add(PeopleClassModel(studentName, studentEmail))

                                        if (peopleModelList.isNotEmpty())
                                            emptyListTextView.visibility = View.GONE
                                        peopleAdapter!!.notifyDataSetChanged()
                                    }
                            }
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