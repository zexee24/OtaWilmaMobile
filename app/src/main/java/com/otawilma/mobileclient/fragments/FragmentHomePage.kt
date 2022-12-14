package com.otawilma.mobileclient.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otawilma.mobileclient.*
import com.otawilma.mobileclient.dataClasses.SchoolDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FragmentHomePage:Fragment(R.layout.fragment_home_page), OtawilmaNetworking {

    private lateinit var timeTableDayAdapter: TimeTableDayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeTableDayAdapter = TimeTableDayAdapter()


        CoroutineScope(Dispatchers.IO).launch {


            val today = LocalDate.now()
            val schoolDayMutableList : MutableList<SchoolDay> = mutableListOf()

            for (i in 0 until sharedPreferences.homePageDays) {
                val dayToTake = today.plusDays(i.toLong())
                schoolDayMutableList.add(dayRepository.getScheduleOfADay(dayToTake))
            }
            CoroutineScope(Dispatchers.Main).launch {
                timeTableDayAdapter.submitItems(schoolDayMutableList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManagerBetter = DayRecyclerLinearLayoutManager(context)
        layoutManagerBetter.orientation = LinearLayoutManager.HORIZONTAL
        view.findViewById<RecyclerView>(R.id.recyclerViewHomeSchedule)?.apply {

            // THIS IS BULLSHIT FUCK YOU DROID
            layoutManager = layoutManagerBetter
            isNestedScrollingEnabled = false
            adapter = timeTableDayAdapter

        }

    }

    class DayRecyclerLinearLayoutManager(context: Context?) : LinearLayoutManager(context){
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {


            /* Useless for now
            if (lp != null) {
                lp.width = width/sharedPreferences.homePageDays
            }
             */

            return super.checkLayoutParams(lp)
        }
    }
}
