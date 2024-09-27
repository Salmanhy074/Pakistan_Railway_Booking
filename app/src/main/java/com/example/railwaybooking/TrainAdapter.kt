package com.example.railwaybooking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainAdapter(private val trains: List<TrainModel>) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    private var onItemClickListener: ((TrainModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (TrainModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.train_card, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trains[position]
        holder.bind(train)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(train)
        }
    }

    override fun getItemCount() = trains.size

    class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTrainName: TextView = itemView.findViewById(R.id.etTrainName)
        private val tvFromStation: TextView = itemView.findViewById(R.id.etFromStation)
        private val tvToStation: TextView = itemView.findViewById(R.id.etToStation)
        private val tvDate: TextView = itemView.findViewById(R.id.etDate)

        fun bind(train: TrainModel) {
            tvTrainName.text = "${train.trainName}"
            tvFromStation.text = "${train.fromStations}"
            tvToStation.text = "${train.toStations}"
            tvDate.text = "${train.date}"

            // Optionally set the image resource for the ImageView if needed
            // imgTrainTrack.setImageResource(R.drawable.traintrack)
        }
    }
}
