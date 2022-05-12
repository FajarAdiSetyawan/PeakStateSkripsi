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
        activatorAdapter = ActivatorAdapter(activity!!, rows)
        binding.rvMainActivator.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)

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


        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.feel_good), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_feel_good.png?alt=media&token=9a861083-6775-4e12-a9c0-24f07d65a7e7",expandActivatorList1)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.performance_boost), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_performance_boost.png?alt=media&token=67e3c114-b357-47ab-a2f7-87b07613fe18",expandActivatorList2)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.brain_boost_focus), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_brain_foc.png?alt=media&token=ef586de5-01b7-4267-9d0f-4cf9c7ae1c78",expandActivatorList3)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.brain_boost_visualization), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_brain_vis.png?alt=media&token=b987ce09-686c-4b59-9e43-17d1c00421b7",expandActivatorList4)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.relax_and_creative_pursuits), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_relax_cre.png?alt=media&token=21a21070-df8c-4228-907d-f7a2ad772ba2",expandActivatorList5)))
        rows.add(RowModel(RowModel.ACTIVATOR, Activator(resources.getString(R.string.sleep_and_meditation), "https://firebasestorage.googleapis.com/v0/b/peakstate-be063.appspot.com/o/activator%2Fic_sleep_medi.png?alt=media&token=4dbe6b16-d30e-4db3-81f3-86b07ac6bd28",expandActivatorList6)))

        activatorAdapter.notifyDataSetChanged()
    }
}