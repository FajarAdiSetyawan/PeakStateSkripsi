package com.brainoptimax.peakstate.adapter.anchoring

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemListResultAnchoringBinding
import com.brainoptimax.peakstate.model.anchoring.Anchoring
import com.brainoptimax.peakstate.ui.fragment.anchoring.ResultAnchoringFragment
import com.brainoptimax.peakstate.ui.fragment.valuegoals.DetailGoalsFragment

class AnchoringAdapter(var context: Context): RecyclerView.Adapter<AnchoringAdapter.ViewHolder>() {
    private var anchoringList: List<Anchoring>? = null

    fun setAnchoring(anchoring: List<Anchoring>?) {
        this.anchoringList = anchoring
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_result_anchoring, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(anchoringList!![position])
    }

    override fun getItemCount(): Int {
        return if (anchoringList != null) {
            anchoringList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemListResultAnchoringBinding.bind(view)

        fun bind(anchoring: Anchoring) {
            with(binding){
                tvItemMemory.text = anchoring.memory
                tvTimeAnchoring.text = anchoring.dateTime
                tvItemResource.text = anchoring.resourceful
            }
            itemView.setOnClickListener {
                val fragment: Fragment = ResultAnchoringFragment.newInstance(
                    anchoring.resourceful!!,
                    anchoring.memory!!,
                    anchoring.descMemory!!,
                    anchoring.dateTime!!,
                )
                val fm = (context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayoutAnchoring, fragment)
                ft.commit()
            }

        }

    }
}