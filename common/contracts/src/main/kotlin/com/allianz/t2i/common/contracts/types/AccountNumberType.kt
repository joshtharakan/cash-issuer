package com.allianz.t2i.common.contracts.types

import net.corda.core.serialization.CordaSerializable


/**
 * To represent different internationally recognized, standardized methods of identifying bank accounts like IBAN, SWIFT etc..
 */
@CordaSerializable
enum class AccountNumberType {
    DEIBAN,
    SWIFT,
    UKDEFAULT,
    NONE
}