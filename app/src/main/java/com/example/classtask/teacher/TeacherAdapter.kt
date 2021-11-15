package com.example.classtask.teacher

import android.app.Dialog
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import com.example.classtask.R
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.classtask.MainActivity
import com.example.classtask.classwork.AssignmentsActivity

class TeacherAdapter(context: Context, teacherClassModelList: List<TeacherClassModel>):
    RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    private var context = context
    private var teacherClassModelList = teacherClassModelList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherAdapter.TeacherViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_class_teacher, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacherClassModel: TeacherClassModel = teacherClassModelList[position]
        holder.title.text = teacherClassModel.getTitle()
        holder.section.text = teacherClassModel.getSection()
        holder.studentCount.text = teacherClassModel.getStudentCount()

        holder.listItem.setOnClickListener {
            val intent = Intent(context, AssignmentsActivity::class.java)
            intent.putExtra("className",teacherClassModel.getTitle())
            intent.putExtra("isTeacher",true)
            context?.startActivity(intent)
        }

        holder.ivmenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_teacher, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.sendInviteMenu)
                    sendToGoogleMail()
                else if(item.itemId == R.id.editClassItem){
                    showCreateClassDialog(teacherClassModel.getTitle(), teacherClassModel.getSection(),position)
                }else if(item.itemId == R.id. deleteItem){

                }

                false
            }
            popupMenu.show()
        }
    }

    private fun sendToGoogleMail(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Class Invite details to ClassTask")
        intent.putExtra(Intent.EXTRA_TEXT, "Dear Students,\nJoin the Class on" +
                " ClassTask using below details:\n\nTeacher ID: dlvsfknkvn\nCode To Join: vdfji847")
        intent.type = "message/rfc822"
        intent.setPackage("com.google.android.gm")
        context.startActivity(Intent.createChooser(intent,"Send mail..."))
    }

    private fun showCreateClassDialog(currentTitle: String, currentSection: String, position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_create_class)

        //Initializing the views of the dialog.
        val subjectEt: EditText = dialog.findViewById(R.id.tvSubject)
        subjectEt.setText(currentTitle)
        val sectionEt: EditText = dialog.findViewById(R.id.tvSection)
        sectionEt.setText(currentSection)
        val createButton: Button = dialog.findViewById(R.id.createButton)
        createButton.text = "Save"
        createButton.setOnClickListener{
            val subject = subjectEt.text.toString()
            val section = sectionEt.text.toString()

            if(subject.trim()!="" && section.trim()!="") {
                teacherClassModelList[position].setTitle(subject)
                teacherClassModelList[position].setSection(section) //update to database
                val refresh = Intent(context, MainActivity::class.java)
                refresh.putExtra("subject", "")
                refresh.putExtra("section", "")
                refresh.putExtra("teacherId", "")
                refresh.putExtra("codeToJoin", "")
                context.startActivity(refresh)   //remember to connect to DB and remove this refresh
            }
            else
                Toast.makeText(context,"Enter Required Values", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun getItemCount(): Int {
        return teacherClassModelList.size
    }

    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvTitleStudent)
        var section: TextView = itemView.findViewById(R.id.tvSectionStudent)
        var studentCount: TextView = itemView.findViewById(R.id.tvTeacherNameStudent)
        var listItem: CardView = itemView.findViewById(R.id.cvTeacherItem)
        var ivmenu: ImageView = itemView.findViewById(R.id.ivTeacherMenu)
//        private var progressBar: ProgressBar = itemView.findViewById(R.id.requestProgressBar)
    }
}