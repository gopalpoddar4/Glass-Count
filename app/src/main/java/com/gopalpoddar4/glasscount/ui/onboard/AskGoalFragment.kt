package com.gopalpoddar4.glasscount.ui.onboard

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.databinding.FragmentAskGoalBinding
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.NavUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AskGoalFragment : Fragment() {

    private var _binding: FragmentAskGoalBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAskGoalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("glasscount", MODE_PRIVATE)

        binding.getStarted.setOnClickListener {
            val goal = binding.goalEditText.text.toString()
            if (goal.isNotEmpty()) {
                val goal1 = goal.toInt()
                if (goal1<8){
                    Toast.makeText(requireContext(),"Please set 8 glass or more", Toast.LENGTH_SHORT).show()
                }else{
                    sharedPreferences.edit().putInt("goal", goal1).apply()
                    sharedPreferences.edit().putBoolean("onboard", true).apply()
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(300)
                        findNavController().navigate(R.id.action_askGoalFragment_to_dashboardFragment)
                    }
                }


            } else {
                Toast.makeText(requireContext(), "Enter daily goal", Toast.LENGTH_SHORT).show()
            }
        }

        val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)

        binding.getStarted.startAnimation(slideUp)
        binding.dot.startAnimation(fadeIn)
        binding.imageView.startAnimation(fadeIn)
        binding.goalEditText.startAnimation(fadeIn)
        binding.textView.startAnimation(fadeIn)
        binding.heading.startAnimation(fadeIn)

        NavUtil.applyBottomSystemMargin(binding.mainLayout)

    }

}