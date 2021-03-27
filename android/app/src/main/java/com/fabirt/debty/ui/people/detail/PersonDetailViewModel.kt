package com.fabirt.debty.ui.people.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.domain.model.MovementType
import com.fabirt.debty.domain.repository.movement.MovementRepository
import com.fabirt.debty.domain.repository.person.PersonRepository
import com.fabirt.debty.ui.common.SwipeToDeleteDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val movementRepository: MovementRepository
) : ViewModel(), SwipeToDeleteDelegate<Movement> {

    private val _lastItemRemoved = MutableLiveData<Movement?>()
    val lastItemRemoved: LiveData<Movement?> get() = _lastItemRemoved

    fun requestPerson(personId: Int) = personRepository.requestPerson(personId)

    fun requestMovements(personId: Int): Flow<List<Movement>> =
        movementRepository.requestPersonMovementsSortedByDate(personId)

    fun requestBalance(personId: Int) = movementRepository.requestPersonBalance(personId)

    suspend fun deletePerson(personId: Int) {
        personRepository.deleteAllPersonRelatedData(personId)
    }

    suspend fun deleteHistory(personId: Int): Int {
        return personRepository.deleteAllPersonRelatedData(personId, false)
    }

    override fun onSwiped(item: Movement) {
        viewModelScope.launch {
            movementRepository.deleteMovement(item)
            _lastItemRemoved.value = item
        }
    }

    fun undoItemRemoval(item: Movement) {
        viewModelScope.launch {
            movementRepository.createMovement(item)
        }
    }

    fun clearLastRemovedItem() {
        _lastItemRemoved.value = null
    }

    fun settleAccount(personId: Int, amount: Double, description: String) {
        val movementType = if (amount > 0) MovementType.OwedMeSettled else MovementType.IOwedSettled
        val movement = Movement(
            0,
            personId,
            amount * -1,
            System.currentTimeMillis(),
            description,
            movementType
        )

        viewModelScope.launch {
            movementRepository.createMovement(movement)
        }
    }
}