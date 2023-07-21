package com.multi.game.ui.game

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multi.game.R
import com.multi.game.core.library.shortToast
import com.multi.game.databinding.FragmentGameBinding
import com.multi.game.domain.game.adapter.GameAdapter
import com.multi.game.domain.other.AppSharedPrefs
import com.multi.game.ui.other.ViewBindingFragment
import kotlinx.coroutines.launch


class FragmentGame : ViewBindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    private val viewModel: GameViewModel by viewModels()
    private val sp by lazy {
        AppSharedPrefs(requireContext())
    }
    private lateinit var gameAdapter: GameAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setStars()
        viewModel.addStarCallBack = {
            sp.increaseBalance(1)
            setStars()
            if (sp.getVibro()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    (requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                        VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    (requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                }
            }
        }

        callbackViewModel.callback = {
            viewModel.pauseValue = false
            viewModel.startTimer()
        }

        binding.buttonHome.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }
        binding.buttonQuit.setOnClickListener {
            end(false)
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pauseValue = true
            viewModel.stopTimer()
            findNavController().navigate(R.id.action_fragmentGame_to_dialogPause)
        }

        binding.buttonReset.setOnClickListener {
            if (sp.getBalance() >= 3) {
                viewModel.reset()
                sp.increaseBalance(-3)
                setStars()
            } else {
                shortToast(requireContext(), "Not enough stars")
            }
        }

        binding.buttonRemoveHorizontal.setOnClickListener {
            if (sp.getBalance() >= 5) {
                viewModel.removeHorizontalLine()
                sp.increaseBalance(-5)
                setStars()
            } else {
                shortToast(requireContext(), "Not enough stars")
            }
        }

        binding.buttonRemoveVertical.setOnClickListener {
            if (sp.getBalance() >= 5) {
                viewModel.removeVerticalLine()
                sp.increaseBalance(-5)
                setStars()
            } else {
                shortToast(requireContext(), "Not enough stars")
            }
        }

        viewModel.list.observe(viewLifecycleOwner) {
            gameAdapter.items = it.toMutableList()
            gameAdapter.notifyDataSetChanged()

            if (it.find { it.value != null } == null) {
                if (viewModel.gameValue) {
                    end(true)
                }
            }
        }

        viewModel.timer.observe(viewLifecycleOwner) { totalSecs ->
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            binding.timeText.text = String.format("%02d:%02d", minutes, seconds);
        }

        if (viewModel.gameValue && !viewModel.pauseValue) {
            viewModel.startTimer()
        }
    }

    private fun setStars() {
        binding.balanceText.text = sp.getBalance().toString()
    }

    private fun end(isWin: Boolean) {
        viewModel.gameValue = false
        viewModel.stopTimer()
        findNavController().navigate(
            FragmentGameDirections.actionFragmentGameToDialogEnd(
                viewModel.timer.value!!,
                isWin
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        gameAdapter = GameAdapter()
        with(binding.gameRV) {
            adapter = gameAdapter
            layoutManager = GridLayoutManager(requireContext(), 5).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            itemAnimator = null
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val childView = rv.findChildViewUnder(e.x, e.y)
                    if (childView != null) {
                        val position = rv.getChildAdapterPosition(childView)
                        lifecycleScope.launch {
                            viewModel.selectItem(position)
                        }
                        if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_CANCEL) {
                            viewModel.checkSelectedItems()
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }
}