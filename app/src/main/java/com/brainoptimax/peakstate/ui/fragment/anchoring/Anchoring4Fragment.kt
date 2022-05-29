package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAnchoring4Binding
import com.brainoptimax.peakstate.ui.fragment.valuegoals.bottomsheet.AddBottomSheetGoals
import com.brainoptimax.peakstate.viewmodel.anchoring.AnchoringViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class Anchoring4Fragment : Fragment() {

    private var fragmentAnchoring4Binding: FragmentAnchoring4Binding? = null
    private val binding get() = fragmentAnchoring4Binding!!

    private lateinit var viewModel: AnchoringViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring4Binding = FragmentAnchoring4Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid)
                .child("Anchoring").child("Result").push()
        val idAnchoring = databaseReference.key
        
        val mBundle: Bundle? = arguments
        val resourceful =  mBundle!!.getString("resourceful")
        val memory =  mBundle.getString("memory")

        binding.btnNext.setOnClickListener {
            val sdf = SimpleDateFormat("E, d MMMM yyyy - H:mm", Locale.getDefault())
            val currentDateAndTime = sdf.format(Date())
            
            val note = binding.etMemory.text.toString().trim()
            if (note.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.note_blank), Toast.LENGTH_SHORT).show()
            }else{
                viewModel.addAnchoring(databaseReference, view, idAnchoring!!, resourceful, memory, note, currentDateAndTime)
                viewModel.status.observe(viewLifecycleOwner) { status ->
                    status?.let {
                        //Reset status value at first to prevent multitriggering
                        //and to be available to trigger action again
                        viewModel.status.value = null

                        Toast.makeText(requireActivity(), "Success Save Data", Toast.LENGTH_SHORT).show()
                        
                        val fragment = Anchoring5Fragment() // replace your custom fragment class
                        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.replace(R.id.frameLayoutAnchoring, fragment)
                        fragmentTransaction.commit()
                    }
                }
            }
        }
    }
}