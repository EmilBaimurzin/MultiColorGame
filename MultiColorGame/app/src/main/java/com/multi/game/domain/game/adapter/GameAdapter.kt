package com.multi.game.domain.game.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multi.game.R
import com.multi.game.core.library.l
import com.multi.game.databinding.ItemGameBinding

class GameAdapter : RecyclerView.Adapter<GameViewHolder>() {
    var items = mutableListOf<GameItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class GameViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: GameItem) {
        val background =
            if (item.bgValue) R.drawable.bg_unselected_1 else R.drawable.bg_unselected_2
        binding.root.setBackgroundResource(background)
        if (item.isSelected) binding.root.setBackgroundResource(R.drawable.bg_selected)

        when (item.value) {
            1 -> binding.imgSymbol.setImageResource(R.drawable.img_symbol01)
            2 -> binding.imgSymbol.setImageResource(R.drawable.img_symbol02)
            3 -> binding.imgSymbol.setImageResource(R.drawable.img_symbol03)
            4 -> binding.imgSymbol.setImageResource(R.drawable.img_symbol04)
            5 -> binding.imgSymbol.setImageResource(R.drawable.img_symbol05)
            else -> binding.imgSymbol.setImageDrawable(null)
        }
    }
}
