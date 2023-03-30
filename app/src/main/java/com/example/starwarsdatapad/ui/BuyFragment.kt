package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.databinding.FragmentBuyBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class BuyFragment : Fragment() {

    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var availableCargoSpace: String = ""
    private var usedCargoSpace: String = ""
    private var totalCargoSpace: String = ""

    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)

        viewModel.totalCargoSpace.observe(viewLifecycleOwner) {value ->
            totalCargoSpace = value
            updateCargoSpace()
        }


        viewModel.usedCargoSpace.observe(viewLifecycleOwner) { value ->
            usedCargoSpace = value
            updateCargoSpace()
        }

        viewModel.availableCargoSpace.observe(viewLifecycleOwner) { value ->
            availableCargoSpace = value
            binding.seekBar.max = availableCargoSpace.toInt()
            binding.seekBar.progress = binding.seekBar.max
            updateCargoSpace()
        }

        viewModel.purchaseItem.observe(viewLifecycleOwner) {value ->
            if (binding.idInput.text.toString() != "") {
                binding.inventoryItem.text = value
            } else {
                binding.inventoryItem.text = ""
            }
        }

        val seek = binding.seekBar
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                binding.qtyToBuy.text = binding.seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                binding.qtyToBuy.text = binding.seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                binding.qtyToBuy.text = binding.seekBar.progress.toString()
            }
        })

        binding.searchBtn.setOnClickListener {
            findItem()
        }

        binding.buyBtn.setOnClickListener {
            buyItem()
        }

        binding.inventoryItem.text = ""

        return binding.root
    }

    private fun updateCargoSpace() {
        uiScope.launch(Dispatchers.IO) {
            if (binding.idInput.text.toString() != "") {
                viewModel.updateCargoSpace(binding.idInput.text.toString().toInt())
            } else {
                viewModel.updateCargoSpace(0)
            }
        }
    }

    private fun buyItem() {
        if (binding.idInput.text.toString() != "") {
            uiScope.launch(Dispatchers.IO) {
                viewModel.buyCargo(
                    binding.idInput.text.toString().toInt(),
                    binding.seekBar.progress
                )
            }
        }
    }

    private fun findItem() {
        uiScope.launch(Dispatchers.IO) {
            viewModel.searchInventory(
                binding.idInput.text.toString().toInt())
        }
        binding.seekBar.progress = binding.seekBar.max
    }
}