package com.example.railwaybooking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register_Admin : AppCompatActivity() {

    private lateinit var etAdminEmail: TextInputEditText
    private lateinit var etAdminUsername: TextInputEditText
    private lateinit var etAdminPhoneNumber: TextInputEditText
    private lateinit var etAdminLicense: TextInputEditText
    private lateinit var etAdminPassword: TextInputEditText
    private lateinit var btnCreateAdminAccount: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var txtLogin: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_admin)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize views
        etAdminEmail = findViewById(R.id.etAdminEmail)
        etAdminUsername = findViewById(R.id.etAdminUsername)
        etAdminPhoneNumber = findViewById(R.id.etAdminPhoneNumber)
        etAdminLicense = findViewById(R.id.etAdminLicense)
        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnCreateAdminAccount = findViewById(R.id.btnCreateAdminAccount)
        progressBar = findViewById(R.id.progress_circular)
        txtLogin = findViewById(R.id.txtLogin)

        btnCreateAdminAccount.setOnClickListener {
            createAdminAccount()
        }


        txtLogin.setOnClickListener {
            btnCreateAdminAccount.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                val intent = Intent(this, Login::class.java)
                startActivity(intent)

                finish()
            }, 3000)
        }

    }




    private fun createAdminAccount() {
        val email = etAdminEmail.text.toString().trim()
        val username = etAdminUsername.text.toString().trim()
        val phoneNumber = etAdminPhoneNumber.text.toString().trim()
        val license = etAdminLicense.text.toString().trim()
        val password = etAdminPassword.text.toString().trim()

        // Basic validation
        if (email.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || license.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnCreateAdminAccount.visibility = View.GONE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    val user = AdminModel(
                        email = email,
                        username = username,
                        phoneNumber = phoneNumber,
                        licenseNumber = license
                    )

                    userId?.let {
                        database.child("users").child(it).setValue(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Admin account created successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Admin_Home::class.java)
                                startActivity(intent)
                                finish()
                                progressBar.visibility = View.GONE
                                btnCreateAdminAccount.visibility = View.VISIBLE

                            } else {
                                Toast.makeText(this, "Failed to create admin: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                                btnCreateAdminAccount.visibility = View.VISIBLE

                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    btnCreateAdminAccount.visibility = View.VISIBLE

                }
            }
    }


}