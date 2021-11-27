package com.example.classtask.classwork.assignment.submissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mikhaellopez.circularimageview.CircularImageView

class SubmissionAdapter(
    context: Context?,
    submissionModelList: List<SubmissionModel>,
    classId: String,
    teacherId: String,
    assignTitle: String
):
    RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>() {

    private var context = context
    private var submissionModelList = submissionModelList
    private var classId = classId
    private var teacherId = teacherId
    private var assignTitle = assignTitle
    private var storageReference = FirebaseStorage.getInstance().reference
    private var rootReference = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubmissionViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_submission, parent, false)
        return SubmissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submissionModel: SubmissionModel = submissionModelList[position]
        holder.studentName.text = submissionModel.getStudentName()
        holder.studentFile.text = submissionModel.getFileName()

        checkBox(holder, submissionModel)

        holder.checked.setOnClickListener {
            holder.progressBar.visibility = View.VISIBLE
            val isCheckedRef = rootReference.child(NodeNames.STUDENT).child(submissionModel.getStudentId())
                .child(classId).child(NodeNames.ASSIGNMENTS).child(assignTitle).child(NodeNames.IS_CHECKED)

            isCheckedRef.setValue("0").addOnCompleteListener {
                checkBox(holder, submissionModel)
            }
        }

        holder.notChecked.setOnClickListener {
            holder.progressBar.visibility = View.VISIBLE
            val isCheckedRef = rootReference.child(NodeNames.STUDENT).child(submissionModel.getStudentId())
                .child(classId).child(NodeNames.ASSIGNMENTS).child(assignTitle).child(NodeNames.IS_CHECKED)

            isCheckedRef.setValue("1").addOnCompleteListener {
                checkBox(holder, submissionModel)
            }
        }

        holder.studentFile.setOnClickListener {
            holder.progressBar.visibility = View.VISIBLE
            storageReference.child(NodeNames.ASSIGN_PDFS).child(teacherId).child(classId)
                .child(assignTitle).child(submissionModel.getStudentId())
                .child(submissionModel.getFileName()).downloadUrl.addOnSuccessListener {
//                    Log.i("file uri", it.toString())
                    holder.progressBar.visibility = View.GONE
                    val fileUrl = it.toString()
                    val openUrlIntent = Intent(Intent.ACTION_VIEW)
                    openUrlIntent.data = Uri.parse(fileUrl)
                    context?.startActivity(openUrlIntent)
                }
        }
    }

    private fun checkBox(holder: SubmissionViewHolder, submissionModel: SubmissionModel){
        rootReference.child(NodeNames.STUDENT).child(submissionModel.getStudentId())
            .child(classId).child(NodeNames.ASSIGNMENTS).child(assignTitle).child(NodeNames.IS_CHECKED)
            .get().addOnCompleteListener {
                holder.progressBar.visibility = View.GONE
                if(it.result != null) {
                    val isChecked = it.result.value.toString()
                    Log.i("ischecked", isChecked)
                    if(isChecked=="1"){
                        holder.checked.visibility = View.VISIBLE
                        holder.notChecked.visibility = View.GONE
                    } else{
                        holder.checked.visibility = View.GONE
                        holder.notChecked.visibility = View.VISIBLE
                    }
                }
            }
    }

    override fun getItemCount(): Int {
        return submissionModelList.size
    }

    class SubmissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var studentName: TextView = itemView.findViewById(R.id.tvStudentName)
        var studentFile: TextView = itemView.findViewById(R.id.tvFile)
        var notChecked: CircularImageView = itemView.findViewById(R.id.notCheckedIv)
        var checked: CircularImageView = itemView.findViewById(R.id.checkedIv)
        var progressBar: View = itemView.findViewById(R.id.checkProgressBar)
    }
}