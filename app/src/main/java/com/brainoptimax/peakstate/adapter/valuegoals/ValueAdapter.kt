package com.brainoptimax.peakstate.adapter.valuegoals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.ui.fragment.valuegoals.AddGoalsFragment
import com.google.android.material.bottomsheet.BottomSheetDialog


class ValueAdapter(// Declare variables to store data from the constructor
    var context: Context,
    var valueName: Array<String>,
    var images: IntArray
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
                        }
                    }
                }else {
                    when {
                        statement.isEmpty() -> {
                            Toast.makeText(context, "Statement Not Blank", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
//                            val intent = Intent(context, AddGoalsActivity::class.java)
//                            intent.putExtra(AddGoalsActivity.EXTRA_VALUE, valueFinal)
//                            intent.putExtra(AddGoalsActivity.EXTRA_STAT, statement)
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            holder.itemView.context.startActivity(intent)
//                            Animatoo.animateSlideLeft(context)
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