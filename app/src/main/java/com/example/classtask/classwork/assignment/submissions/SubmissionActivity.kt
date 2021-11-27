package com.example.classtask.classwork.assignment.submissions

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.classtask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.classtask.Constants
import com.example.classtask.NodeNames
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import android.content.ActivityNotFoundException
import android.util.Log
import android.view.MenuItem
import androidx.annotation.NonNull





class SubmissionActivity : AppCompatActivity() {
    private lateinit var submitBtn: Button
    private lateinit var submitLinkLayout: ConstraintLayout
    private lateinit var tvFileName: TextView
    private lateinit var file: File

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private val userID: String = firebaseUser?.uid.toString()
    private var rootReference = FirebaseDatabase.getInstance().reference
    private var storageReference = FirebaseStorage.getInstance().reference
    private var classId = ""
    private var teacherId = ""
    private var assignTitle = ""
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true) //back arrow on action bar
            actionBar.elevation = 0F
        }

        val intent = intent
        title = intent.getStringExtra("className")

        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvDesc: TextView = findViewById(R.id.tvDescription)
        val tvPoints: TextView = findViewById(R.id.tvPoints)
        submitBtn = findViewById(R.id.uploadBtn)
        submitLinkLayout = findViewById(R.id.clStudentSubmit)
        tvFileName = findViewById(R.id.tvFileName)
        val pdftv: TextView = findViewById(R.id.pdfTv)
        progressBar = findViewById(R.id.submitProgressBar)

        //set values accordingly
        assignTitle = intent.getStringExtra("title").toString()
        tvTitle.text = assignTitle
        tvDesc.text = intent.getStringExtra("description")
        tvPoints.text = intent.getStringExtra("points")
        classId = intent.getStringExtra("classId").toString()
        teacherId = intent.getStringExtra("teacherId").toString()

        if(intent.getBooleanExtra("isTeacher", false)){
            submitBtn.text = "Submissions"
            submitLinkLayout.visibility = View.GONE
            pdftv.visibility = View.GONE

            submitBtn.setOnClickListener {
                val curIntent = Intent(this, TotalSubmissionsActivity::class.java)
                curIntent.putExtra("title", assignTitle)
                curIntent.putExtra("classId", classId)
                curIntent.putExtra("teacherId", teacherId)
                startActivity(curIntent)
            }
        }
        else{
            submitBtn.text = "Upload"
            submitLinkLayout.visibility = View.GONE
            pdftv.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            //check if assignment already submitted
            checkIfSubmitted()

            submitBtn.setOnClickListener {
                //open files directory of phone
                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.type = "application/pdf"
                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                startActivityForResult(chooseFile, Constants.PICKFILE_RESULT_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constants.PICKFILE_RESULT_CODE && resultCode == RESULT_OK){
            val returnUri = data?.data
            file = File(returnUri?.path.toString())
            val fileName = file.name

            //check extension of file if it is pdf or not
            val filenameArray = fileName.split(".").toTypedArray()
            val extension = filenameArray[filenameArray.size - 1]

            progressBar.visibility = View.VISIBLE

            if(extension == "pdf"){
                uploadFileToDB(fileName, returnUri)
            } else{
                Toast.makeText(this, "Upload only pdfs", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun uploadFileToDB(fileName: String, returnUri: Uri?) {
        val fileStoreRef = storageReference.child(NodeNames.ASSIGN_PDFS).child(teacherId)
                                .child(classId).child(assignTitle).child(userID).child(fileName)

        if (returnUri != null) {
            fileStoreRef.putFile(returnUri).addOnCompleteListener {
                //after file storage in firebase, update realtime DB
                val assignRef = rootReference.child(NodeNames.STUDENT).child(userID).child(classId).child(NodeNames.ASSIGNMENTS)
                    .child(assignTitle)

                assignRef.child(NodeNames.IS_SUBMIT).setValue("1")
                assignRef.child(NodeNames.SUBMISSION_LINK).setValue(fileName)

                tvFileName.text = fileName
                submitLinkLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun checkIfSubmitted(){
        val assignRef = rootReference.child(NodeNames.STUDENT).child(userID).child(classId).child(NodeNames.ASSIGNMENTS)
            .child(assignTitle)
        assignRef.get().addOnCompleteListener {
            progressBar.visibility = View.GONE
            val snap = it.result

            if(snap.child(NodeNames.IS_SUBMIT).value.toString() == "1"){
                tvFileName.text = snap.child(NodeNames.SUBMISSION_LINK).value.toString()
                submitLinkLayout.visibility = View.VISIBLE
            }
        }
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