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
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.databinding.FragmentAskNameBinding
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.NavUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AskNameFragment : Fragment() {

    private var _binding: FragmentAskNameBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAskNameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefrence = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)

        binding.next.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            if (name.isNotEmpty()){
                sharedPrefrence.edit().putString("name",name).apply()
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    findNavController().navigate(R.id.action_askNameFragment_to_askGoalFragment)
                }
            }else{
                Toast.makeText(requireContext(),"Enter name", Toast.LENGTH_SHORT).show()
            }
        }
        val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)


        binding.next.startAnimation(slideUp)
        binding.dot.startAnimation(fadeIn)
        binding.imageView.startAnimation(fadeIn)
        binding.textView.startAnimation(fadeIn)
        binding.nameEditText.startAnimation(fadeIn)
        binding.heading.startAnimation(fadeIn)


        NavUtil.applyBottomSystemMargin(binding.mainLayout)
    }

}