package com.brainoptimax.peakstate.adapter.emotions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemResultEmotionsBinding
import com.brainoptimax.peakstate.model.Emotion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultPositiveAdapter(private val emotionList: ArrayList<Emotion>) :
    RecyclerView.Adapter<ResultPositiveAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_result_emotions, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emotionList[position])
    }

    override fun getItemCount(): Int = emotionList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemResultEmotionsBinding.bind(view)

        fun bind(emotion: Emotion) {
            val totalEmotion = emotion.emotionTotal
            val emotionName = emotion.emotionName

            val refEmotion = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")
            refEmotion.child("total")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val total = snapshot.value.toString()
                            binding.pbEmotions.progress = 0; // call these two methods before setting progress.
                            binding.pbEmotions.max = total.toInt()
                            binding.pbEmotions.progress = totalEmotion!!;
                        } else {
                            binding.pbEmotions.max = 0
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(itemView.context, error.message, Toast.LENGTH_SHORT).show()
                    }
                })

            binding.tvCountEmotions.text = totalEmotion.toString()

            binding.tvEmotion.text = emotionName

            when (emotionName) {
                "Excited" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_exited)
                }
                "Enthusiastic" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_enthusiastic)
                }
                "Happy" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_happy)
                }
                "Grateful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_grateful)
                }
                "Passionate" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_passionate)
                }
                "Joyful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_joyful)
                }
                "Brave" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_brave)
                }
                "Confident" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_confident)
                }
                "Proud" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_proud)
                }
                "Hopeful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_hopeful)
                }
                "Optimistic" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_optimistic)
                }
                "Calm" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_calm)
                }
                "Fun" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_fun)
                }
                "Awful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_awful)
                }
                "Good" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_good)
                }
            }

            itemView.setOnClickListener {

                Toast.makeText(itemView.context, binding.pbEmotions.max.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}