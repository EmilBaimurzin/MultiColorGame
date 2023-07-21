package com.multi.game.domain.game

import com.multi.game.core.library.random
import com.multi.game.domain.game.adapter.GameItem

class GameRepository {
    fun generateList(): List<GameItem> {
        val listToReturn = mutableListOf<GameItem>()
        repeat(40) { index ->
            listToReturn.add(GameItem(value = 1 random 5).also {
                if (index % 2 == 0) it.bgValue = true
            })
        }
        return listToReturn
    }
}