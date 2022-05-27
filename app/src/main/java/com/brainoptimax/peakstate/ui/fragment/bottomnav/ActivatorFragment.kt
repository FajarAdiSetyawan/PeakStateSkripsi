package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.ActivatorAdapter
import com.brainoptimax.peakstate.databinding.FragmentActivatorBinding
import com.brainoptimax.peakstate.model.activator.Activator
import com.brainoptimax.peakstate.model.activator.DetailActivator
import com.brainoptimax.peakstate.model.activator.ExpandActivator
import com.brainoptimax.peakstate.model.activator.RowModel

class ActivatorFragment : Fragment() {
    private var fragmentActivatorBinding: FragmentActivatorBinding? = null
    private val binding get() = fragmentActivatorBinding!!

    lateinit var rows : MutableList<RowModel>
    lateinit var activatorAdapter: ActivatorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentActivatorBinding = FragmentActivatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rows = mutableListOf()
        activatorAdapter = ActivatorAdapter(requireActivity(), rows)
        binding.rvMainActivator.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        binding.rvMainActivator.adapter = activatorAdapter

        dataActivator()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dataActivator(){

        val expandActivatorList1 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList1.add(ExpandActivator(resources.getString(R.string.physical_calm_alert_state), resources.getString(R.string.e_g_sports_discussions_giving_presentations_studying), null))
        expandActivatorList1.add(ExpandActivator(resources.getString(R.string.relax_the_mind_for_creative_pursuits), resources.getString(R.string.e_g_blogging_composing_drawing_brainstorming_ideas), null))

        val expandActivatorList2 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList2.add(ExpandActivator(resources.getString(R.string.executive_functions), resources.getString(R.string.attend_meetings_problem_solving_decision_making_with_time_constraints), null))
        expandActivatorList2.add(ExpandActivator(resources.getString(R.string.group_work), resources.getString(R.string.attending_group_discussion_lectures_seminars_talk), null))
        expandActivatorList2.add(ExpandActivator(resources.getString(R.string.high_intensity_activity), resources.getString(R.string.high_intensity_sport_e_g_wresting_boxing_martial_arts_other_competitive_situations_that_require_increased_alertness_and_quick_reflexes), null))

        val expandActivatorList3 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList3.add(ExpandActivator(resources.getString(R.string.creative_task), resources.getString(R.string.blogging_composing_drawing_brainstorming_ideas), null))
        expandActivatorList3.add(ExpandActivator(resources.getString(R.string.study), resources.getString(R.string.studying_attending_talks_lectures_discussions), null))
        expandActivatorList3.add(ExpandActivator(resources.getString(R.string.practice), resources.getString(R.string.archery_golfing_target_practice_massage_reflexiology), null))

        val expandActivatorList4 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList4.add(ExpandActivator(resources.getString(R.string.creative_pursuits), resources.getString(R.string.creative_visualization_exercises), null))
        expandActivatorList4.add(ExpandActivator(resources.getString(R.string.skill_improvement_visualization), resources.getString(R.string.for_visualizing_elaborate_processes_with_multi_steps_e_g_stage_performers_athletes_golfer_skiers_etc_surgeons_etc), null))

        val expandActivatorList5 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList5.add(ExpandActivator(resources.getString(R.string.meditation), resources.getString(R.string.visualization_meditation), null))
        expandActivatorList5.add(ExpandActivator(resources.getString(R.string.quiet_contemplation), resources.getString(R.string.to_quieten_the_mind_for_prayer_reflection), null))
        expandActivatorList5.add(ExpandActivator(resources.getString(R.string.studying), resources.getString(R.string.studying_self_learning_creative_problem_solving), null))
        expandActivatorList5.add(ExpandActivator(resources.getString(R.string.calm_relaxation), resources.getString(R.string.calming_transition_into_a_relaxed_state), null))

        val expandActivatorList6 : MutableList<ExpandActivator> = mutableListOf()
        expandActivatorList6.add(ExpandActivator(resources.getString(R.string.sleep_meditate), resources.getString(R.string.induce_sleep), null))
        expandActivatorList6.add(ExpandActivator(resources.getString(R.string.calm), resources.getString(R.string.meditation_and_sleep), null))
        expandActivatorList6.add(ExpandActivator(resources.getString(R.string.mental_quiescence), resources.getString(R.string.to_quieten_a_chattery_mind_visualization_and_meditation), null))


        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.feel_good), R.drawable.ic_feel_good,expandActivatorList1)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.performance_boost), R.drawable.ic_performance_boost,expandActivatorList2)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.brain_boost_focus), R.drawable.ic_brain_foc,expandActivatorList3)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.brain_boost_visualization), R.drawable.ic_brain_vis,expandActivatorList4)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.relax_and_creative_pursuits), R.drawable.ic_relax_cre,expandActivatorList5)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.sleep_and_meditation), R.drawable.ic_sleep_medi,expandActivatorList6)))

        activatorAdapter.notifyDataSetChanged()
    }
}