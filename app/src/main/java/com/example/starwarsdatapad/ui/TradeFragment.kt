package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.starwarsdatapad.R
import com.example.starwarsdatapad.adapter.ViewPageAdapter
import com.example.starwarsdatapad.databinding.FragmentTradeBinding
import com.google.android.material.tabs.TabLayout


class TradeFragment : Fragment() {

    private var _binding: FragmentTradeBinding? = null
    private val binding get() = _binding!!
    /*private lateinit var userData: UserData

    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTradeBinding.inflate(inflater, container, false)
        /*viewModel.userData.observe(viewLifecycleOwner) { value ->
            userData = value
        }*/

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_tradeFragment_to_homeFragment)
        }
        /*binding.credits.text = userData.credits.toString()
        binding.score.text = userData.score.toString()*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Create View",view?.findViewById<ViewPager>(R.id.viewPager).toString())
        var pager = view?.findViewById<ViewPager>(R.id.viewPager)!!
        var tab = view?.findViewById<TabLayout>(R.id.tabLayout)!!

        val adapter = ViewPageAdapter(supportFragmentManager = childFragmentManager)

        adapter.addFragment(BuyFragment(), "Buy")
        adapter.addFragment(SellFragment(), "Sell")

        if (pager != null) {
            pager.adapter = adapter
        }

        if (tab != null) {
            tab.setupWithViewPager(pager)
        }
    }
}