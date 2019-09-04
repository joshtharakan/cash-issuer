package com.allianz.t2i.common.contracts

import com.allianz.t2i.common.contracts.states.BankAccountState
import com.allianz.t2i.common.contracts.types.BankAccount
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.Requirements.using
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.identity.Party
import net.corda.core.transactions.LedgerTransaction
import java.security.PublicKey

class BankAccountContract : Contract {

    companion object {
        @JvmStatic
        val CONTRACT_ID = "com.allianz.t2i.common.contracts.BankAccountContract"
    }

    interface Commands : CommandData {
        class Add : Commands
        class Update : Commands
    }


    // TODO: Contract code not implemented for demo.
    override fun verify(tx: LedgerTransaction) {

        // There should be one and only one single command in the transaction
        val command = tx.commands.requireSingleCommand<Commands>()
        when (command.value) {
            is Commands.Add -> {

                requireThat {
                    // Shape constraints
                    "No inputs should be consumed when creating a new bank Account state." using (tx.inputs.isEmpty())
                    "Only one output state should be created.." using (tx.outputs.size == 1)

                    // Content constraints
                    val bankAccountState = tx.outputsOfType<BankAccountState>().single()
                    "transaction output should be a Bank Account state" using (bankAccountState is BankAccountState)


                    // Required signer constraints
                    val expectedSigners: PublicKey = bankAccountState.owner.owningKey
                    "There must be exactly one signers" using (command.signers.toSet().size == 1)
                    "Owner must be signers." using (command.signers.contains(expectedSigners))
                }

            }

            is Commands.Update -> {

                requireThat {


                }
            }

        }
    }

}