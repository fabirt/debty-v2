package com.fabirt.debty.ui.people.home

import androidx.lifecycle.ViewModel
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    val people = personRepository.requestAllPersons()
}