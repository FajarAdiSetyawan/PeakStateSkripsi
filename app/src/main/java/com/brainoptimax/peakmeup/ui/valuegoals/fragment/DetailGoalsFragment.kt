package com.brainoptimax.peakmeup.ui.valuegoals.fragment

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
import com.brainoptimax.peakmeup.adapter.valuegoals.DetailValueGoalsAdapter
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentDetailGoalsBinding
import com.brainoptimax.peakmeup.utils.Preferences
import com.squareup.picasso.Picasso


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
    private var idGoals: String? = null
    private var value: String? = null
    private var statement: String? = null
    private var desc: String? = null
    private var img: String? = null
    private var datetime: String? = null

    private var fragmentDetailGoalsBinding: FragmentDetailGoalsBinding? = null
    private val binding get() = fragmentDetailGoalsBinding!!

    private lateinit var preference: Preferences

    private lateinit var viewModel: ValueGoalsViewModel

    private var detailValueGoalsAdapter: DetailValueGoalsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idGoals = it.getString(ID)
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

        // TODO: mengambil data preference
        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        // TODO: set text dari data yang dikirim
        binding.tvValue.text = value
        binding.tvDatetime.text = datetime
        binding.tvDesc.text = desc
        binding.tvStatement.text = statement

        // TODO: cek data image kosong
        if (img == ""){
            binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
        }else{
            // TODO: kalau tidak kosong
            Picasso.get().load(img).into(binding.ivIconValue)
        }

        // TODO: klik button back
        binding.backMain.setOnClickListener {
            startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }

        // TODO: mengambil semua data todo dan ditamilkan dengan menggunakan recyclerview
        binding.rvGoals.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvGoals.layoutManager = linearLayoutManager
        // TODO: memanggil adapter dan mengirimkan data idgoals ke adapter
        detailValueGoalsAdapter = DetailValueGoalsAdapter(idGoals)
        binding.rvGoals.adapter = detailValueGoalsAdapter

        // TODO: memanggil semua data todo dari firebase
        viewModel.allToDo(uidUser!!, idGoals!!)
        // TODO: cek data
        viewModel.todoMutableLiveData.observe(viewLifecycleOwner){ toDo ->
            detailValueGoalsAdapter!!.setTodo(toDo)
            detailValueGoalsAdapter!!.notifyDataSetChanged()
        }
        // TODO: kalau terjadi error
        viewModel.databaseErrorToDo.observe(viewLifecycleOwner
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        // TODO: memanggil fungsi dari adapter
        detailValueGoalsAdapter!!.setOnItemDoneClickListener { todo ->
            viewModel.doneTodo(uidUser, idGoals!!, todo.idTodo!!)
            viewModel.doneTodoMutableLiveData.observe(requireActivity()) { status ->
                if (status.equals("success")){
                    Toast.makeText(requireActivity(), "Done ${todo.todo}", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.databaseErrorDoneTodo.observe(viewLifecycleOwner
            ) { error ->
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        detailValueGoalsAdapter!!.setOnItemNotDoneClickListener { toDo ->
            viewModel.notDoneTodo(uidUser, idGoals!!, toDo.idTodo!!)
            viewModel.notDoneTodoMutableLiveData.observe(viewLifecycleOwner) { status ->
                if (status.equals("success")){
                    Toast.makeText(requireActivity(), "Not Done ${toDo.todo}", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.databaseErrorNotDoneTodo.observe(viewLifecycleOwner
            ) { error ->
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }
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