package com.example.railwaybooking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class HomeFragment : Fragment() {


    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var txtName: TextView
    private lateinit var etCalender: TextView
    private lateinit var txtSearch: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainAdapter: TrainAdapter
    private val trainList = mutableListOf<TrainModel>()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        txtName = view.findViewById(R.id.txtUserName)

        txtSearch = view.findViewById(R.id.txtSearch)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        recyclerView = view.findViewById(R.id.recyclerViewSearchTrains)
        recyclerView.layoutManager = LinearLayoutManager(context)
        trainAdapter = TrainAdapter(trainList)
        recyclerView.adapter = trainAdapter

        database = FirebaseDatabase.getInstance().reference.child("Trains")

        trainAdapter.setOnItemClickListener { train ->
            // Open SeatSelectionFragment as a full fragment
            val seatSelectionFragment = SeatSelectionFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, seatSelectionFragment)
                .addToBackStack(null)
                .commit()
        }


        fetchTrainData()
        loadUserData()
        return view

    }




    private fun fetchTrainData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trainList.clear()
                for (trainSnapshot in snapshot.children) {
                    val train = trainSnapshot.getValue(TrainModel::class.java)
                    if (train != null) {
                        trainList.add(train)
                    }
                }
                trainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: "Guest User"
                        txtName.text = name
                    } else {
                        txtName.text = "User not found"
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onCancelled(error: DatabaseError) {
                    txtName.text = "Error loading user data: ${error.message}"
                }
            })
        } else {
            txtName.text = "User not logged in"
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtSearch = view.findViewById<TextView>(R.id.txtSearch)

        txtSearch.setOnClickListener {
            val bottomSheetFragment = TrainSearchBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }



}