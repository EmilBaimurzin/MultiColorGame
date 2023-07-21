package com.multi.game.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.multi.game.R
import com.multi.game.ui.other.ViewBindingFragment
import com.multi.game.databinding.FragmentMainBinding

class FragmentMain: ViewBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentGame)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentSettings)
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}