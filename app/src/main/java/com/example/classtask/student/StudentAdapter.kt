package com.example.classtask.student

import androidx.recyclerview.widget.RecyclerView
import android.view.View

import android.widget.TextView
import com.example.classtask.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.classtask.MainActivity
import com.example.classtask.NodeNames
import com.example.classtask.classwork.AssignmentsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentAdapter(context: Context?, studentClassModelList: List<StudentClassModel>):
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var context = context
    private var studentClassModelList = studentClassModelList
    private var rootReference = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_class_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentClassModel: StudentClassModel = studentClassModelList[position]
        holder.title.text = studentClassModel.getTitle()
        holder.section.text = studentClassModel.getSection()
        holder.teacherName.text = studentClassModel.getTeacherName()

        holder.listItem.setOnClickListener {
            val intent = Intent(context,AssignmentsActivity::class.java)
            intent.putExtra("className",studentClassModel.getTitle())
            intent.putExtra("isTeacher",false)
            context?.startActivity(intent)
        }

        holder.ivmenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_student, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.leaveItem)
                    leaveClass(studentClassModel.getUserId(), studentClassModel.getCodeToJoin(), studentClassModel.getTeacherId())

                false
            }
            popupMenu.show()
        }
    }

    private fun leaveClass(userId: String, codeToJoin: String, teacherId: String) {
        val studentRef = rootReference.child(NodeNames.STUDENT).child(userId).child(codeToJoin)
        studentRef.removeValue().addOnCompleteListener{
            val teacherRef = rootReference.child(NodeNames.TEACHER).child(teacherId).child(
                codeToJoin
            )
            teacherRef.child(NodeNames.STUDENT_COUNT).get().addOnCompleteListener {
                val curSnapshot = it.result
                val studentCount = curSnapshot.value.toString()
                var curCount = studentCount.toInt()
                curCount--
                if(curCount>=0)
                    teacherRef.child(NodeNames.STUDENT_COUNT).setValue(curCount.toString())
            }
        }
    }


    override fun getItemCount(): Int {
        return studentClassModelList.size
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvTitleStudent)
        var section: TextView = itemView.findViewById(R.id.tvSectionStudent)
        var teacherName: TextView = itemView.findViewById(R.id.tvTeacherNameStudent)
        var listItem: CardView = itemView.findViewById(R.id.cvStudentItem)
        var ivmenu: ImageView = itemView.findViewById(R.id.ivStudentMenu)
//        private var progressBar: ProgressBar = itemView.findViewById(R.id.requestProgressBar)
    }
}