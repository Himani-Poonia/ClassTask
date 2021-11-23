package com.example.classtask.classwork.assignment.submissions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R

class SubmissionAdapter(context: Context?, submissionModelList: List<SubmissionModel>):
    RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>() {

    private var context = context
    private var submissionModelList = submissionModelList

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

        holder.studentFile.setOnClickListener {

        }
    }


    override fun getItemCount(): Int {
        return submissionModelList.size
    }

    class SubmissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var studentName: TextView = itemView.findViewById(R.id.tvStudentName)
        var studentFile: TextView = itemView.findViewById(R.id.tvFile)
//        private var progressBar: ProgressBar = itemView.findViewById(R.id.requestProgressBar)
    }
}