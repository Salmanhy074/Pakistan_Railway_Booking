package com.example.railwaybooking

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {



    private lateinit var txtLogout: TextView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtNumber: TextView
    private lateinit var txtForgotPassword: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        txtName = view.findViewById(R.id.txtName)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtNumber = view.findViewById(R.id.txtNumber)
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword)
        progressBar = view.findViewById(R.id.progressBar)
        txtLogout = view.findViewById(R.id.txtLogout)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        txtLogout.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            auth.signOut()
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().supportFragmentManager.popBackStack() // Close the fragment
            }, 3000)
        }


        txtForgotPassword.setOnClickListener {
            progressBar.visibility = View.VISIBLE


            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                val intent = Intent(requireContext(), Forgot::class.java)
                startActivity(intent)
                progressBar.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }, 3000)

        }

        loadUserData()
    }



    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            val userRef = database.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: "Guest User"
                        val email = snapshot.child("email").getValue(String::class.java) ?: "No Email Number Available"
                        val contactNumber = snapshot.child("phone").getValue(String::class.java) ?: "No Contact Number Available"

                        txtName.text = name
                        txtEmail.text = email
                        txtNumber.text = contactNumber
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }



}