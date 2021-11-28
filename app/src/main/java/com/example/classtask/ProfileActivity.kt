package com.example.classtask

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularimageview.CircularImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true) //back arrow on action bar
            actionBar.elevation = 0F
        }

        mAuth = FirebaseAuth.getInstance()

        val intent = intent
        val tvName: TextView = findViewById(R.id.tvProfileName)
        tvName.text = intent.getStringExtra("name")
        val tvEmail: TextView = findViewById(R.id.tvProfileEmail)
        tvEmail.text = intent.getStringExtra("email")
        val profileImageView: CircularImageView = findViewById(R.id.ivProfile)

        Glide.with(this)
            .load(Uri.parse(intent.getStringExtra("photoUrl")))
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(profileImageView)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
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