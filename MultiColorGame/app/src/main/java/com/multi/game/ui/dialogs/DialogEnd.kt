package com.multi.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.multi.game.R
import com.multi.game.core.library.ViewBindingDialog
import com.multi.game.databinding.DialogEndBinding

class DialogEnd: ViewBindingDialog<DialogEndBinding>(DialogEndBinding::inflate) {
    private val args: DialogEndArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
                true
            } else {
                false
            }
        }

        if (args.isWin) {
            binding.container.setBackgroundResource(R.drawable.bg_winner)
        } else {
            binding.container.setBackgroundResource(R.drawable.bg_loss)
        }

        binding.homeButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }
        binding.restartButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentGame)
        }

        val minutes = (args.time % 3600) / 60
        val seconds = args.time % 60

        binding.time.text = String.format("%02d:%02d", minutes, seconds)
    }
}