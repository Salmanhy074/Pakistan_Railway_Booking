package com.example.railwaybooking

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AdminFrag_Home : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var etTrainName: TextInputEditText
    private lateinit var etCoaches: TextInputEditText
    private lateinit var etEngineNumber: TextInputEditText
    private lateinit var etDate: TextView
    private lateinit var etFromStations: TextInputEditText
    private lateinit var etToStations: TextInputEditText
    private lateinit var etIntermediateStations: TextInputEditText
    private lateinit var addTrain: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_frag__home, container, false)

        database = FirebaseDatabase.getInstance().reference.child("Trains")

        etTrainName = view.findViewById(R.id.etTrainName)
        etCoaches = view.findViewById(R.id.etCoaches)
        etEngineNumber = view.findViewById(R.id.etEngineNumber)
        etDate = view.findViewById(R.id.etDate)
        addTrain = view.findViewById(R.id.btnAddTrain)
        etFromStations = view.findViewById(R.id.etFromStation)
        etToStations = view.findViewById(R.id.etToStation)
        etIntermediateStations = view.findViewById(R.id.etIntermediateStations)

        // Set up DatePickerDialog when etDate is clicked
        etDate.setOnClickListener {
            openDatePicker()
        }

        addTrain.setOnClickListener {
            uploadTrainData()
        }

        return view
    }



     private fun openDatePicker() {
         val calendar = Calendar.getInstance()
         val year = calendar.get(Calendar.YEAR)
         val month = calendar.get(Calendar.MONTH)
         val day = calendar.get(Calendar.DAY_OF_MONTH)

         val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
             val formattedDate = "${selectedDay}-${selectedMonth + 1}-${selectedYear}"
             etDate.text = formattedDate
         }, year, month, day)

         datePickerDialog.setCancelable(false)

         datePickerDialog.show()
     }


   /* private fun openDatePicker() {
        // Create an instance of MaterialDatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()


        // Show the date picker
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

        // Set a listener to retrieve the selected date
        datePicker.addOnPositiveButtonClickListener { selection ->
            // Convert the selected date (which is in milliseconds) to a readable format
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormatter.format(Date(selection))
            etDate.text = formattedDate
        }
    }*/


    private fun uploadTrainData() {
        val trainName = etTrainName.text.toString().trim()
        val coaches = etCoaches.text.toString().trim()
        val engineNumber = etEngineNumber.text.toString().trim()
        val date = etDate.text.toString().trim()
        val fromStations = etFromStations.text.toString().trim()
        val toStations = etToStations.text.toString().trim()
        val intermediateStations = etIntermediateStations.text.toString().trim()

        if (trainName.isEmpty() || coaches.isEmpty() || engineNumber.isEmpty() || date.isEmpty() || fromStations.isEmpty() || toStations.isEmpty() || intermediateStations.isEmpty()) {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val trainId = database.push().key ?: ""

        val train = TrainModel(trainId, trainName, coaches, engineNumber, date, fromStations, toStations, intermediateStations,
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        )

        database.child(trainId).setValue(train).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Train information uploaded", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(context, "Failed to upload train data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etTrainName.text?.clear()
        etCoaches.text?.clear()
        etEngineNumber.text?.clear()
        etDate.text = ""
        etFromStations.text?.clear()
        etToStations.text?.clear()
        etIntermediateStations.text?.clear()
    }
}
