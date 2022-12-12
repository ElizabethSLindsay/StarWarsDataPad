package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.R
import com.example.starwarsdatapad.adapter.QuestAdapter
import com.example.starwarsdatapad.data.UserData
import com.example.starwarsdatapad.databinding.FragmentQuestBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory


class QuestFragment : Fragment() {

    private var _binding: FragmentQuestBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserData

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

        binding.searchBtn.setOnClickListener {

        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_questFragment_to_homeFragment)
        }
        /*binding.credits.text = userData.credits.toString()
        binding.score.text = userData.score.toString()*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuestAdapter { dataPadEntity ->
            //TODO get qest based off of id and show the quest details when quest is clicked
        }
    }

}