package com.brainoptimax.peakstate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.LayoutAddAnchorBinding
import com.brainoptimax.peakstate.model.Memory
import com.brainoptimax.peakstate.ui.activity.anchoring.Anchoring2Activity
import com.brainoptimax.peakstate.ui.activity.anchoring.Anchoring4Activity

class MemoryAdapter(private val memoryList :ArrayList<Memory>): RecyclerView.Adapter<MemoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_add_anchor, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memoryList[position])
    }

    override fun getItemCount(): Int = memoryList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = LayoutAddAnchorBinding.bind(view)

        fun bind(memory: Memory) {
            with(binding){
                tvListAnchor.text = memory.memory
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Anchoring4Activity::class.java)
                intent.putExtra("MEMORY", memory.memory)
                itemView.context.startActivity(intent)
            }
        }
    }


}