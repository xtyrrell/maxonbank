package com.xtyrrell.maxonbank.web

import com.xtyrrell.maxonbank.coreapi.*
import com.xtyrrell.maxonbank.query.AccountView
import com.xtyrrell.maxonbank.query.LedgerEntryView
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture

@RequestMapping("/accounts")
@RestController
class AccountsController(
    private val commandGateway: CommandGateway, private val queryGateway: QueryGateway
) {

    @PostMapping
    fun create(): CompletableFuture<UUID> {
        return commandGateway.send(OpenAccountCommand())
    }

    @GetMapping
    fun index() = queryGateway.query(ListAccountsQuery(), ResponseTypes.multipleInstancesOf(AccountView::class.java))

    // TODO: Return this in a machine-readable format
    @GetMapping("/total-balance")
    fun totalBalance(): String {
        val totalBalance = queryGateway.query(GetTotalAvailableBalanceQuery(), ResponseTypes.instanceOf(Money::class.java))

        return "The total available balance across all accounts at MaxonBank is ${totalBalance.get()}"
    }

    @PostMapping("/{accountId}/deposits")
    fun deposit(
        @PathVariable("accountId") accountId: String,
        @RequestBody depositFundsRequest: DepositFundsRequest
    ): CompletableFuture<UUID> {
        // TODO: This returns nothing. Why is that? How can we make it return something, eg a UUID
        // or amount deposited?
        // TODO: Add error handling -- catch if there is an error and return something meaningful
        return commandGateway.send(DepositFundsCommand(UUID.fromString(accountId), depositFundsRequest.amount))
    }

    @PostMapping("/{accountId}/withdrawals")
    fun withdraw(
        @PathVariable("accountId") accountId: String,
        @RequestBody withdrawFundsRequest: WithdrawFundsRequest
    ): CompletableFuture<UUID> {
        // TODO: This returns nothing. Why is that? How can we make it return something, eg a UUID
        // or amount withdrawn?
        // TODO: Add error handling -- catch if there is an error and return something meaningful
        return commandGateway.send(WithdrawFundsCommand(UUID.fromString(accountId), withdrawFundsRequest.amount))
    }

    @GetMapping("/{accountId}")
    fun show(
        @PathVariable("accountId") accountId: String
    ): CompletableFuture<AccountView>? {
        return queryGateway.query(GetAccountDetailsQuery(UUID.fromString(accountId)), ResponseTypes.instanceOf(AccountView::class.java))
    }

    // Using terminology from https://www.notion.so/Ubiquitous-Language-51b1115c4f42410fb2bd10e52548b1ad
    @GetMapping("/{accountId}/ledger")
    fun showLedger(
        @PathVariable("accountId") accountId: String
    ): CompletableFuture<List<LedgerEntryView>> {
        return queryGateway.query(ListAccountHistoryQuery(UUID.fromString(accountId)),
            ResponseTypes.multipleInstancesOf(LedgerEntryView::class.java))
    }

    @GetMapping("/{accountId}/balance")
    fun showBalance(
        @PathVariable("accountId") accountId: String
    ): CompletableFuture<Money> {
        return queryGateway.query(
            GetAccountAvailableBalanceQuery(UUID.fromString(accountId)), ResponseTypes.instanceOf(Money::class.java)
        )
    }
}

class DepositFundsRequest(val amount: Money)
class WithdrawFundsRequest(val amount: Money)
