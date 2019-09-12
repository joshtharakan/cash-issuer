package com.allianz.t2i.common.contracts.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import net.corda.core.serialization.CordaSerializable

/**
 * Marker interface for bank account numbers.
 */
// TODO: Remove this hack at some point.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = UKAccountNumber::class, name = "UKDEFAULT"),
        JsonSubTypes.Type(value = DEIBANAccountNumber::class, name = "DEIBAN"),
        JsonSubTypes.Type(value = NoAccountNumber::class, name = "none")
)
@CordaSerializable
interface AccountNumber {
    val digits: String
}

/**
 * UK bank account numbers come in sort code and account number pairs.
 */
@CordaSerializable
data class UKAccountNumber(override val digits: String ) : AccountNumber {

    @JsonCreator
    constructor(@JsonProperty("sortCode") sortCode: String, @JsonProperty("accountNumber") accountNumber: String) : this("$sortCode$accountNumber" )

    val sortCode get() = digits.subSequence(0, 6)
    val accountNumber get() = digits.subSequence(6, 14)

    init {
        // Account number validation.
        require(accountNumber.length == 8) { "A UK bank account accountNumber must be eight digits long." }
        require(accountNumber.matches(Regex("[0-9]+"))) {
            "An account accountNumber must only contain the numbers zero to nine."
        }

        // Sort code validation.
        require(sortCode.length == 6) { "A UK bank sort code must be 6 digits long." }
        require(sortCode.matches(Regex("[0-9]+"))) {
            "An account accountNumber must only contain the numbers zero to nine."
        }
    }

    override fun toString() = "Sort Code: $sortCode Account Number: $accountNumber"

}

/**
 * IBAN bank account numbers come in IBAN format.
 */
@CordaSerializable
data class DEIBANAccountNumber(override val digits: String) : AccountNumber {

    @JsonCreator
        constructor(@JsonProperty("countryCode") countryCode: String, @JsonProperty("checkDigit") checkDigit: String, @JsonProperty("bankIdentifier") bankIdentifier: String, @JsonProperty("accountNumber") accountNumber: String ) :
            this("$countryCode$checkDigit$bankIdentifier$accountNumber")

    val countryCode get() = digits.subSequence(0, 2)
    val checkDigit get() = digits.subSequence(2, 4)
    val bankIdentifier get() = digits.subSequence(4, 12)
    val accountNumber get() = digits.subSequence(12,22)

    init {
        // Account number validation.
        require(countryCode.length == 2) { "Country code should be of length 2." }
        require(countryCode.equals("DE")) {
            "Country code should be DE for Germany"
        }

        // Sort code validation.
        require(checkDigit.length == 2) { "A checkdigit  must be 2 digits long." }
        require(checkDigit.matches(Regex("[0-9]+"))) {
            "A check digit must only contain the numbers zero to nine."
        }



        require(bankIdentifier.length == 8) {
            "Bank identifier shoudl be 8 digit long"
        }
        require(checkDigit.matches(Regex("[0-9]+"))) {
            "A Bank identifier must only contain the numbers zero to nine."
        }

        require(accountNumber.length==10) {
            "Bank account number should be 10 digit long"
        }
        require(checkDigit.matches(Regex("[0-9]+"))) {
            "A Bank account number must only contain the numbers zero to nine."
        }
    }

    override fun toString() = "Country Code: $countryCode  Check Digit: $checkDigit Bank Identifier: $bankIdentifier Account Number: $accountNumber"

}

/**
 * Sometimes we don't have a bank account number.
 */
@CordaSerializable
data class NoAccountNumber(override val digits: String = "No bank account number available.") : AccountNumber