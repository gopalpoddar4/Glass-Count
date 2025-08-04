package com.gopalpoddar4.glasscount.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.marginTop
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.databinding.FragmentAchivementBinding
import com.gopalpoddar4.glasscount.databinding.FragmentSplashBinding
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.utils.NavUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AchivementFragment : Fragment() {

    private var _binding: FragmentAchivementBinding?=null
    private val binding get() = _binding!!
    lateinit var sharedPrefrence: SharedPreferences
    lateinit var name: String
    lateinit var firstName: String
    var streak: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAchivementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavUtil.applyBottomSystemMargin(binding.ll3)

        sharedPrefrence = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)

        name = sharedPrefrence.getString("name","User").toString()
        streak = GlassPref.getStreak(requireContext())
        firstName = name.split(" ")[0]


        val slideUp = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)
        val fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in)

        binding.streak.text = "$streak DAY"
        binding.streakAchive.text = "Great! You're on a $streak-day hydration streak!"

        binding.letsGo.text = "Let's Go, $firstName!"

        binding.ll1.startAnimation(fadeIn)
        binding.ll2.startAnimation(fadeIn)
        binding.next.startAnimation(slideUp)

        binding.next.setOnClickListener {
            findNavController().navigate(
                R.id.action_achivementFragment_to_dashboardFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.achivementFragment, true).build()
            )
        }

    }

}