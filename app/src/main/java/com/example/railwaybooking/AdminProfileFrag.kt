package com.example.railwaybooking

import android.annotation.SuppressLint
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


class AdminProfileFrag : Fragment() {

    private lateinit var txtLogout: TextView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtNumber: TextView
    private lateinit var txtLicense: TextView
    private lateinit var txtForgotPassword: TextView
    private lateinit var txtTrains: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        txtLogout = view.findViewById(R.id.txtLogoutAdmin)
        txtName = view.findViewById(R.id.txtNameAdmin)
        txtEmail = view.findViewById(R.id.txtEmailAdmin)
        txtNumber = view.findViewById(R.id.txtNumberAdmin)
        txtLicense = view.findViewById(R.id.txtLicenceAdmin)
        txtTrains = view.findViewById(R.id.txtTrains)
        txtForgotPassword = view.findViewById(R.id.txtForgotPasswordAdmin)

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
                requireActivity().supportFragmentManager.popBackStack()
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
        loadTrains()

    }


    @SuppressLint("SetTextI18n")
    private fun loadTrains() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val trainsRef = FirebaseDatabase.getInstance().getReference("Trains")

        trainsRef.orderByChild("adminId").equalTo(userId).get().addOnSuccessListener { dataSnapshot ->
            val trainCount = dataSnapshot.childrenCount

            if (trainCount > 0) {
                txtTrains.text = "Trains uploaded by you: $trainCount"
            } else {
                txtTrains.text = "Trains uploaded by you: 0"
            }
        }.addOnFailureListener { exception ->
            txtTrains.text = "Error loading trains"
        }
    }



    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            val userRef = database.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("username").getValue(String::class.java) ?: "Guest User"
                        val email = snapshot.child("email").getValue(String::class.java) ?: "No Email Number Available"
                        val contactNumber = snapshot.child("phoneNumber").getValue(String::class.java) ?: "No Contact Number Available"
                        val licenseNumber = snapshot.child("licenseNumber").getValue(String::class.java) ?: "No License Number Available"

                        txtName.text = name
                        txtEmail.text = email
                        txtNumber.text = contactNumber
                        txtLicense.text = licenseNumber
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

}