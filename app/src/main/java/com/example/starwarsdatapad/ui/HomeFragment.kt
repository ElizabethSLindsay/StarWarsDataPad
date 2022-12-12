package com.example.starwarsdatapad.ui


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.starwarsdatapad.BaseApplication
import com.example.starwarsdatapad.R
import com.example.starwarsdatapad.data.UserData
import com.example.starwarsdatapad.databinding.FragmentHomeBinding
import com.example.starwarsdatapad.viewmodel.DataPadViewModel
import com.example.starwarsdatapad.viewmodel.DataPadViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserData

    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.userData.observe(viewLifecycleOwner) { value ->
            userData = value
            binding.credits.text = getString(R.string.credits, userData.credits.toString())
            binding.score.text = getString(R.string.score, userData.score.toString())
        }

        binding.questBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questFragment)
        }

        binding.tradeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_tradeFragment)
        }

        binding.upgradeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_upgradeFragment)
        }

        binding.translateBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aurebeshFragment)
        }

        return binding.root
    }

}