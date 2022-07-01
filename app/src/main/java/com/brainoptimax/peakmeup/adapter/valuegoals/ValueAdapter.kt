package com.brainoptimax.peakmeup.adapter.valuegoals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.ui.valuegoals.fragment.AddGoalsFragment
import com.brainoptimax.peakmeup.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class ValueAdapter(// Declare variables to store data from the constructor
    var context: Context,
    private var valueName: Array<String>,
    private var images: IntArray
) : RecyclerView.Adapter<ValueAdapter.ViewHolder>() {

//    private lateinit var nav : NavController


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_value_goals, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val valueSelected = valueName[position]

        holder.tvValueName.text = valueSelected
        holder.ivValue.setImageResource(images[position])

        holder.itemView.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_value)
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0

            val tvSelectValue = bottomSheetDialog.findViewById<TextView>(R.id.tv_select_value)
            tvSelectValue!!.text = valueSelected

            val etValue = bottomSheetDialog.findViewById<EditText>(R.id.et_value)

            if (valueSelected == "Other") {
                tvSelectValue.visibility = View.GONE
                etValue!!.visibility = View.VISIBLE
            }

            val etStatement = bottomSheetDialog.findViewById<EditText>(R.id.et_steatment)

            val btnStartGoals = bottomSheetDialog.findViewById<Button>(R.id.btn_set_goals)

            btnStartGoals!!.setOnClickListener {
                val customValue = etValue!!.text.toString()
                val statement = etStatement!!.text.toString()

                val valueFinal = tvSelectValue.text.toString()
                if (valueFinal == "Other"){
                    when {
                        customValue.isEmpty() -> {
                            Toast.makeText(context, "Value Not Blank", Toast.LENGTH_SHORT).show()
                        }
                        statement.isEmpty() -> {
                            Toast.makeText(context, "Statement Not Blank", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val fragment: Fragment = AddGoalsFragment.newInstance(
                                customValue,
                                statement
                            )
                            val fm = (context as AppCompatActivity).supportFragmentManager
                            val ft = fm.beginTransaction()
                            ft.replace(R.id.frameLayoutVG, fragment)
                            ft.commit()
                            ft.addToBackStack(null)
                            bottomSheetDialog.dismiss()
                            bottomSheetDialog.hide()
                        }
                    }
                }else {
                    when {
                        statement.isEmpty() -> {
                            Toast.makeText(context, "Statement Not Blank", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val fragment: Fragment = AddGoalsFragment.newInstance(
                                valueFinal,
                                statement
                            )
                            val fm = (context as AppCompatActivity).supportFragmentManager
                            val ft = fm.beginTransaction()
                            ft.replace(R.id.frameLayoutVG, fragment)
                            ft.commit()
                            ft.addToBackStack(null)
                            bottomSheetDialog.dismiss()

                        }
                    }
                }
            }

            bottomSheetDialog.show()
        }
    }

    override fun getItemCount(): Int = valueName.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare member variables for all the Views in a row
        var tvValueName: TextView
        var ivValue: ImageView

        // Create a constructor that accepts the entire row and search the View hierarchy to find each subview
        init {
            // Store the item subviews in member variables
            tvValueName = itemView.findViewById(R.id.tv_value)
            ivValue = itemView.findViewById(R.id.iv_icon_value)
        }
    }


}