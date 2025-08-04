package com.gopalpoddar4.glasscount.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gopalpoddar4.glasscount.AlarmReceiver
import com.gopalpoddar4.glasscount.AppClass
import com.gopalpoddar4.glasscount.NewAppWidget
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.data.RecentDrinkAdapter
import com.gopalpoddar4.glasscount.data.RecentDrinkModel
import com.gopalpoddar4.glasscount.databinding.FragmentDashboardBinding
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.GlassPref
import com.gopalpoddar4.glasscount.utils.NavUtil
import com.gopalpoddar4.glasscount.utils.WidgetHelper
import com.gopalpoddar4.glasscount.vmandrepo.MainViewModel
import com.gopalpoddar4.glasscount.vmandrepo.VMFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding?=null
    private val binding get() = _binding!!
    lateinit var sharedPrefrence: SharedPreferences
    var dailyWater: Int = 0
    var persent: Int = 0
    var goal: Int=0
    lateinit var name: String
    lateinit var firstName: String
    lateinit var recentDrinkAdapter: RecentDrinkAdapter
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDailyAlarm(requireContext())

        val repository = (requireActivity().application as AppClass).repository
        viewModel = ViewModelProvider(this, VMFactory(repository))[MainViewModel::class.java]

        sharedPrefrence = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)
        dailyWater = GlassPref.getCount(requireContext())
        goal = GlassPref.getGoal(requireContext())
        name = sharedPrefrence.getString("name","User").toString()
        sharedPrefrence.edit().putLong("last_date", System.currentTimeMillis()).apply()
        firstName = name.split(" ")[0]

        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.card_surface)

        greating()

        initUi()

        // today's history
        viewModel.getRecentData().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.noDataAvailable.visibility = View.VISIBLE
                binding.last5Rcv.visibility = View.GONE
            } else {
                binding.noDataAvailable.visibility = View.GONE
                binding.last5Rcv.visibility = View.VISIBLE
                recentDrinkAdapter = RecentDrinkAdapter(it)
                binding.last5Rcv.adapter = recentDrinkAdapter
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            })

        binding.cardProgress.setOnClickListener {
           countGlass()
        }
    }

    private fun greating(){
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greating = when(hour){
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }
        binding.goodMorning.text = "$greating"
        binding.userName.text = name
    }

    private fun initUi(){
        binding.todayGoal.text = "Drink $goal Glasses of Water"
        persent = ((dailyWater.toFloat() / goal) * 100).toInt()
        binding.circularProgressBar.apply {
            setProgressWithAnimation(persent.toFloat(),1000)
        }
        binding.persentProgress.text = "$persent%"
        if (goal==dailyWater){

            binding.remainingGlass.text = "Goal Achieved! \uD83C\uDF89\n"
            binding.confirmationTxt.text = "Great Job, $firstName! You've completed your goal for today!"
            binding.arrow.visibility = View.GONE

        }else{
            binding.remainingGlass.text = "${goal-dailyWater} Glass more"
            binding.arrow.visibility = View.VISIBLE
        }

        binding.last5Rcv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun countGlass(){

        val currentTime = WidgetHelper.recentTime()


        if (goal==dailyWater){
            binding.arrow.visibility = View.GONE
            binding.remainingGlass.text = "Goal Achieved! \uD83C\uDF89\n"
            binding.confirmationTxt.text = "Great Job, $firstName! You've completed your goal for today!"

        }else if (goal-1 == dailyWater){
            dailyWater++

            binding.lottieAnimationView.visibility = View.VISIBLE
            sharedPrefrence.edit().putInt("dailyWater",dailyWater).apply()

            persent = ((dailyWater.toFloat() / goal) * 100).toInt()
            binding.circularProgressBar.apply {
                setProgressWithAnimation(persent.toFloat(),1000)
            }
            binding.persentProgress.text = "$persent%"
            binding.remainingGlass.text = "Goal Achieved! \uD83C\uDF89\n"
            binding.confirmationTxt.text = "Great Job, $firstName! You've completed your goal for today!"
            binding.arrow.visibility = View.GONE

            viewModel.insertRecentData(currentTime)

        }else{
            dailyWater++

            viewModel.insertRecentData(currentTime)
            sharedPrefrence.edit().putInt("dailyWater",dailyWater).apply()

            persent = ((dailyWater.toFloat() / goal) * 100).toInt()
            binding.circularProgressBar.apply {
                setProgressWithAnimation(persent.toFloat(),1000)
            }
            binding.arrow.visibility = View.VISIBLE
            binding.persentProgress.text = "$persent%"
            binding.remainingGlass.text = "${goal-dailyWater} Glass more"
        }

        context?.let {
            AppWidgetManager.getInstance(context)
                .updateAppWidget(ComponentName(it, NewAppWidget::class.java), RemoteViews(context?.packageName, R.layout.new_app_widget))
        }
    }

    fun setDailyAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set time to 11:59 PM
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 57)
            set(Calendar.SECOND, 0)
        }

        // If time is already passed today, set for tomorrow
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}