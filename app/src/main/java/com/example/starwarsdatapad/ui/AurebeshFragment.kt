package com.example.starwarsdatapad.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.starwarsdatapad.databinding.FragmentAurebeshBinding


class AurebeshFragment : Fragment() {

    private var _binding: FragmentAurebeshBinding? = null
    private val binding get() = _binding!!
    private var text: String = ""
    /*private lateinit var userData: UserData

    private val viewModel: DataPadViewModel by activityViewModels {
        DataPadViewModelFactory(
            (activity?.application as BaseApplication).database.DataPadDao()
        )
    }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAurebeshBinding.inflate(inflater, container, false)
        /*viewModel.userData.observe(viewLifecycleOwner) { value ->
            userData = value
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_aurebeshFragment_to_homeFragment)
        }*/

        binding.apply {
            aBtn.setOnClickListener {addLetter("a")}
            bBtn.setOnClickListener {addLetter("b")}
            cBtn.setOnClickListener {addLetter("c")}
            dBtn.setOnClickListener {addLetter("d")}
            eBtn.setOnClickListener {addLetter("e")}
            fBtn.setOnClickListener {addLetter("f")}
            gBtn.setOnClickListener {addLetter("g")}
            hBtn.setOnClickListener {addLetter("h")}
            iBtn.setOnClickListener {addLetter("i")}
            jBtn.setOnClickListener {addLetter("g")}
            kBtn.setOnClickListener {addLetter("k")}
            lBtn.setOnClickListener {addLetter("l")}
            mBtn.setOnClickListener {addLetter("n")}
            nBtn.setOnClickListener {addLetter("m")}
            oBtn.setOnClickListener {addLetter("o")}
            pBtn.setOnClickListener {addLetter("p")}
            qBtn.setOnClickListener {addLetter("q")}
            rBtn.setOnClickListener {addLetter("r")}
            sBtn.setOnClickListener {addLetter("s")}
            tBtn.setOnClickListener {addLetter("t")}
            uBtn.setOnClickListener {addLetter("u")}
            vBtn.setOnClickListener {addLetter("v")}
            wBtn.setOnClickListener {addLetter("w")}
            xBtn.setOnClickListener {addLetter("x")}
            yBtn.setOnClickListener {addLetter("y")}
            zBtn.setOnClickListener {addLetter("z")}
            spaceBtn.setOnClickListener{addLetter(" ")}
        }

        binding.backspaceBtn.setOnClickListener {
            text = text.dropLast(1)
            storeText()
        }

        binding.clearBtn.setOnClickListener {
            text = ""
            storeText()
        }
        /*binding.credits.text = userData.credits.toString()
        binding.score.text = userData.score.toString()*/

        return binding.root
    }

    private fun addLetter(letter: String) {
        text += letter
        storeText()
    }

    private fun storeText() {
        binding.aurebeshText.text = text
    }

}