package com.example.railwaybooking

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast

class SeatSelectionFragment : Fragment() {

    private val seatCount = 80
    private val bookedSeats = listOf(5, 8, 12)
    private val maxSeatSelection = 4
    private val selectedSeats = mutableListOf<Int>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seat_selection, container, false)

        val gridLayout = view.findViewById<GridLayout>(R.id.grid_seat_selection)
        gridLayout.columnCount = 5

        var seatNumber = 1

        for (i in 1..(seatCount / 4)) {
            for (berth in 1..3) {
                val berthButton = createBerthButton(seatNumber)
                gridLayout.addView(berthButton)
                seatNumber++
            }

            val emptySpace = View(context)
            val spaceParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            emptySpace.layoutParams = spaceParams
            gridLayout.addView(emptySpace)

            val seatButton = createSeatButton(seatNumber)
            gridLayout.addView(seatButton)
            seatNumber++
        }

        val continueButton = view.findViewById<Button>(R.id.btn_continue_to_payment)
        continueButton.setOnClickListener {
            if (selectedSeats.isNotEmpty()) {
                val paymentBottomSheet = PaymentBottomSheetFragment()
                paymentBottomSheet.show(parentFragmentManager, "PaymentBottomSheet")
            } else {
                Toast.makeText(context, "Please select at least one seat", Toast.LENGTH_SHORT).show()
            }
        }

        val cancelButton = view.findViewById<Button>(R.id.btnCancel1)
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun createSeatButton(seatNumber: Int): Button {
        val seatButton = Button(context)
        seatButton.text = "S $seatNumber"

        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(8, 8, 8, 8)
        }
        seatButton.layoutParams = params

        if (bookedSeats.contains(seatNumber)) {
            seatButton.setBackgroundColor(Color.GRAY)
            seatButton.isEnabled = false
        } else {
            seatButton.setBackgroundColor(Color.GREEN)
            seatButton.setOnClickListener {
                handleSeatSelection(seatNumber, seatButton)
            }
        }

        return seatButton
    }


    private fun createBerthButton(seatNumber: Int): Button {
        val berthButton = Button(context)
        berthButton.text = "B $seatNumber"

        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(8, 8, 8, 8)
        }
        berthButton.layoutParams = params

        if (bookedSeats.contains(seatNumber)) {
            berthButton.setBackgroundColor(Color.GRAY)
            berthButton.isEnabled = false
        } else {
            berthButton.setBackgroundColor(Color.GREEN)
            berthButton.setOnClickListener {
                handleSeatSelection(seatNumber, berthButton)
            }
        }

        return berthButton
    }

    private fun handleSeatSelection(seatNumber: Int, seatButton: Button) {
        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber)
            seatButton.setBackgroundColor(Color.GREEN)
        } else {
            if (selectedSeats.size < maxSeatSelection) {
                selectedSeats.add(seatNumber)
                seatButton.setBackgroundColor(Color.BLUE)
            } else {
                Toast.makeText(context, "You can only book up to $maxSeatSelection seats", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
