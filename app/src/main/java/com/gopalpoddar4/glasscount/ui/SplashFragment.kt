package com.gopalpoddar4.glasscount.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gopalpoddar4.glasscount.AppClass
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.databinding.FragmentSplashBinding
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.vmandrepo.MainViewModel
import com.gopalpoddar4.glasscount.vmandrepo.VMFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding?=null
    private val binding get() = _binding!!
    lateinit var sharedPrefrence: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefrence = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)
        val login = sharedPrefrence.getBoolean("onboard",false)
        val streak = GlassPref.getStreak(requireContext())

        //to hide system navigation and status bar
        requireActivity().window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)

            if(!login){
                findNavController().navigate(R.id.action_splashFragment_to_introFragment,null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment,true).build())
            }
            else if (shouldDataClear() && streak >= 3){
                findNavController().navigate(R.id.action_splashFragment_to_achivementFragment,null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment,true).build())
            }
            else if (login){
                findNavController().navigate(R.id.action_splashFragment_to_dashboardFragment,null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment,true).build())
            }
            else{
                Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //Visible system navigation bar and status bar
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    //this function check today or yesterday
    fun shouldDataClear(): Boolean{
        val lastSync = sharedPrefrence.getLong("last_date",0L)

        val lastDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(lastSync))
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        return lastDate!= currentDate
    }
}