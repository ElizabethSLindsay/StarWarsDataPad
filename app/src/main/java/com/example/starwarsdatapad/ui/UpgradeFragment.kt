package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.R
import com.example.starwarsdatapad.data.Ships
import com.example.starwarsdatapad.data.Upgrades
import com.example.starwarsdatapad.data.UserData
import com.example.starwarsdatapad.databinding.FragmentUpgradeBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class UpgradeFragment : Fragment() {

    private var _binding: FragmentUpgradeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userData: UserData
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var upgrades: List<Upgrades>
    private val shipList: List<String> = listOf(
        "Corellian Corvette", "Corellian Light Freighter", "Medium Transport")
    private lateinit var ship: Ships

    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpgradeBinding.inflate(inflater, container, false)
        viewModel.userData.observe(viewLifecycleOwner) { value ->
            userData = value
            binding.credits.text = getString(R.string.credits, userData.credits.toString())
            binding.score.text = getString(R.string.score, userData.score.toString())
        }

        /*viewModel.upgradeSearch.observe(viewLifecycleOwner) { value ->
            binding.upgradeItemSearch.text = value
        }

        uiScope.launch(Dispatchers.IO) {
            var upgradeString: String = ""
            ship = viewModel.getShip()
            upgradeString += "Current Ship: " + ship.name + "\n\n"
            upgrades = viewModel.getUpgrades()
            for (upgrade: Upgrades in upgrades) {
                if (upgrade.label !in shipList)
                upgradeString += upgrade.label + "\n"
            }
            binding.inventoryItem.text = upgradeString
        }*/

        /*binding.searchBtn.setOnClickListener {
            findUpgrade()
        }*/

        binding.buyUpgradeBtn.setOnClickListener {  }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_upgradeFragment_to_homeFragment)
        }

        return binding.root
    }

    /*private fun findUpgrade() {
        uiScope.launch(Dispatchers.IO) {
            viewModel.searchUpgrade(id)
        }
    }*/
}