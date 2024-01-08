package it.dsequino.apitest.utils

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer
import it.dsequino.apitest.R

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.calendarDayText)
}