package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.R
import com.example.starwarsdatapad.data.UserData
import com.example.starwarsdatapad.databinding.FragmentQuestBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class QuestFragment : Fragment() {

    private var _binding: FragmentQuestBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserData
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuestBinding.inflate(inflater, container, false)
        viewModel.userData.observe(viewLifecycleOwner) { value ->
            userData = value
            binding.credits.text = getString(R.string.credits, userData.credits.toString())
            binding.score.text = getString(R.string.score, userData.score.toString())
        }

        viewModel.questText.observe(viewLifecycleOwner) { value ->
            binding.questText.text = value
        }

        viewModel.questLines.observe(viewLifecycleOwner) { value ->
            binding.questTextView.text = value
        }

        binding.searchBtn.setOnClickListener {
            searchQuest()
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_questFragment_to_homeFragment)
        }

        binding.questText.movementMethod = ScrollingMovementMethod()
        binding.questTextView.movementMethod = ScrollingMovementMethod()


        return binding.root
    }


    private fun searchQuest() {
        if (binding.questIdInput.text.toString() != "") {
            uiScope.launch(Dispatchers.IO) {
                viewModel.searchQuest(binding.questIdInput.text.toString())
            }
        }
    }

}