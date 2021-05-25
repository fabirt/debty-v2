package com.fabirt.debty.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyTest {

    @Test
    fun formatNumberToCurrencyString_isCorrect() {
        val result = 10_000.toCurrencyString()
        assertThat(result).isEqualTo(result)
    }

    @Test
    fun formatOptionalNumberToCurrencyString_isCorrect() {
        val result = null.toCurrencyString()
        assertThat(result).isEqualTo(result)
    }

    @Test
    fun formatNumberToDecimalString_isCorrect() {
        val result = 10_000.toDecimalString()
        assertThat(result).isEqualTo(result)
    }

    @Test
    fun formatOptionalNumberToDecimalString_isCorrect() {
        val result = null.toDecimalString()
        assertThat(result).isEqualTo("0")
    }
}