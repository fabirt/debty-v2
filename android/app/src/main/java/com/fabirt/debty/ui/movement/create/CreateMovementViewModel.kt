package com.fabirt.debty.ui.movement.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.domain.model.MovementType
import com.fabirt.debty.domain.repository.movement.MovementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMovementViewModel @Inject constructor(
    private val repository: MovementRepository
) : ViewModel() {

    private val _date: MutableLiveData<Long> = MutableLiveData()
    val date: LiveData<Long> get() = _date

    private var movementType: MovementType? = null

    init {
        _date.value = System.currentTimeMillis()
    }

    suspend fun requestInitialMovement(id: String?): Movement? {
        return id?.toIntOrNull()?.let {
            repository.requestOneTimeMovement(it)
        }
    }

    fun changeDate(value: Long) {
        _date.value = value
    }

    fun changeMovementType(value: MovementType) {
        movementType = value
    }

    fun validateAmount(value: String?): Boolean {
        val amount = value?.toDoubleOrNull()
        return amount != null && amount > 0
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

    suspend fun createMovement(
        personId: Int,
        amount: String?,
        description: String?,
    ) {
        val data = Movement(
            id = 0,
            personId = personId,
            amount = amount!!.toDouble() * movementType!!.multiplier,
            epochMilli = _date.value!!,
            description = description!!,
            type = movementType!!
        )

        repository.createMovement(data)
    }

    suspend fun updateMovement(
        id: Int,
        personId: Int,
        amount: String?,
        description: String?,
    ) {
        val data = Movement(
            id = id,
            personId = personId,
            amount = amount!!.toDouble() * movementType!!.multiplier,
            epochMilli = _date.value!!,
            description = description!!,
            type = movementType!!
        )

        repository.updateMovement(data)
    }
}