package com.allianz.t2i.common.contracts.types

import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.allianz.t2i.common.contracts.states.BankAccountState
import com.r3.corda.lib.tokens.money.USD
import net.corda.core.identity.Party
import net.corda.core.serialization.CordaSerializable

@CordaSerializable
data class BankAccount(
        val accountId: String,
        val accountName: String,
        val accountNumber: AccountNumber,
        val currency: TokenType = USD,
        val type: BankAccountType = BankAccountType.COLLATERAL // Defaulted to collateral for now.
)

//{
//
//    // Constructor to set USD as the default
//    constructor(  accountId: String,
//                  accountName: String,
//                  accountNumber: AccountNumber
//    ):this(accountId, accountName, accountNumber, USD)
//}


fun BankAccount.toState(owner: Party, verifier: Party): BankAccountState {
    return BankAccountState(owner, verifier, accountId, accountName, accountNumber,  currency, type)
}