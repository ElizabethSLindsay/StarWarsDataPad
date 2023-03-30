package com.example.starwarsdatapad.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.databinding.FragmentSellBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SellFragment : Fragment() {

    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var sellValue: Int = 0


    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellBinding.inflate(inflater, container, false)


        viewModel.inventoryString.observe(viewLifecycleOwner) {value ->
            binding.inventoryItem.text = value
        }

        viewModel.itemTotal.observe(viewLifecycleOwner) { value ->
            if (value != "") {
                binding.seekBar.max = value.toInt()
                binding.seekBar.progress = binding.seekBar.max
            } else {
                binding.seekBar.max = 0
            }
        }

        viewModel.sellItem.observe(viewLifecycleOwner) {value ->
            if (binding.idInput.text.toString() != "") {
                binding.inventoryItemSearch.text = value
            } else {
                binding.inventoryItemSearch.text = ""
            }
        }

        viewModel.sellValue.observe(viewLifecycleOwner) { value ->
            if (value != "") {
                sellValue = value.toInt()
            } else {
                sellValue = 0
            }
            updateSellTotal()
        }

        val seek = binding.seekBar
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                binding.qtyToSell.text = seek.progress.toString()
                updateSellTotal()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                binding.qtyToSell.text = seek.progress.toString()
                updateSellTotal()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                binding.qtyToSell.text = seek.progress.toString()
                updateSellTotal()
            }
        })

        binding.searchBtn.setOnClickListener {
            findItem()
        }

        binding.sellBtn.setOnClickListener {
            sellItem()
        }
        updateInventory()
        binding.inventoryItem.movementMethod = ScrollingMovementMethod()


        return binding.root
    }

    private fun findItem() {
        uiScope.launch(Dispatchers.IO) {
            if (binding.idInput.text.toString() != "") {
                viewModel.searchSell(
                    binding.idInput.text.toString().toInt())
            }
        }
    }

    private fun sellItem() {
        uiScope.launch(Dispatchers.IO) {
            if (binding.idInput.text.toString() != "") {
                viewModel.sellItem(binding.idInput.text.toString().toInt(), binding.seekBar.progress)
            }
        }
    }

    private fun updateSellTotal() {
        binding.sellValue.text = ("Total Sell Value: " +
                binding.seekBar.progress * sellValue).toString()
    }

    private fun updateInventory() {
        uiScope.launch(Dispatchers.IO) {
            viewModel.updateInventory()
        }
    }

}