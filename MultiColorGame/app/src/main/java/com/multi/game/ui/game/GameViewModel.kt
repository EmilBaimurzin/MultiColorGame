package com.multi.game.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.multi.game.core.library.random
import com.multi.game.core.library.safe
import com.multi.game.domain.game.GameRepository
import com.multi.game.domain.game.adapter.GameItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val repository = GameRepository()
    private var gameScope = CoroutineScope(Dispatchers.Default)
    var addStarCallBack: (() -> Unit)? = null

    var gameValue = true
    var pauseValue = false
    private val _list = MutableLiveData(repository.generateList())
    val list: LiveData<List<GameItem>> = _list

    private val _timer = MutableLiveData(0)
    val timer: LiveData<Int> = _timer

    fun selectItem(index: Int) {
        safe {
            val newList = _list.value!!.toMutableList()
            if (newList[index].value != null) {
                newList[index].isSelected = true
                _list.postValue(newList)
            }
        }
    }

    fun startTimer() {
        gameScope = CoroutineScope(Dispatchers.Default)
        gameScope.launch {
            while (true) {
                _timer.postValue(_timer.value!! + 1)
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        gameScope.cancel()
    }

    fun checkSelectedItems() {
        val currentList = _list.value!!.toMutableList()
        val selectedItem = currentList.find { it.isSelected }
        if (selectedItem != null) {
            val allSelectedItems = currentList.filter { it.isSelected }
            val uniqueItemsList = mutableListOf<Int>()
            allSelectedItems.forEach {
                uniqueItemsList.add(it.value!!)
            }
            val uniqueItems = uniqueItemsList.distinct()
            if (uniqueItems.size == 1 && allSelectedItems.size >= 2) {
                checkAllRows()
                if (allSelectedItems.size >= 4) {
                    addStarCallBack?.invoke()
                }
            } else {
                currentList.map { it.isSelected = false }
                _list.postValue(currentList)
            }
        }
    }

    private fun checkAllRows() {
        val currentList = _list.value!!.toMutableList()
        repeat(5) { index ->
            val listOfRow = mutableListOf<GameItem>()
            if (!currentList[index].isSelected) {
                listOfRow.add(currentList[index])
            }
            if (!currentList[index + 5].isSelected) {
                listOfRow.add(currentList[index + 5])
            }
            if (!currentList[index + 10].isSelected) {
                listOfRow.add(currentList[index + 10])
            }
            if (!currentList[index + 15].isSelected) {
                listOfRow.add(currentList[index + 15])
            }
            if (!currentList[index + 20].isSelected) {
                listOfRow.add(currentList[index + 20])
            }
            if (!currentList[index + 25].isSelected) {
                listOfRow.add(currentList[index + 25])
            }
            if (!currentList[index + 30].isSelected) {
                listOfRow.add(currentList[index + 30])
            }
            if (!currentList[index + 35].isSelected) {
                listOfRow.add(currentList[index + 35])
            }
            listOfRow.reverse()
            repeat(8) {
                val position = when (it) {
                    0 -> index + 35
                    1 -> index + 30
                    2 -> index + 25
                    3 -> index + 20
                    4 -> index + 15
                    5 -> index + 10
                    6 -> index + 5
                    else -> index + 0
                }
                if (listOfRow.size >= it + 1) {
                    currentList[position].value = listOfRow[it].value
                } else {
                    currentList[position].value = null
                }
            }
        }
        currentList.map { it.isSelected = false }
        _list.postValue(currentList)
    }

    fun removeVerticalLine() {
        val randomRow = 0 random 4
        val currentList = _list.value!!.toMutableList()
        var counter = 0
        listOf(0, 5, 10, 15, 20, 25, 30, 35).forEach {
            if (currentList[it + randomRow].value != null) {
                currentList[it + randomRow].isSelected = true
            } else {
                counter += 1
            }
        }
        if (_list.value!!.find { it.value != null } != null) {
            if (counter == 8) {
                removeVerticalLine()
            }
            _list.postValue(currentList)
            checkAllRows()
        }
    }

    fun removeHorizontalLine() {
        val randomRow = listOf(0, 5, 10, 15, 20, 25, 30, 35).random()
        val currentList = _list.value!!.toMutableList()
        var counter = 0
        listOf(0, 1, 2, 3, 4).forEach {
            if (currentList[it + randomRow].value != null) {
                currentList[it + randomRow].isSelected = true
            } else {
                counter += 1
            }
        }
        if (_list.value!!.find { it.value != null } != null) {
            if (counter == 5) {
                removeHorizontalLine()
            }
            _list.postValue(currentList)
            checkAllRows()
        }
    }

    fun reset() {
        _list.postValue(repository.generateList())
    }
}