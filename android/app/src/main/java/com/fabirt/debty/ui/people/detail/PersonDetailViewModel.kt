package com.fabirt.debty.ui.people.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.domain.model.Movement
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

    fun requestPerson(personId: Int) = personRepository.requestPerson(personId)

    fun requestMovements(personId: Int): Flow<List<Movement>> =
        movementRepository.requestPersonMovementsSortedByDate(personId)

    fun requestBalance(personId: Int) = movementRepository.requestPersonBalance(personId)

    fun deletePerson(personId: Int) {}

    override fun onSwiped(item: Movement) {
        viewModelScope.launch {
            movementRepository.deleteMovement(item)
        }
    }
}