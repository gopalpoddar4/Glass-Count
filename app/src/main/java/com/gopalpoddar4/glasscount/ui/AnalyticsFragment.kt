package com.gopalpoddar4.glasscount.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.gopalpoddar4.glasscount.AppClass
import com.gopalpoddar4.glasscount.R
import com.gopalpoddar4.glasscount.data.DailyDrinkModel
import com.gopalpoddar4.glasscount.databinding.FragmentAnalyticsBinding
import com.gopalpoddar4.glasscount.databinding.FragmentProfileBinding
import com.gopalpoddar4.glasscount.utils.NavUtil
import com.gopalpoddar4.glasscount.vmandrepo.MainViewModel
import com.gopalpoddar4.glasscount.vmandrepo.VMFactory


class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding?=null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel
    lateinit var sharedPrefrence: SharedPreferences
    var streak: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnalyticsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefrence = requireActivity().getSharedPreferences("glasscount",MODE_PRIVATE)
        streak = sharedPrefrence.getInt("streak",0)

        NavUtil.applyBottomSystemMargin(binding.scroolBar)
        val repository = (requireActivity().application as AppClass).repository
        viewModel = ViewModelProvider(this, VMFactory(repository))[MainViewModel::class.java]

        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.card_surface)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment,false)

                }
            })

        binding.streak.text = "$streak DAY"
        binding.streakAchive.text = "You're on a $streak-day hydration streak!"

        viewModel.dailyDrinkData().observe (viewLifecycleOwner){list->

            val last7DaysData = list.take(7)
            if (!last7DaysData.isEmpty() && last7DaysData.size >= 4){
                setUpChart(last7DaysData)
            }

            val totalGlassCount = last7DaysData.sumOf { it.dailyDrink }
            binding.weeklyAchiveTxt.text = "You drank $totalGlassCount glasses in last ${last7DaysData.size} days"
        }

    }

    private fun setUpChart(data: List<DailyDrinkModel>){
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        data.forEachIndexed { index, item ->
            entries.add(Entry(index.toFloat(),item.dailyDrink.toFloat()))
            labels.add(item.date)
        }

        val dataset = LineDataSet(entries,"Water Intake (Glasses)").apply {
            color = ContextCompat.getColor(requireContext(),R.color.accent)
            valueTextColor = ContextCompat.getColor(requireContext(),R.color.text_primary)
            valueTextSize = 12f

            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(),R.drawable.graph_bg)

            setDrawCircles(true)
            circleRadius = 5f
            circleHoleRadius = 2.5f
            setCircleColor(ContextCompat.getColor(requireContext(),R.color.text_primary))
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawValues(true)
            valueFormatter = DefaultValueFormatter(0)

        }

        val lineData = LineData(dataset)
        binding.chart.data = lineData

        val xAxis = binding.chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -0f // rotate if labels overlap

        // Y-Axis setup
        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
            textSize = 12f
        }

        binding.chart.axisRight.isEnabled = false

        // Legend
        binding.chart.legend.apply {
            textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
            textSize = 14f
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            yOffset = 20f
        }

        // Chart config
        binding.chart.description.isEnabled = false
        binding.chart.setTouchEnabled(false)
        binding.chart.setPinchZoom(false)
        binding.chart.animateY(1000)
        binding.chart.invalidate()
    }

}