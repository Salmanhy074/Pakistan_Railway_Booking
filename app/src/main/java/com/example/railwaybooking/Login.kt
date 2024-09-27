package com.example.railwaybooking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val adminEmail = "admin@example.com"
    private val adminPassword = "admin123"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        val txtRegister: TextView = findViewById(R.id.txtRegister)
        val txtRegisterAdmin: TextView = findViewById(R.id.txtRegisterAdmin)
        val imageBack: ImageButton = findViewById(R.id.btnBack)
        val txtForgotPassword: TextView = findViewById(R.id.forgotPassword)
        val loginButton: MaterialButton = findViewById(R.id.btnLogin)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)

        txtRegister.setOnClickListener {
            loginButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE

                val intent = Intent(this, Register::class.java)
                startActivity(intent)

                finish()
            }, 3000)
        }


        txtRegisterAdmin.setOnClickListener {
            loginButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE

                val intent = Intent(this, Register_Admin::class.java)
                startActivity(intent)

                finish()
            }, 3000)
        }


        imageBack.setOnClickListener {
            val a = Intent(this, Register::class.java)
            startActivity(a)
            finish()
        }

        txtForgotPassword.setOnClickListener {
            val password = Intent(this, Forgot::class.java)
            startActivity(password)
            finish()
        }



        loginButton.setOnClickListener {
            loginUser()
        }


    }


    private fun loginUser() {
        val etUsernameOrEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)
        val btnLogin: MaterialButton = findViewById(R.id.btnLogin)

        val textInputUsernameOrEmail: TextInputLayout = findViewById(R.id.textInputLayout)
        val textInputPassword: TextInputLayout = findViewById(R.id.textInputLayout5)

        textInputUsernameOrEmail.error = null
        textInputPassword.error = null

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.isNullOrEmpty() -> return
                    etUsernameOrEmail.hasFocus() -> textInputUsernameOrEmail.error = null
                    etPassword.hasFocus() -> textInputPassword.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etUsernameOrEmail.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)

        val usernameOrEmail = etUsernameOrEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        var hasError = false

        if (usernameOrEmail.isEmpty()) {
            textInputUsernameOrEmail.error = "Email is required"
            hasError = true
        }
        if (password.isEmpty()) {
            textInputPassword.error = "Password is required"
            hasError = true
        }

        if (hasError) {
            return
        }

        progressBar.visibility = View.VISIBLE
        btnLogin.visibility = View.GONE

        auth.signInWithEmailAndPassword(usernameOrEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    if (userId != null) {
                        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val licenseNumber = dataSnapshot.child("licenseNumber").value as? String

                                val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()

                                val intent = if (!licenseNumber.isNullOrEmpty()) {
                                    Intent(this, Admin_Home::class.java)
                                } else {
                                    Intent(this, Home_Screen::class.java)
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }

                progressBar.visibility = View.GONE
                btnLogin.visibility = View.VISIBLE
            }
    }




}