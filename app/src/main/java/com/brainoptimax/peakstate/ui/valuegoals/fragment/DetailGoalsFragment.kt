package com.brainoptimax.peakstate.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.DetailValueGoalsAdapter
import com.brainoptimax.peakstate.databinding.FragmentDetailGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.brainoptimax.peakstate.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.valuegoals.ValueGoalsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ID = "id"
private const val VALUE = "value"
private const val STATEMENT = "statement"
private const val DESC = "desc"
private const val IMG = "img"
private const val DATETIME = "datetime"

class DetailGoalsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var value: String? = null
    private var statement: String? = null
    private var desc: String? = null
    private var img: String? = null
    private var datetime: String? = null

    private var fragmentDetailGoalsBinding: FragmentDetailGoalsBinding? = null
    private val binding get() = fragmentDetailGoalsBinding!!

    private lateinit var goals: ArrayList<ToDo>

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var viewModel: ValueGoalsViewModel

    private var detailValueGoalsAdapter: DetailValueGoalsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            value = it.getString(VALUE)
            statement = it.getString(STATEMENT)
            desc = it.getString(DESC)
            img = it.getString(IMG)
            datetime = it.getString(DATETIME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailGoalsBinding = FragmentDetailGoalsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailGoalsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, value: String, statement: String, desc: String, img: String, datetime: String) =
            DetailGoalsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(VALUE, value)
                    putString(STATEMENT, statement)
                    putString(DESC, desc)
                    putString(IMG, img)
                    putString(DATETIME, datetime)
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: memanggil viewmodel
        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(
            id!!
        )

        binding.tvValue.text = value
        binding.tvDatetime.text = datetime
        binding.tvDesc.text = desc
        binding.tvStatement.text = statement

        if (img == ""){
            binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
        }else{
            Picasso.get().load(img).into(binding.ivIconValue)
        }

        binding.backMain.setOnClickListener {
            startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }

        binding.rvGoals.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvGoals.layoutManager = linearLayoutManager
        detailValueGoalsAdapter = DetailValueGoalsAdapter(id)
        binding.rvGoals.adapter = detailValueGoalsAdapter

        viewModel.allToDo(id!!)
        viewModel.todoMutableLiveData.observe(requireActivity()){ toDo ->
            detailValueGoalsAdapter!!.setTodo(toDo)
            detailValueGoalsAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorToDo.observe(requireActivity()
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        fragmentDetailGoalsBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailGoalsBinding = null
    }


}