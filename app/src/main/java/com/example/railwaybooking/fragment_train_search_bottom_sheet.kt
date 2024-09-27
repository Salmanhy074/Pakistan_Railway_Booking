package com.example.railwaybooking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.Calendar

class TrainSearchBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_train_search_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etCalender = view.findViewById<MaterialTextView>(R.id.etCalender)
        val btnClose = view.findViewById<MaterialButton>(R.id.btnClose)
        val searchButton = view.findViewById<MaterialButton>(R.id.btnSearch)

        etCalender.setOnClickListener {
            openDatePicker { selectedDate ->
                etCalender.text = selectedDate
            }
        }

        btnClose.setOnClickListener {
            dismiss()
        }

        searchButton.setOnClickListener {
        }
    }

    private fun openDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onDateSelected(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.setCancelable(false)

        datePickerDialog.show()
    }
}
