package com.example.classtask.classwork.assignment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.R

class AssignmentAdapter(context: Context?, assignmentClassModelList: List<AssignmentClassModel>, isTeacher: Boolean):
    RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    private var context = context
    private var assignmentClassModelList = assignmentClassModelList
    private var isTeacher = isTeacher

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
        holder.time.text = assignmentClassModel.getTime()
        holder.menu.visibility = View.GONE

        if(isTeacher){
            holder.menu.visibility = View.VISIBLE

            holder.ivmenu.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_assignment, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {item ->
                    if(item.itemId == R.id.editItem){
                        //open activity with previous assignment data
                    }else if(item.itemId == R.id. deleteItem){
                        //remove this assignment from db
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
                intent.putExtra("className", assignmentClassModel.getClassName())
                context?.startActivity(intent)
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