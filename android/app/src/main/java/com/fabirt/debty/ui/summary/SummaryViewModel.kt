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
    private val personRepository: PersonRepository
) : ViewModel() {

    val people = personRepository.requestAllPersonsWithTotal()

    fun calculateSummaryData(list: List<Person>): SummaryData {
        var balance = 0.0
        var positive = 0.0
        var negative = 0.0
        list.forEach { person ->
            val total = person.total
            if (total != null) {
                balance += total

                if (total > 0) {
                    positive += total
                } else if (total < 0) {
                    negative += total.absoluteValue
                }
            }
        }

        return SummaryData(balance, positive, negative)
    }
}