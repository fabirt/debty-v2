package com.fabirt.debty.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyTest {

    @Test
    fun formatNumberToCurrencyString_isCorrect() {
        val result = 10_000.toCurrencyString()
        assertThat(result).isEqualTo("\$10.000,00")
    }

    @Test
    fun formatOptionalNumberToCurrencyString_isCorrect() {
        val result = null.toCurrencyString()
        assertThat(result).isEqualTo("\$0,00")
    }

    @Test
    fun formatNumberToDecimalString_isCorrect() {
        val result = 10_000.toDecimalString()
        assertThat(result).isEqualTo("10.000")
    }

    @Test
    fun formatOptionalNumberToDecimalString_isCorrect() {
        val result = null.toDecimalString()
        assertThat(result).isEqualTo("0")
    }
}