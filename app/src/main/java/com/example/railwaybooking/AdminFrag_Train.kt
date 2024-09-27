package com.example.railwaybooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AdminFrag_Train : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainAdapter: TrainAdapter
    private lateinit var database: DatabaseReference
    private val trainList = mutableListOf<TrainModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_frag__train, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewTrains)
        recyclerView.layoutManager = LinearLayoutManager(context)
        trainAdapter = TrainAdapter(trainList)
        recyclerView.adapter = trainAdapter

        database = FirebaseDatabase.getInstance().reference.child("Trains")

        fetchTrainData()

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
}