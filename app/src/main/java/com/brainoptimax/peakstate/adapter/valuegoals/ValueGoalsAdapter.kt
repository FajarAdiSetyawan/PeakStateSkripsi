package com.brainoptimax.peakstate.adapter.valuegoals

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemValueGoalsListBinding
import com.brainoptimax.peakstate.model.ValueGoals
import com.brainoptimax.peakstate.ui.activity.goals.DetailValueGoalsActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ValueGoalsAdapter(private val data :ArrayList<ValueGoals>): RecyclerView.Adapter<ValueGoalsAdapter.ViewHolder>()  {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_value_goals_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemValueGoalsListBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(valueGoals: ValueGoals){
            binding.tvValue.text = valueGoals.value
            binding.tvDesc.text = valueGoals.descValue
            binding.tvDatetime.text = valueGoals.dateTime
            val imgValue = valueGoals.imgValue
            if (imgValue == "null"){
                binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
            }else{
                Picasso.get().load(imgValue).into(binding.ivIconValue)
            }

//                binding.ivMenu.setOnClickListener {
//                    val popup = PopupMenu(itemView.context,binding.ivMenu)
//                    popup.inflate(R.menu.menu_value_goals)
//                    popup.setOnMenuItemClickListener {
//
//                        when(it.itemId){
//                            R.id.menu_edit->{
//                                val intent = Intent(itemView.context, EditValueGoalsActivity::class.java)
//                                intent.putExtra("valueId", valueGoals.id)
//                                itemView.context.startActivity(intent)
//                                Animatoo.animateSlideRight(itemView.context)
//                            }
//                            R.id.menu_delete->{
//                                MaterialAlertDialogBuilder(itemView.context, R.style.MaterialAlertDialogRounded)
//                                    .setTitle("Are you sure")
//                                    .setMessage("Do you want to delete this goals " + valueGoals.value + " ?")
//                                    .setPositiveButton("Ok") { dialog, which ->
//                                        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(valueGoals.id!!).removeValue().addOnSuccessListener {
//                                            Toast.makeText(itemView.context, "Success Delete Goals", Toast.LENGTH_SHORT).show()
//                                        }.addOnFailureListener {
//                                            Toast.makeText(itemView.context, "Error Delete Goals", Toast.LENGTH_SHORT).show()
//                                        }
//                                    }
//                                    .setNegativeButton(
//                                        "Cancel"
//                                    ) { dialog, which -> }
//                                    .show()
//                            }
//                        }
//                        true
//                    }
//                    popup.show()
//                }
            binding.ivDelete.setOnClickListener {
                MaterialAlertDialogBuilder(itemView.context, R.style.MaterialAlertDialogRounded)
                    .setTitle("Are you sure")
                    .setMessage("Do you want to delete this goals " + valueGoals.value + " ?")
                    .setPositiveButton("Ok") { _, _ ->
                        databaseReference.child("Users").child(auth.currentUser!!.uid)
                            .child("ValueGoals").child(valueGoals.id!!).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    itemView.context,
                                    "Success Delete Goals",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                            Toast.makeText(
                                itemView.context,
                                "Error Delete Goals",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> }
                    .show()
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailValueGoalsActivity::class.java)
                intent.putExtra("valueId", valueGoals.id)
                itemView.context.startActivity(intent)
                Animatoo.animateSlideRight(itemView.context)
            }
        }


    }
}

