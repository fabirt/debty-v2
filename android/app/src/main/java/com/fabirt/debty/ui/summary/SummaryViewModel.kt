package com.fabirt.debty.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    val people = personRepository.requestAllPersons().asLiveData()
}