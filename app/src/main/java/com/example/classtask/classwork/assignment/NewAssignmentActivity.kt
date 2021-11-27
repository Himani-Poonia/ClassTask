package com.example.classtask.classwork.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewAssignmentActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etDesc: EditText
    private lateinit var etPoints: EditText
//    private lateinit var progressBar: View
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()
    private var prevTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_assignment)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true) //back arrow on action bar
            actionBar.elevation = 0F
        }

        val intent = intent
        title = intent.getStringExtra("className")
        val classId = intent.getStringExtra("classId").toString()
        val isEditing = intent.getBooleanExtra("isEditing", false)
        val assignTitle = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val points = intent.getStringExtra("points").toString()

        etTitle = findViewById(R.id.etTitle)
        etDesc = findViewById(R.id.etDescription)
        etPoints = findViewById(R.id.etPoints)

        etTitle.setText(assignTitle)
        etDesc.setText(description)
        etPoints.setText(points)
        prevTitle = assignTitle

        val saveBtn: Button = findViewById(R.id.saveBtn)
//        progressBar = findViewById(R.id.progressBar)

        saveBtn.setOnClickListener {
//            progressBar.visibility = View.VISIBLE
            updateAssignToDB(classId, isEditing, prevTitle)
        }
    }

    private fun updateAssignToDB(classId: String, isEditing: Boolean, prevTitle: String) {
        val title = etTitle.text.toString().trim()
        val desc = etDesc.text.toString().trim()
        val points = etPoints.text.toString().trim()


        if(title!="" && desc!="" && points!="") {
            if (!isEditing) {
                saveAssignment(classId, title, desc, points)
            } else{
                val assignRef = rootReference.child(NodeNames.TEACHER).child(userID).child(classId)
                    .child(NodeNames.ASSIGNMENTS).child(prevTitle)

                assignRef.removeValue().addOnCompleteListener {
                    val studentAssignRef = rootReference.child(NodeNames.STUDENT)
                    studentAssignRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(studentSnapshot: DataSnapshot) {
                            for (snaps in studentSnapshot.children) {
                                val studentId = snaps.key.toString()

                                //check if student joined the current class and has assignment of previous title
                                studentAssignRef.child(studentId).child(classId)
                                    .child(NodeNames.ASSIGNMENTS).child(prevTitle).removeValue()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }

                saveAssignment(classId, title, desc, points)
            }
        } else {
                Toast.makeText(this@NewAssignmentActivity, "Enter valid values", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun saveAssignment(classId: String, title: String, desc: String, points: String){
        val assignRef = rootReference.child(NodeNames.TEACHER).child(userID).child(classId)
            .child(NodeNames.ASSIGNMENTS)

        assignRef.child(title).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val hashMap: MutableMap<String, String> = HashMap()

                    hashMap[NodeNames.DESCRIPTION] = desc
                    hashMap[NodeNames.POINTS] = points
                    hashMap[NodeNames.CREATED_AT] = System.currentTimeMillis().toString()

                    assignRef.child(title).setValue(hashMap).addOnCompleteListener(
                        OnCompleteListener<Void?> {
//                                    progressBar.visibility = View.GONE
                            //update assignment to students list also
                            val studentAssignRef = rootReference.child(NodeNames.STUDENT)
                            studentAssignRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(studentSnapshot: DataSnapshot) {
                                    for (snaps in studentSnapshot.children) {
                                        val studentId = snaps.key.toString()

                                        //check if student joined the current class
                                        studentAssignRef.child(studentId).child(classId)
                                            .addListenerForSingleValueEvent(object : ValueEventListener{
                                                override fun onDataChange(eachStudentSnap: DataSnapshot) {
                                                    if(eachStudentSnap.exists()){
                                                        val studentAssignMap: MutableMap<String, String> =
                                                            HashMap()

                                                        studentAssignMap[NodeNames.IS_SUBMIT] = "0"
                                                        studentAssignMap[NodeNames.SUBMISSION_LINK] = ""

                                                        studentAssignRef.child(studentId).child(classId).child(NodeNames.ASSIGNMENTS)
                                                            .child(title).setValue(studentAssignMap).addOnCompleteListener(
                                                                OnCompleteListener<Void?> {})
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {}
                                            })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@NewAssignmentActivity,
                                        "Error occured, Try Again!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                            Toast.makeText(
                                this@NewAssignmentActivity,
                                "Assignment Successfully Saved",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        })

                } else {
                    Toast.makeText(
                        this@NewAssignmentActivity,
                        "This title is already given to another Assignment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@NewAssignmentActivity,
                    "Check Internet or try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //to handle back arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}