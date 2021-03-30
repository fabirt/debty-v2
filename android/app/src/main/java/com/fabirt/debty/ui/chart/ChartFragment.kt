package com.fabirt.debty.ui.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fabirt.debty.R
import com.fabirt.debty.databinding.FragmentChartBinding
import com.fabirt.debty.ui.common.showUnexpectedFailureSnackBar
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChartViewModel by viewModels()

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
        val lineColor = requireContext().getColor(R.color.colorPositive)
        val highlightColor = requireContext().getColor(R.color.colorPrimary)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)

        binding.chart.apply {
            setNoDataText(getString(R.string.empty_summary))
            setNoDataTextColor(requireContext().getColor(R.color.low_emphasis_color))
            setNoDataTextTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.montserrat_semi_bold
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movements
                .catch { showUnexpectedFailureSnackBar() }
                .collect { movements ->
                    if (movements.isEmpty()) return@collect

                    val entries = movements.mapIndexed { index, value ->
                        Entry(index.toFloat(), value.amount.toFloat())
                    }

                    val dataSet = LineDataSet(entries, getString(R.string.chart_label)).apply {
                        color = lineColor
                        valueTextColor = colorOnBackground
                        valueTypeface = typeface
                        setCircleColor(lineColor)
                        circleHoleColor = lineColor
                        this.highLightColor = highlightColor
                        fillDrawable =
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.chart_fill_gradient,
                                null
                            )
                        this.setDrawFilled(true)
                    }

                    val lineData = LineData(dataSet)

                    binding.chart.apply {
                        data = lineData
                        axisLeft.textColor = colorOnBackground
                        axisLeft.typeface = typeface
                        axisRight.typeface = typeface
                        xAxis.typeface = typeface
                        axisRight.textColor = colorOnBackground
                        xAxis.textColor = colorOnBackground
                        description.textColor = colorOnBackground
                        legend.textColor = colorOnBackground
                        legend.typeface = typeface
                        description.text = ""
                        xAxis.granularity = 1f
                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                                val count = movements.count()
                                val index = value.toInt()

                                if (index < 0) return ""

                                return movements.getOrNull(index)?.let { movement ->
                                    val date = Date(movement.date)
                                    val sf = SimpleDateFormat("MMM dd", Locale.getDefault())

                                    if (index == 0) {
                                        return@let sf.format(date)
                                    }

                                    if (index == movements.count() - 1) {
                                        return@let sf.format(date)
                                    }

                                    if (count > 2 && index == count / 2) {
                                        return@let sf.format(date)
                                    }

                                    return@let ""
                                } ?: ""
                            }
                        }

                        invalidate()
                        // animateX(1000)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}