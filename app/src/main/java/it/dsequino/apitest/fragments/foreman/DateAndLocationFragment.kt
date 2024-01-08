package it.dsequino.apitest.fragments.foreman

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import it.dsequino.apitest.R
import it.dsequino.apitest.databinding.FragmentDateAndLocationBinding
import it.dsequino.apitest.utils.DayViewContainer
import it.dsequino.apitest.utils.MonthViewContainer
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class DateAndLocationFragment : Fragment() {
    private lateinit var binding: FragmentDateAndLocationBinding
    private lateinit var currentMonth: YearMonth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateAndLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentMonth = YearMonth.now()
        setupCalendar(currentMonth)

        binding.btnPreviousMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            setupCalendar(currentMonth)
            binding.calendarView.scrollToMonth(currentMonth)
            updateMonthName(currentMonth)
        }

        binding.btnNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            setupCalendar(currentMonth)
            binding.calendarView.scrollToMonth(currentMonth)
            updateMonthName(currentMonth)
        }
    }

    private fun setupCalendar(currentMonth: YearMonth) {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        binding.calendarView.monthScrollListener = { calendarMonth ->
            this.currentMonth = calendarMonth.yearMonth
            updateMonthName(calendarMonth.yearMonth)
        }

        val titlesContainer = requireActivity().findViewById<ViewGroup>(R.id.titlesContainer)
        titlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek()[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

        val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        binding.monthName.text = monthName
        updateMonthName(currentMonth)
    }

    private fun updateMonthName(yearMonth: YearMonth) {
        val monthName = yearMonth.month.getDisplayName(
            TextStyle.FULL, Locale.ITALIAN
        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        binding.monthName.text = monthName
    }
}