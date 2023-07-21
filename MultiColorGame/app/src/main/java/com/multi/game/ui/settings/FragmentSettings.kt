package com.multi.game.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.multi.game.R
import com.multi.game.databinding.FragmentSettingsBinding
import com.multi.game.domain.other.AppSharedPrefs
import com.multi.game.ui.other.MainActivity
import com.multi.game.ui.other.ViewBindingFragment

class FragmentSettings :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val sharedPrefs by lazy {
        AppSharedPrefs(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.musicSlider.value = sharedPrefs.getVolume().toFloat()
        binding.musicSlider.setCustomThumbDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.music_tick,
                null
            )!!
        )
        setButtons()

        binding.buttonOn.setOnClickListener {
            setVibroState(true)
        }

        binding.buttonOff.setOnClickListener {
            setVibroState(false)
        }

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.confirmButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.musicSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                sharedPrefs.setVolume(slider.value.toInt())
                (requireActivity() as MainActivity).saveSec()
                (requireActivity() as MainActivity).startMusic()
            }
        })
    }

    private fun setVibroState(value: Boolean) {
        sharedPrefs.setVibro(value)
        setButtons()
    }

    private fun setButtons() {
        binding.buttonOff.setImageResource(if (sharedPrefs.getVibro()) R.drawable.button_off_not_active else R.drawable.button_off_active)
        binding.buttonOn.setImageResource(if (sharedPrefs.getVibro()) R.drawable.button_on_active else R.drawable.button_on_not_active)
    }
}