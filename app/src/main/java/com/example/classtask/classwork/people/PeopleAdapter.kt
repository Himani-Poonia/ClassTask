package com.example.classtask.classwork.people

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R

class PeopleAdapter(
    context: Context?,
    peopleModelList: List<PeopleClassModel>
):
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    private var context = context
    private var peopleModelList = peopleModelList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_people, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val peopleClassModel: PeopleClassModel = peopleModelList[position]
        holder.studentName.text = peopleClassModel.getStudentName()
        holder.studentEmail.text = peopleClassModel.getStudentEmail()
        Log.i("teacher","fgh")
    }


    override fun getItemCount(): Int {
        return peopleModelList.size
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var studentName: TextView = itemView.findViewById(R.id.tvPeopleName)
        var studentEmail: TextView = itemView.findViewById(R.id.tvPeopleEmail)
    }
}