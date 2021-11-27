package com.example.classtask.classwork.assignment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.example.classtask.classwork.assignment.submissions.SubmissionActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

class AssignmentAdapter(context: Context?, assignmentClassModelList: List<AssignmentClassModel>, isTeacher: Boolean, teacherId: String):
    RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    private var context = context
    private var assignmentClassModelList = assignmentClassModelList
    private var isTeacher = isTeacher
    private var rootReference = FirebaseDatabase.getInstance().reference
    private var teacherId = teacherId

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignmentViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignmentClassModel: AssignmentClassModel = assignmentClassModelList[position]
        holder.title.text = assignmentClassModel.getTitle()

        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

        val assignTime = assignmentClassModel.getTime().toLong()
        val assignmentTime: String = dateFormat.format(assignTime)
//        val splitString = dateTime.split(" ").toTypedArray()
//        val assignmentTime = dateTime

        holder.time.text = "Posted on " + assignmentTime
        holder.menu.visibility = View.GONE

        if(isTeacher){
            holder.menu.visibility = View.VISIBLE

            holder.ivmenu.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_assignment, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {item ->
                    if(item.itemId == R.id.editItem){
                        //edit current assignment
                        val intent = Intent(context, NewAssignmentActivity::class.java)
                        intent.putExtra("className", assignmentClassModel.getClassName())
                        intent.putExtra("classId", assignmentClassModel.getClassId())
                        intent.putExtra("isEditing", true)
                        intent.putExtra("title", assignmentClassModel.getTitle())
                        intent.putExtra("description", assignmentClassModel.getDesc())
                        intent.putExtra("points", assignmentClassModel.getPoints())
                        context?.startActivity(intent)

                    }else if(item.itemId == R.id. deleteItem){
                        //remove this assignment from db
                        deleteAssign(teacherId, assignmentClassModel.getClassId(), assignmentClassModel.getTitle())
                    }

                    false
                }
                popupMenu.show()
            }

            holder.clAssignItem.setOnClickListener {
                val intent = Intent(context, SubmissionActivity::class.java)
                intent.putExtra("title",assignmentClassModel.getTitle())
                intent.putExtra("description",assignmentClassModel.getDesc())
                intent.putExtra("points",assignmentClassModel.getPoints())
                intent.putExtra("isTeacher", true)
                intent.putExtra("teacherId", teacherId)
                intent.putExtra("classId", assignmentClassModel.getClassId())
                intent.putExtra("className", assignmentClassModel.getClassName())
                context?.startActivity(intent)
            }
        } else{
            holder.clAssignItem.setOnClickListener {
                val intent = Intent(context, SubmissionActivity::class.java)
                intent.putExtra("title",assignmentClassModel.getTitle())
                intent.putExtra("description",assignmentClassModel.getDesc())
                intent.putExtra("points",assignmentClassModel.getPoints())
                intent.putExtra("isTeacher", false)
                intent.putExtra("teacherId", teacherId)
                intent.putExtra("classId", assignmentClassModel.getClassId())
                intent.putExtra("className", assignmentClassModel.getClassName())
                context?.startActivity(intent)
            }
        }
    }

    private fun deleteAssign(teacherId: String, classId: String, title: String){
        val assignRef = rootReference.child(NodeNames.TEACHER).child(teacherId).child(classId)
            .child(NodeNames.ASSIGNMENTS).child(title)
        assignRef.removeValue().addOnCompleteListener {
            Toast.makeText(context, "Assignment successfully deleted", Toast.LENGTH_SHORT).show()
            rootReference.child(NodeNames.STUDENT).get().addOnCompleteListener { deleteStudentClass ->
                val snapshot = deleteStudentClass.result
                for (snaps in snapshot.children) {
                    val studentId = snaps.key.toString()
                    rootReference.child(NodeNames.STUDENT).child(studentId).child(classId)
                        .child(NodeNames.ASSIGNMENTS).child(title).removeValue()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return assignmentClassModelList.size
    }

    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvAssignTitle)
        var time: TextView = itemView.findViewById(R.id.tvAssignTime)
        var menu: ImageView = itemView.findViewById(R.id.ivAssignMenu)
        var ivmenu: ImageView = itemView.findViewById(R.id.ivAssignMenu)
        var clAssignItem: ConstraintLayout = itemView.findViewById(R.id.clAssignItem)
//        private var progressBar: ProgressBar = itemView.findViewById(R.id.requestProgressBar)
    }
}