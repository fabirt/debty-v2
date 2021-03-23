package com.fabirt.debty.ui.people.detail

import androidx.lifecycle.ViewModel
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.domain.repository.movement.MovementRepository
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val movementRepository: MovementRepository
) : ViewModel() {

    fun requestPerson(personId: Int) = personRepository.requestPerson(personId)

    fun requestMovements(personId: Int): Flow<List<Movement>> =
        movementRepository.requestPersonMovements(personId)
}