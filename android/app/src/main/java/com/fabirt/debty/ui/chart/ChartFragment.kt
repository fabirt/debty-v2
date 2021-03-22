package com.fabirt.debty.ui.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentChartBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.pow

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorOnBackground = requireContext().getColor(R.color.colorOnBackground)
        val lineColor = requireContext().getColor(R.color.colorCustom1)

        val entries = mutableListOf<Entry>()
        for (i in 0..20) {
            val data = Entry(i.toFloat(), ((i * i) * (-1.0).pow(i.toDouble())).toFloat())
            entries.add(data)
        }

        val dataSet = LineDataSet(entries, getString(R.string.chart_label)).apply {
            color = lineColor
            valueTextColor = colorOnBackground
            setCircleColor(lineColor)
        }

        val lineData = LineData(dataSet)

        binding.chart.apply {
            data = lineData
            axisLeft.textColor = colorOnBackground
            axisRight.textColor = colorOnBackground
            xAxis.textColor = colorOnBackground
            description.textColor = colorOnBackground
            legend.textColor = colorOnBackground
            description.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}