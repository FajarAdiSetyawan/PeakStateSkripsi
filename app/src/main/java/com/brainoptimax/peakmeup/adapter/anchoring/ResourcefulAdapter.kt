package com.brainoptimax.peakmeup.adapter.anchoring

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.anchoring.Resourceful
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemResourcefulBinding

class ResourcefulAdapter :RecyclerView.Adapter<ResourcefulAdapter.ViewHolder>() {
    private var resourcefulList: List<Resourceful>? = null
    private var onItemClickListener:((Resourceful)->Unit)? = null
    private var onItemDeleteClickListener:((Resourceful)->Unit)? = null

    fun setResourceful(resourceful: List<Resourceful>?) {
        this.resourcefulList = resourceful
    }

    fun setOnItemClickListener(listener: (Resourceful)->Unit) {
        onItemClickListener = listener
    }

    fun setOnItemDeleteClickListener(listener: (Resourceful)->Unit) {
        onItemDeleteClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_resourceful, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resourcefulList!![position])
    }

    override fun getItemCount(): Int {
        return if (resourcefulList != null) {
            resourcefulList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemResourcefulBinding.bind(view)

        fun bind(resourceful: Resourceful) {
            val resourcefulS = resourceful.resourceful
            val id = resourceful.id
            with(binding){
                tvListResourceful.text = resourcefulS
            }

            itemView.setOnClickListener {
                onItemClickListener?.let{
                    it(resourceful)
                }
            }
            binding.ivRemove.setOnClickListener {
                onItemDeleteClickListener?.let{
                    it(resourceful)
                }
            }
        }
    }


}



