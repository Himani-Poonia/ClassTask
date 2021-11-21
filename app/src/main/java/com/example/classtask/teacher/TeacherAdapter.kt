package com.example.classtask.teacher

import android.app.Dialog
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import com.example.classtask.R
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.classtask.MainActivity
import com.example.classtask.NodeNames
import com.example.classtask.classwork.AssignmentsActivity
import com.example.classtask.student.StudentClassModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherAdapter(context: Context, teacherClassModelList: List<TeacherClassModel>):
    RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    private var context = context
    private var teacherClassModelList = teacherClassModelList
    private var rootReference = FirebaseDatabase.getInstance().reference

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
            context.startActivity(intent)
        }

        holder.ivmenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_teacher, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.sendInviteMenu)
                    sendToGoogleMail(teacherClassModel.getUserId(), teacherClassModel.getUniqueId())
                else if(item.itemId == R.id.editClassItem){
                    showCreateClassDialog(teacherClassModel.getTitle(), teacherClassModel.getSection(),position)
                }else if(item.itemId == R.id. deleteItem){
                    deleteClass(teacherClassModel.getUserId(), teacherClassModel.getUniqueId())
                }

                false
            }
            popupMenu.show()
        }
    }

    private fun sendToGoogleMail(teacherId: String, codeToJoin: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Class Invite details to ClassTask")
        intent.putExtra(Intent.EXTRA_TEXT, "Dear Students,\nJoin the Class on" +
                " ClassTask using below details:\n\nTeacher ID: $teacherId\nCode To Join: $codeToJoin")
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
                val curClassId = teacherClassModelList[position].getUniqueId()
                val userId = teacherClassModelList[position].getUserId()

                rootReference.child(NodeNames.TEACHER).child(userId).child(curClassId).child(NodeNames.SUBJECT).setValue(subject)
                rootReference.child(NodeNames.TEACHER).child(userId).child(curClassId).child(NodeNames.SECTION).setValue(section)
            }
            else
                Toast.makeText(context,"Enter Required Values", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteClass(userId: String, classId: String){
        val teacherRef = rootReference.child(NodeNames.TEACHER).child(userId).child(classId)
        teacherRef.removeValue().addOnCompleteListener {
            Toast.makeText(context, "Class successfully deleted", Toast.LENGTH_SHORT).show()
            rootReference.child(NodeNames.STUDENT).get().addOnCompleteListener { deleteStudentClass ->
                val snapshot = deleteStudentClass.result
                for (snaps in snapshot.children) {
                    val studentId = snaps.key.toString()
                    rootReference.child(NodeNames.STUDENT).child(studentId).child(classId).removeValue()
                }
            }
        }
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