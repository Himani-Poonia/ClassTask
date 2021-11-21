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
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherFragment : Fragment() {

    private lateinit var teacherRecyclerView: RecyclerView
    private var teacherAdapter: TeacherAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var teacherClassModelList: ArrayList<TeacherClassModel>
    private lateinit var thisContext: Context
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()
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


        val teacherListRef = rootReference.child(NodeNames.TEACHER).child(userID)
        teacherListRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                teacherClassModelList.clear()
                teacherAdapter!!.notifyDataSetChanged()

                for(snaps in snapshot.children) {
                    val uniqueDBId = snaps.key.toString()

                    val thisSubject = snaps.child(NodeNames.SUBJECT).value.toString()
                    val thisSection = snaps.child(NodeNames.SECTION).value.toString()
                    val thisStudentCount = snaps.child(NodeNames.STUDENT_COUNT).value.toString()

                    teacherClassModelList.add(TeacherClassModel(thisSubject, thisSection, thisStudentCount.toInt(), uniqueDBId, userID))
                    if (teacherClassModelList.isNotEmpty())
                        emptyListTextView.visibility = View.GONE
                    teacherAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}