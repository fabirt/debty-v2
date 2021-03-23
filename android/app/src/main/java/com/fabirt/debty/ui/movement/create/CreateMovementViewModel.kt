package com.fabirt.debty.ui.movement.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.domain.model.MovementType
import com.fabirt.debty.domain.repository.movement.MovementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMovementViewModel @Inject constructor(
    private val repository: MovementRepository
) : ViewModel() {

    private val _movement: MutableLiveData<Movement> = MutableLiveData()
    val movement: LiveData<Movement> get() = _movement

    private val _date: MutableLiveData<Long> = MutableLiveData()
    val date: LiveData<Long> get() = _date

    private var movementType: MovementType? = null

    init {
        _date.value = System.currentTimeMillis()
    }

    fun changeDate(value: Long) {
        _date.value = value
    }

    fun changeMovementType(value: MovementType) {
        movementType = value
    }

    fun validateAmount(value: String?): Boolean {
        return value?.toDoubleOrNull() != null
    }

    fun validateDescription(value: String?): Boolean {
        return value?.isNotBlank() == true
    }

    fun validateDate(): Boolean {
        return _date.value != null
    }

    fun validateMovementType(): Boolean {
        return movementType != null
    }

    fun createMovement(
        personId: Int,
        amount: String?,
        description: String?,
    ) {
        val data = Movement(
            id = 0,
            personId =  personId,
            amount =  amount!!.toDouble() * movementType!!.multiplier,
            epochMilli =  _date.value!!,
            description =  description!!,
            type =  movementType!!
        )
        viewModelScope.launch {
            repository.createMovement(data)
        }
    }
}