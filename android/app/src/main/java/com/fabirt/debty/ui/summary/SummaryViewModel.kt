package com.fabirt.debty.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.domain.model.SummaryData
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class SummaryViewModel @Inject constructor(
    personRepository: PersonRepository
) : ViewModel() {

    val people = personRepository.requestAllPersonsWithTotal()
}