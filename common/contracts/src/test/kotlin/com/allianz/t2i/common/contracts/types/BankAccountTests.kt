package com.allianz.t2i.common.contracts.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals

class BankAccountTests {


    @Test
    fun `deserialising the German IBAN account type bank account  from rest api`() {

        //{ accountId: 12345, accountName: josh, accountNumber: { sortCode: 442200, accountNumber: 13371337, type: uk }, currency: { tokenIdentifier: GBP, fractionDigits: 2} }, verifier: Issuer
        val sampleIBANAccount: String = "{\"accountId\":\"12345\",\"accountName\":\"josh\",\"accountNumber\":{\"countryCode\":\"DE\",\"checkDigit\":\"98\",\"bankIdentifier\":\"70040045\",\"accountNumber\":\"0002333222\",\"type\":\"DEIBAN\"},\"currency\":{\"tokenIdentifier\": \"GBP\",\"fractionDigits\":\"2\"}}"

        val bankAccount: BankAccount = jacksonObjectMapper()
                .readerFor(BankAccount::class.java)
                .readValue(sampleIBANAccount)

        assertEquals("12345", bankAccount.accountId);
        assertEquals(AccountNumberType.DEIBAN, bankAccount.accountNumber.type)
        assertEquals("DE98700400450002333222", bankAccount.accountNumber.digits)


    }

}