package com.fabirt.debty.util

import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.util.calculateSummaryData
import com.google.common.truth.Truth.assertThat
import org.junit.Test

import org.junit.Before

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SummaryTest {
    private lateinit var people: List<Person>

    @Before
    fun setup() {
        people = listOf(
            Person(1, "A", 0, null, 50_000.0),
            Person(2, "B", 0, null, 40_000.0),
            Person(3, "C", 0, null, -20_000.0),
            Person(3, "D", 0, null, -10_000.0),
        )
    }

    @Test
    fun summaryBalance_isCorrect() {
        val summary = calculateSummaryData(people)

        assertThat(summary.balance).isEqualTo(60_000)
    }

    @Test
    fun summaryPositive_isCorrect() {
        val summary = calculateSummaryData(people)

        assertThat(summary.positive).isEqualTo(90_000)
    }

    @Test
    fun summaryNegative_isCorrect() {
        val summary = calculateSummaryData(people)

        assertThat(summary.negative).isEqualTo(30_000)
    }
}