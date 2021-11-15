package com.example.classtask.student

import androidx.recyclerview.widget.RecyclerView
import android.view.View

import android.widget.TextView
import com.example.classtask.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.classtask.classwork.AssignmentsActivity

class StudentAdapter(context: Context?, studentClassModelList: List<StudentClassModel>):
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var context = context
    private var studentClassModelList = studentClassModelList

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
    }


    override fun getItemCount(): Int {
        return studentClassModelList.size
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvTitleStudent)
        var section: TextView = itemView.findViewById(R.id.tvSectionStudent)
        var teacherName: TextView = itemView.findViewById(R.id.tvTeacherNameStudent)
        var listItem: CardView = itemView.findViewById(R.id.cvStudentItem)
//        private var progressBar: ProgressBar = itemView.findViewById(R.id.requestProgressBar)
    }
}