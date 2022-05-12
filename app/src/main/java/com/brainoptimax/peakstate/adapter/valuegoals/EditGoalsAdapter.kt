package com.brainoptimax.peakstate.adapter.valuegoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemEditGoalsBinding
import com.brainoptimax.peakstate.model.Goals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditGoalsAdapter(private var goalsList: ArrayList<Goals>, val valueId: String?) :
    RecyclerView.Adapter<EditGoalsAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var valueid: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val inflate =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_edit_goals, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goalsList[position])
    }

    override fun getItemCount(): Int = goalsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemEditGoalsBinding.bind(view)
        fun bind(goals: Goals) {
            valueid = valueId

            with(binding) {
                binding.tvListGoals.text = goals.goals

                binding.ivRemoveGoals.setOnClickListener {
                    databaseReference.child("Users").child(auth.currentUser!!.uid)
                        .child("ValueGoals")
                        .child(valueid!!).child("goals").child(adapterPosition.toString()).removeValue().addOnSuccessListener {
                            Toast.makeText(itemView.context, "Success Delete Value", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(itemView.context, "Error Delete Value", Toast.LENGTH_SHORT).show()

                        }
                }
            }
        }
    }

}
