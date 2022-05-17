package com.brainoptimax.peakstate.adapter.emotions

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

class ResultNegativeAdapter(private val emotionList: ArrayList<Emotion>) :
    RecyclerView.Adapter<ResultNegativeAdapter.ViewHolder>() {

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
                            binding.pbEmotions.progress = 0 // call these two methods before setting progress.
                            binding.pbEmotions.max = total.toInt()
                            binding.pbEmotions.progress = totalEmotion!!
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
                "Numb" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_numb)
                }
                "Bad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bad)
                }
                "Bored" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bored)
                }
                "Tired" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_tired)
                }
                "Frustrated" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_frustrated)
                }
                "Stressed" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_stressed)
                }
                "Insecure" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_insecure)
                }
                "Angry" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_angry)
                }
                "Sad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_sad)
                }
                "Afraid" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_afraid)
                }
                "Envious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_envious)
                }
                "Anxious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_anxious)
                }
                "Down" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_down)
                }
            }

        }
    }
}