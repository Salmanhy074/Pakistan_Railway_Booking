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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var registerButton: MaterialButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val txtLogin: TextView = findViewById(R.id.txtLogin)
        val backImage: ImageButton = findViewById(R.id.btnBack)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)
        registerButton = findViewById(R.id.btnCreateAccount)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        txtLogin.setOnClickListener {
            registerButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                val intent = Intent(this, Login::class.java)
                startActivity(intent)

                finish()
            }, 3000)
        }


        backImage.setOnClickListener{
            val a = Intent(this, Login::class.java)
            startActivity(a)
            finish()
        }


        registerButton.setOnClickListener {
            /*registerButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                registerButton.visibility = View.VISIBLE

            }, 3000)*/

            registerUser()
        }

    }


    private fun registerUser() {

        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etUsername: TextInputEditText = findViewById(R.id.etUsername)
        val etPhoneNumber: TextInputEditText = findViewById(R.id.etPhoneNumber)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)

        val textInputEmail: TextInputLayout = findViewById(R.id.textInputLayout)
        val textInputUsername: TextInputLayout = findViewById(R.id.textInput)
        val textInputPhoneNumber: TextInputLayout = findViewById(R.id.textInputLayout2)
        val textInputPassword: TextInputLayout = findViewById(R.id.textInputLayout5)

        textInputEmail.error = null
        textInputUsername.error = null
        textInputPhoneNumber.error = null
        textInputPassword.error = null

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.isNullOrEmpty() -> return
                    etEmail.hasFocus() -> textInputEmail.error = null
                    etUsername.hasFocus() -> textInputUsername.error = null
                    etPhoneNumber.hasFocus() -> textInputPhoneNumber.error = null
                    etPassword.hasFocus() -> textInputPassword.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etEmail.addTextChangedListener(textWatcher)
        etUsername.addTextChangedListener(textWatcher)
        etPhoneNumber.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)

        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val password = etPassword.text.toString().trim()

        var hasError = false

        if (email.isEmpty()) {
            textInputEmail.error = "Email is required"
            hasError = true
        }
        if (username.isEmpty()) {
            textInputUsername.error = "Username is required"
            hasError = true
        }
        if (phoneNumber.isEmpty()) {
            textInputPhoneNumber.error = "Phone number is required"
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
        registerButton.visibility = View.GONE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    val user = UserModel(username, email, phoneNumber)
                    userId?.let {
                        database.child("users").child(it).setValue(user)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    textInputUsername.error = "Failed to store user data: ${dbTask.exception?.message}"
                                }
                            }
                    }

                    progressBar.visibility = View.GONE
                    registerButton.visibility = View.VISIBLE

                } else {
                    textInputEmail.error = "Failed to register: ${task.exception?.message}"
                    progressBar.visibility = View.GONE
                    registerButton.visibility = View.VISIBLE
                }
            }
    }


}