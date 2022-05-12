package com.brainoptimax.peakstate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.LayoutAddAnchorBinding
import com.brainoptimax.peakstate.model.Resourceful
import com.brainoptimax.peakstate.ui.activity.anchoring.Anchoring2Activity

class AnchoringAdapter(private val resourcefulList :ArrayList<Resourceful>):RecyclerView.Adapter<AnchoringAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_add_anchor, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resourcefulList[position])
    }

    override fun getItemCount(): Int = resourcefulList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = LayoutAddAnchorBinding.bind(view)

        fun bind(resourceful: Resourceful) {
            with(binding){
                tvListAnchor.text = resourceful.anchoring
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Anchoring2Activity::class.java)
                intent.putExtra("RESOURCEFUL", resourceful.anchoring)
                itemView.context.startActivity(intent)
            }
        }
    }


}



