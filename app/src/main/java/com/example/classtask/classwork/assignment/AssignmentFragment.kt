package com.example.classtask.classwork.assignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.example.classtask.classwork.AssignmentsActivity
import com.example.classtask.student.StudentAdapter
import com.example.classtask.student.StudentClassModel
import com.example.classtask.teacher.TeacherClassModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AssignmentFragment : Fragment() {
    private lateinit var assignRecyclerView: RecyclerView
    private var assignmentAdapter: AssignmentAdapter? = null
    private lateinit var emptyListTextView: TextView
    private lateinit var assignmentClassModelList: ArrayList<AssignmentClassModel>
    private var rootReference = FirebaseDatabase.getInstance().reference
    private lateinit var progressBar: View

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
        progressBar = view.findViewById(R.id.assignProgressBar)

        val bundle = arguments
        val isTeacher = bundle!!.getBoolean("isTeacher")
        val className = bundle.getString("className").toString()
        val teacherId = bundle.getString("teacherId").toString()
        val classId = bundle.getString("classId").toString()
        val floatingButton: FloatingActionButton = view.findViewById(R.id.floatingActionButton)

        //functioning of floating action button
        if(isTeacher) {
            floatingButton.visibility = View.VISIBLE
            floatingButton.setOnClickListener {
                val intent = Intent(this.activity, NewAssignmentActivity::class.java)
                intent.putExtra("className", className)
                intent.putExtra("classId", classId)
                intent.putExtra("isEditing", false)
                intent.putExtra("title", "")
                intent.putExtra("description", "")
                intent.putExtra("points", "")
                startActivity(intent)
            }
        }
        else{
            floatingButton.visibility = View.GONE
        }

        assignRecyclerView.layoutManager = LinearLayoutManager(activity)

        assignmentClassModelList = ArrayList()
        assignmentAdapter = AssignmentAdapter(activity, assignmentClassModelList, isTeacher, teacherId)

        assignRecyclerView.adapter = assignmentAdapter

        emptyListTextView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        val assignRef = rootReference.child(NodeNames.TEACHER).child(teacherId).child(classId).child(NodeNames.ASSIGNMENTS)
        assignRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                assignmentClassModelList.clear()
                progressBar.visibility = View.GONE
                assignmentAdapter!!.notifyDataSetChanged()

                for(snaps in snapshot.children) {
                    val title = snaps.key.toString()

                    val description = snaps.child(NodeNames.DESCRIPTION).value.toString()
                    val points = snaps.child(NodeNames.POINTS).value.toString()
                    val createdAt = snaps.child(NodeNames.CREATED_AT).value.toString()

                    assignmentClassModelList.add(AssignmentClassModel(title, createdAt, description, points, className, classId))
                    if (assignmentClassModelList.isNotEmpty())
                        emptyListTextView.visibility = View.GONE
                    assignmentAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })
    }
}
