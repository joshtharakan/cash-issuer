package com.allianz.t2i.common.contracts

import com.allianz.t2i.common.contracts.states.BankAccountState
import com.allianz.t2i.common.contracts.states.NostroTransactionState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.Requirements.using
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction
import java.security.PublicKey

class NostroTransactionContract : Contract {

    companion object {
        @JvmStatic
        val CONTRACT_ID = "com.allianz.t2i.common.contracts.NostroTransactionContract"
    }

    interface Commands : CommandData {
        class Add : Commands
        class Match : Commands
    }
    // TODO: Contract code not implemented for demo.
    override fun verify(tx: LedgerTransaction) {

        // There should be one and only one single command in the transaction
        val command = tx.commands.requireSingleCommand<NostroTransactionContract.Commands>()
        when (command.value) {
            is NostroTransactionContract.Commands.Add -> {

                requireThat {
                    // Shape constraints
                    "No inputs should be consumed when creating a new bank Account state." using (tx.inputs.isEmpty())
                    "At least one output state should be created.." using (tx.outputs.size >= 1)

                    // Content constraints
                   tx.outputsOfType<NostroTransactionState>().forEach {

                       "transaction output should be a Nostro Transaction state" using (it is NostroTransactionState)

                       // Required signer constraints
                       "There must be exactly one signers" using (command.signers.toSet().size == 1)
                   }


                }

            }

            is NostroTransactionContract.Commands.Match -> {

                requireThat {


                }
            }

        }
    }
}