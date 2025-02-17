package com.allianz.t2i.client.rpc.server.controllers

import com.allianz.t2i.client.rpc.server.common.InternalServiceException
import net.corda.client.jackson.JacksonSupport
import com.allianz.t2i.client.rpc.server.common.RPCFetchException
import com.allianz.t2i.client.rpc.server.model.AddBankAccountRequest
import com.allianz.t2i.client.rpc.server.service.StandardService
import com.allianz.t2i.common.contracts.types.BankAccount
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*



/**
 * Define CorDapp-specific endpoints in a controller such as this.
 */
@RestController
@RequestMapping("/t2i") // The paths for GET and POST requests are relative to this base path.
class CustomController(val standardService: StandardService) {

    /**
     * SLF4J logging
     */
    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }


    /**
     * Rest endpoint to add a bank account with account details and node info to which connections is established
     * @param node Node identity to be used for rpc connection
     * @param account ID,Name,Number,sortcode represents account details
     */
    @PostMapping(path = arrayOf("/addbankaccount" ), produces = arrayOf("application/json"), consumes = arrayOf("application/json"))
    private fun addBankAccount(
            @RequestBody addBankAccountRequest: AddBankAccountRequest
    ): ResponseEntity<String> {

        try {

            logger.info("Add bank account called")
            /**
             * provide the validation for details provided
             */
//            require(addBankAccountRequest.bankAccountDetails.accountId.length>6) {"requires account id of size greater than 6"}



            // call to the service for adding bank account
            val rpcResponse = this.standardService.addBankAccount(node = addBankAccountRequest.node, bankAccountDetails = addBankAccountRequest.bankAccountDetails)
            logger.info(rpcResponse.toString())


            if(rpcResponse.status.equals("success")){


                // generate json response
                val mapper = JacksonSupport.createNonRpcMapper()
                val AddBankResponse = mapper.writeValueAsString(rpcResponse)  // myCordaState can be any object.

                return ResponseEntity.ok().body(AddBankResponse)
            }
            else throw RPCFetchException()

        } catch (exception: Exception) {
            throw RPCFetchException()
        }


    }

    /**
     * Rest endpoint for token transfer and node info to which connections is established
     * @param node Node identity to be used for rpc connection
     * @param recipient recipient party identifier
     * @param amount amount to transfer
     */
    @PostMapping( "/tokentransfer")
    private fun tokenTransfer(@RequestParam(value="node", defaultValue = "AGCSSE") node: String,
                              @RequestParam(value="recipient", defaultValue = "PartyB") recipient: String,
                              @RequestParam(value="amount", defaultValue = "100") amount: String)
                : ResponseEntity<String> {

        try {
            val rpcResponse = this.standardService.tokenTransfer(node, recipient, amount)
            logger.info(rpcResponse.toString())
            if(rpcResponse.status.equals("success")){


                // generate json response
                val mapper = JacksonSupport.createNonRpcMapper()
                val tokenTransferResponse = mapper.writeValueAsString(rpcResponse)  // myCordaState can be any object.

                return ResponseEntity.ok().body(tokenTransferResponse)
            }
            else throw RPCFetchException()

        } catch (exception: Exception) {
            throw RPCFetchException()
        }


    }



    /**
     * Rest endpoint for fetch token balance from vault using vault query
     * @param node Node identity to be used for rpc connection
     */
//    @CrossOrigin(origins = ["http://localhost:4200"])
    @GetMapping( "/tokenbalance")
    private fun getTokenBalance(@RequestParam(value="node", defaultValue = "AGCSSE") node: String): ResponseEntity<String> {

        try {
            val rpcResponse = this.standardService.getTokenBalance(node)
            logger.info(rpcResponse.toString())
            if(rpcResponse.status.equals("success")){


                // generate json response
                val mapper = JacksonSupport.createNonRpcMapper()
                val addBankResponse = mapper.writeValueAsString(rpcResponse)  // myCordaState can be any object.

                return ResponseEntity.ok().body(addBankResponse)
            }
            else throw RPCFetchException()

        } catch (exception: Exception) {
            throw RPCFetchException()
        }


    }

    /**
     * Rest endpoint for token transfer and node info to which connections is established
     * @param node Node identity to be used for rpc connection
     * @param amount amount to redeem
     */
    @PostMapping( "/tokenredeem")
    private fun tokenTransfer(@RequestParam(value="node", defaultValue = "AGCSSE") node: String,
                              @RequestParam(value="amount", defaultValue = "100") amount: String)
            : ResponseEntity<String> {

        try {
            val rpcResponse = this.standardService.tokenRedemption(node, amount)
            logger.info(rpcResponse.toString())
            if(rpcResponse.status.equals("success")){


                // generate json response
                val mapper = JacksonSupport.createNonRpcMapper()
                val tokenRedemptionResponse = mapper.writeValueAsString(rpcResponse)  // myCordaState can be any object.

                return ResponseEntity.ok().body(tokenRedemptionResponse)
            }
            else throw RPCFetchException()

        } catch (exception: Exception) {
            throw RPCFetchException()
        }


    }


    /**
     * Rest endpoint for token transfer and node info to which connections is established
     * @param node Node identity to be used for rpc connection
     * @param amount amount to redeem
     */
    @GetMapping( "/transactionlist")
    private fun transactionList(@RequestParam(value="node", defaultValue = "AGCSSE") node: String)
            : ResponseEntity<String> {

        try {
            val transactionList = standardService.fetchTransactionList(node)



            if(transactionList.isNotEmpty()){


                // generate json response
                val mapper = JacksonSupport.createNonRpcMapper()
                val transactionListResponse = mapper.writeValueAsString(transactionList)  // myCordaState can be any object.

                return ResponseEntity.ok().body(transactionListResponse)
            }
            else throw InternalServiceException("Transaction list empty")

        } catch (exception: Exception) {
            throw InternalServiceException(exception.message?:"Transaction fetch exception")
        }


    }


}