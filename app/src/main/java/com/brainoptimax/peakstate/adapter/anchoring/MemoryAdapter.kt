package com.brainoptimax.peakstate.adapter.anchoring

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemResourcefulBinding
import com.brainoptimax.peakstate.model.anchoring.Memory

class MemoryAdapter : RecyclerView.Adapter<MemoryAdapter.ViewHolder>() {
    private var memoryList: List<Memory>? = null
    private var onItemClickListener:((Memory)->Unit)? = null
    private var onItemDeleteClickListener:((Memory)->Unit)? = null

    fun setMemory(memory: List<Memory>?) {
        this.memoryList = memory
    }

    fun setOnItemClickListener(listener: (Memory)->Unit) {
        onItemClickListener = listener
    }

    fun setOnItemDeleteClickListener(listener: (Memory)->Unit) {
        onItemDeleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_resourceful, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memoryList!![position])
    }

    override fun getItemCount(): Int {
        return if (memoryList != null) {
            memoryList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemResourcefulBinding.bind(view)

        fun bind(memory: Memory) {
            with(binding){
                tvListResourceful.text = memory.memory
            }
            itemView.setOnClickListener {
                onItemClickListener?.let{
                    it(memory)
                }
            }
            binding.ivRemove.setOnClickListener {
                onItemDeleteClickListener?.let{
                    it(memory)
                }
            }
        }
    }


}