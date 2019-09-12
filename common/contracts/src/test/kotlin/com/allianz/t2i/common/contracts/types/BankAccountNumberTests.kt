package com.allianz.t2i.common.contracts.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.h2.value.DataType.readValue




class BankAccountNumberTests {


    @Test
    fun `deserialising the IBAN type bank account number from rest api`() {

       val sampleIBANAccount: String = "{\"countryCode\":\"DE\",\"checkDigit\":\"98\",\"bankIdentifier\":\"70040045\",\"accountNumber\":\"0002333222\",\"type\":\"DEIBAN\"}"


        val accountNumber: AccountNumber = jacksonObjectMapper()
                .readerFor(AccountNumber::class.java)
                .readValue(sampleIBANAccount)


        assertEquals("DE98700400450002333222", accountNumber.digits)
        assertEquals(AccountNumberType.DEIBAN, accountNumber.type)
    }

}