package com.gopalpoddar4.glasscount.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gopalpoddar4.glasscount.AppClass
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.utils.NavUtil
import com.gopalpoddar4.glasscount.utils.ResetGlassCount
import com.gopalpoddar4.glasscount.vmandrepo.MainViewModel
import com.gopalpoddar4.glasscount.vmandrepo.VMFactory


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       sharedPreferences = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)

        val repository = (requireActivity().application as AppClass).repository
        viewModel = ViewModelProvider(this, VMFactory(repository))[MainViewModel::class.java]

        //NavUtil.applyBottomSystemMargin(binding.profileLayout1)
        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.card_surface)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment,false)
                }
            })


        val goal = GlassPref.getGoal(requireContext())
        val name = sharedPreferences.getString("name","User")
        binding.userName.text = name
        binding.glass.text = "$goal Glass / Day"

        streak()

        binding.clearTodayData.setOnClickListener {
            ResetGlassCount.resetData(requireContext())
            Toast.makeText(requireContext(), "Today's data cleared", Toast.LENGTH_SHORT).show()
        }
        binding.deleteAllData.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            Toast.makeText(requireContext(), "All Data Deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileFragment_to_splashFragment,null,NavOptions.Builder().setPopUpTo(R.id.splashFragment,false).build())
        }

        binding.editBtn.setOnClickListener {

        }

    }
    private fun streak(){
        var streak = GlassPref.getStreak(requireContext())
        binding.streak.text = "$streak DAY"

        if (streak>=3){
            binding.streakAchive.visibility = View.VISIBLE
            binding.streakAchive.text = "Great! You're on a $streak-day hydration streak!"
        }
    }
}