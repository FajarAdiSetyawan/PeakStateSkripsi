package com.brainoptimax.peakstate.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemActivatorBinding
import com.brainoptimax.peakstate.databinding.ItemExpandActivatorBinding
import com.brainoptimax.peakstate.model.activator.RowModel
import com.brainoptimax.peakstate.ui.activity.DetailActivatorActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.bumptech.glide.request.RequestOptions


class ActivatorAdapter(val context: Context, var rowModels: MutableList<RowModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * flag to restrict expand / collapse action it is already expanding / collapsing
     */
    private var actionLock = false

    class MainActivatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemActivatorBinding.bind(view)

        fun bind(rowModel: RowModel){
            with(binding){
                tvMainActivator.text = rowModel.activator.name
//                Picasso.get().load(rowModel.activator.img).into(ivIconActivator)

                ivIconActivator.setImageResource(rowModel.activator.images)
            }
        }
    }

    class ExpandActivatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemExpandActivatorBinding.bind(view)

        fun bind(rowModel: RowModel){
            with(binding){
                tvExpandActivator.text = rowModel.expandActivator.title
                tvExpandActivatorDesc.text = rowModel.expandActivator.desc
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val type = rowModels[position].type
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            RowModel.ACTIVATOR -> MainActivatorViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_activator, parent, false))
            RowModel.EXPAND_ACTIVATOR -> ExpandActivatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_expand_activator, parent, false))
            else -> MainActivatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activator, parent, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = rowModels[position]
        when(row.type){
            RowModel.ACTIVATOR -> {
                (holder as MainActivatorViewHolder).bind(rowModels[position])

                holder.itemView.setOnClickListener {
                    if (!actionLock) {
                        actionLock = true
                        if (row.isExpanded) {
                            row.isExpanded = false
                            collapse(position)
                        } else {
                            row.isExpanded = true
                            expand(position)
                        }
                    }
                }
            }
            RowModel.EXPAND_ACTIVATOR -> {
                (holder as ExpandActivatorViewHolder).bind(row)
                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, DetailActivatorActivity::class.java)
                    intent.putExtra(DetailActivatorActivity.EXTRA_TITLE, row.expandActivator.title)
                    intent.putExtra(DetailActivatorActivity.EXTRA_DESC, row.expandActivator.desc)
                    holder.itemView.context.startActivity(intent)

                }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun expand(position: Int) {

        var nextPosition = position

        val row = rowModels[position]

        when (row.type) {

            RowModel.ACTIVATOR -> {

                /**
                 * add element just below of clicked row
                 */
                for (activator in row.activator.stateList!!) {
                    rowModels.add(++nextPosition, RowModel(RowModel.EXPAND_ACTIVATOR, activator))
                }

                notifyDataSetChanged()
            }

            RowModel.EXPAND_ACTIVATOR -> {

                /**
                 * add element just below of clicked row
                 */
                for (expand in row.expandActivator.detailList!!) {
                    rowModels.add(++nextPosition, RowModel(RowModel.DETAIL, expand))
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun collapse(position: Int) {
        val row = rowModels[position]
        val nextPosition = position + 1

        when (row.type) {

            RowModel.ACTIVATOR -> {

                /**
                 * remove element from below until it ends or find another node of same type
                 */
                outerloop@ while (true) {
                    if (nextPosition == rowModels.size || rowModels.get(nextPosition).type === RowModel.ACTIVATOR) {
                        break@outerloop
                    }

                    rowModels.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }

            RowModel.EXPAND_ACTIVATOR -> {

                /**
                 * remove element from below until it ends or find another node of same type or find another parent node
                 */
                outerloop@ while (true) {
                    if (nextPosition == rowModels.size || rowModels.get(nextPosition).type === RowModel.ACTIVATOR || rowModels.get(nextPosition).type === RowModel.EXPAND_ACTIVATOR
                    ) {
                        break@outerloop
                    }

                    rowModels.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }

    override fun getItemCount() = rowModels.size
}