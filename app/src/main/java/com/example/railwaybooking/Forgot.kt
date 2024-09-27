package com.example.railwaybooking

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class Forgot : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val forgotButton: MaterialButton = findViewById(R.id.btnForgotPassword)
        val backImage: ImageButton = findViewById(R.id.btnBack)



        backImage.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


        forgotButton.setOnClickListener {

            forgotPassword()

        }
    }


    @SuppressLint("SetTextI18n")
    private fun forgotPassword() {

        val etUsernameOrEmail: TextInputEditText = findViewById(R.id.etUsernameOrEmail)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)
        val btnForgotPassword: MaterialButton = findViewById(R.id.btnForgotPassword)
        val textInputUsernameOrEmail: TextInputLayout = findViewById(R.id.textInputLayout)
        val txtError: TextView = findViewById(R.id.txt)

        textInputUsernameOrEmail.error = null
        txtError.visibility = View.GONE

        val usernameOrEmail = etUsernameOrEmail.text.toString().trim()

        if (usernameOrEmail.isEmpty()) {
            textInputUsernameOrEmail.error = "Email is required"
            return
        }

        progressBar.visibility = View.VISIBLE
        btnForgotPassword.visibility = View.GONE

        auth.sendPasswordResetEmail(usernameOrEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    txtError.visibility = View.VISIBLE
                    txtError.text = "Password reset email sent. Please check your inbox."

                    openEmailClient(usernameOrEmail)
                } else {
                    textInputUsernameOrEmail.error = "Failed to send password reset email: ${task.exception?.message}"
                }

                progressBar.visibility = View.GONE
                btnForgotPassword.visibility = View.VISIBLE
            }
    }

    private fun openEmailClient(email: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Password Reset Request")
            putExtra(Intent.EXTRA_TEXT, "We have received a request to reset your password. \n Please follow the instructions to reset your password.")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Choose an email client"))
        } else {
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show()
        }
    }


}