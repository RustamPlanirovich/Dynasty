package com.nauk0a.dynasty.budget.historyAndPlaning

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nauk0a.dynasty.budget.historyAndPlaning.currentPurchases.CurrentPurchasesFragment
import com.nauk0a.dynasty.budget.historyAndPlaning.planingForTheFuture.FuturePlaningFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private lateinit var fragment: Fragment
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> fragment = CurrentPurchasesFragment()
            1 -> fragment = FuturePlaningFragment()
            2 -> fragment = HistoryFragment()
        }

//        val fragment = HistoryFragment()
//        fragment.arguments = Bundle().apply {
//            putInt("ARG_OBJECT", position + 1)
//        }

        return fragment
    }
}