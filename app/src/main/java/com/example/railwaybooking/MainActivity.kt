package com.example.railwaybooking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private val splashScreenTime: Long = 3000
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        Handler(Looper.getMainLooper()).postDelayed({
            showNoInternetDialogIfNeeded()
        }, splashScreenTime)
    }




    private fun checkUserStatus() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

                userRef.get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val licenseNumber = dataSnapshot.child("licenseNumber").value as? String

                        if (licenseNumber.isNullOrEmpty()) {
                            startActivity(Intent(this, Home_Screen::class.java))
                        } else {
                            startActivity(Intent(this, Admin_Home::class.java))
                        }
                    } else {
                        startActivity(Intent(this, Login::class.java))
                    }
                    finish()
                }.addOnFailureListener {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }




    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetDialogIfNeeded() {
        if (!isInternetConnected()) {
            showNoInternetDialog()
        } else {
            checkUserStatus()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.network_dialog, null)
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnRetry = dialogView.findViewById<MaterialButton>(R.id.retryButton)
        val btnClose = dialogView.findViewById<MaterialButton>(R.id.closeButton)
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progress_circular)

        btnRetry.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE

                if (isInternetConnected()) {
                    alertDialog.dismiss()
                    checkUserStatus()
                } else {
                    dialogView.findViewById<TextView>(R.id.textView3).text = "No internet connection!"
                }
            }, 3000)
        }


        btnClose.setOnClickListener {
            alertDialog.dismiss()
            finishAffinity()
        }

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}
