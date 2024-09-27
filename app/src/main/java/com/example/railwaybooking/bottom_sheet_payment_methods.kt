package com.example.railwaybooking


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheetFragment : BottomSheetDialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_payment_methods, container, false)

        val btnEasypaisa = view.findViewById<Button>(R.id.btnEasypaisa)
        val btnJazzCash = view.findViewById<Button>(R.id.btnJazzCash)
        val btnCardPayment = view.findViewById<Button>(R.id.btnCardPayment)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnEasypaisa.setOnClickListener {
            Toast.makeText(context, "Easypaisa Payment Selected", Toast.LENGTH_SHORT).show()
            //dismiss()
        }

        btnJazzCash.setOnClickListener {
            Toast.makeText(context, "JazzCash Payment Selected", Toast.LENGTH_SHORT).show()
            //dismiss()
        }

        btnCardPayment.setOnClickListener {
            Toast.makeText(context, "Card Payment Selected", Toast.LENGTH_SHORT).show()
            //dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}
