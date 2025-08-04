package com.gopalpoddar4.glasscount.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.databinding.FragmentIntroBinding
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.NavUtil


class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIntroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.background)

        val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)


        binding.next.startAnimation(slideUp)
        binding.dot.startAnimation(fadeIn)
        binding.txt2.startAnimation(fadeIn)
        binding.textView.startAnimation(fadeIn)
        binding.imageView.startAnimation(fadeIn)
        binding.heading.startAnimation(fadeIn)

        binding.next.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_askNameFragment)
        }

        NavUtil.applyBottomSystemMargin(binding.mainLayout)
    }
}