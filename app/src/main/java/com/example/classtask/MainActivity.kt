package com.example.classtask

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.classtask.teacher.TeacherFragment
import com.example.classtask.student.StudentFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.widget.EditText
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var currentUserName: String
    private lateinit var currentUserEmail: String
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()

    companion object {
        var tabLayout: TabLayout? = null
        var viewPager: ViewPager? = null
        var subject = ""
        var section = ""
        var teacherId = ""
        var codeToJoin = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val account: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
        currentUserName = account.displayName
        currentUserEmail = account.email
        userDetailsToDatabase(currentUserName, currentUserEmail)

        tabLayout = findViewById(R.id.tabLayoutMain)
        viewPager = findViewById(R.id.viewPagerMain)

        setViewPager()

        //click listener for floating button which will show popup menu on button click
        val floatingButton:FloatingActionButton = findViewById(R.id.floatingActionButton)
        floatingButton.setOnClickListener {
            val popupMenu = PopupMenu(this@MainActivity, it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                if(item.itemId == R.id.item_create_class){
                    showCreateClassDialog()
                }else if(item.itemId == R.id.item_join_class){
                    showJoinClassDialog()
                }

                false
            }
            popupMenu.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemProfile) {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            intent.putExtra("name",currentUserName)
            intent.putExtra("email",currentUserEmail)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreateClassDialog() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_create_class)

        //Initializing the views of the dialog.
        val subjectEt: EditText = dialog.findViewById(R.id.tvSubject)
        val sectionEt: EditText = dialog.findViewById(R.id.tvSection)
        val createButton: Button = dialog.findViewById(R.id.createButton)
        createButton.setOnClickListener{
            subject = subjectEt.text.toString()
            section = sectionEt.text.toString()

            if(subject.trim()!="" && section.trim()!="") {
                //add class to database
                val databaseRef = rootReference.child(NodeNames.TEACHER).child(userID).push()
//                val classId = databaseRef.key.toString()

                val hashMap: MutableMap<String, String> = HashMap()

                hashMap[NodeNames.SUBJECT] = subject
                hashMap[NodeNames.SECTION] = section
                hashMap[NodeNames.CREATED_AT] = System.currentTimeMillis().toString()
                hashMap[NodeNames.STUDENT_COUNT] = "0"


                databaseRef.setValue(hashMap).addOnCompleteListener(
                    OnCompleteListener<Void?> {})
            }
            else
                Toast.makeText(this,"Enter Required Values", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showJoinClassDialog() {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_join_class)

        //Initializing the views of the dialog.
        val teacherIdEt: EditText = dialog.findViewById(R.id.tvTeacherId)
        val codeToJoinEt: EditText = dialog.findViewById(R.id.tvCodeToJoin)
        val joinButton: Button = dialog.findViewById(R.id.joinButton)
        joinButton.setOnClickListener{
            teacherId = teacherIdEt.text.toString()
            codeToJoin = codeToJoinEt.text.toString()

            if(teacherId.trim()!="" && codeToJoin.trim()!="") {
                //add class to database
                val databaseRef = rootReference.child(NodeNames.STUDENT).child(userID).child(codeToJoin)
                databaseRef.child(NodeNames.TEACHERID).get().addOnCompleteListener {
                    if(it.result.value == null){
                        val hashMap: MutableMap<String, String> = HashMap()
                        hashMap[NodeNames.TEACHERID] = teacherId

                        databaseRef.setValue(hashMap).addOnCompleteListener {
                            Toast.makeText(this, "Class successfully joined", Toast.LENGTH_SHORT).show()

                            //update student count in teacher class
                            val currentRef =
                                rootReference.child(NodeNames.TEACHER).child(teacherId)
                                    .child(codeToJoin)
                            currentRef.child(NodeNames.STUDENT_COUNT).get().addOnCompleteListener {itr ->
                                val curSnapshot = itr.result
                                val studentCount = curSnapshot.value.toString()
                                var curCount = studentCount.toInt()
                                curCount++
                                currentRef.child(NodeNames.STUDENT_COUNT).setValue(curCount.toString())
                            }
                        }
                    }else{
                        Toast.makeText(this, "Class already joined or wrong credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
                Toast.makeText(this,"Enter Required Values", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    //set details of current user to database
    private fun userDetailsToDatabase(currentUserName: String?, currentUserEmail: String?) {
        if (userID != null) {
            Log.i("userId", userID)
            rootReference.child(NodeNames.USERS).child(userID).child(NodeNames.NAME).setValue(currentUserName).addOnCompleteListener(
                OnCompleteListener<Void?> {})
            rootReference.child(NodeNames.USERS).child(userID).child(NodeNames.EMAIL).setValue(currentUserEmail).addOnCompleteListener(
                OnCompleteListener<Void?> {})
        }
    }

    //this function will set the view pager according to the tab selection
    private fun setViewPager() {
        val mainAdapter = Adapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager?.adapter = mainAdapter

        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    //Fragment adapter class which will handle tab switching
    class Adapter(fm: FragmentManager, behavior: Int): FragmentPagerAdapter(fm, behavior) {
        override fun getCount(): Int {
            return tabLayout!!.tabCount
        }

        override fun getItem(position: Int): Fragment {
            return if(position==0) {
                val fragment = TeacherFragment()
                fragment
            }else{
                val fragment = StudentFragment()
                fragment
            }
        }
    }
}