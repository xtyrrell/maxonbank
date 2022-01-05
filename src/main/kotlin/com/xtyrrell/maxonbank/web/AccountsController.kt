package com.xtyrrell.maxonbank.web

import com.xtyrrell.maxonbank.coreapi.*
import com.xtyrrell.maxonbank.query.AccountView
import com.xtyrrell.maxonbank.query.LedgerEntryView
import com.xtyrrell.maxonbank.query.toDTO
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
    fun create(
        @RequestBody openAccountRequest: OpenAccountRequest
    ): CompletableFuture<UUID> = commandGateway.send(Commands.OpenAccount(openAccountRequest.accountId))

    @GetMapping
    fun index(): CompletableFuture<List<AccountView>> = queryGateway.query(Queries.Accounts(), ResponseTypes.multipleInstancesOf(AccountView::class.java))

    @GetMapping("/total-balance")
    fun totalBalance() = queryGateway.query(Queries.TotalBalance(), ResponseTypes.instanceOf(Money::class.java))

    @PostMapping("/{accountId}/deposits")
    fun deposit(
        @PathVariable("accountId") accountId: String,
        @RequestBody depositFundsRequest: DepositFundsRequest
    ): DepositFundsRequest {
        // TODO: Add error handling -- catch if there is an error and return something meaningful
        // TODO: Find a way to avoid the "Not enough information to infer type variable R" error if we omit the `val x`
        val x: CompletableFuture<Unit> = commandGateway.send(Commands.DepositFunds(depositFundsRequest.ledgerEntryId, UUID.fromString(accountId), depositFundsRequest.amount))

        return depositFundsRequest
    }

    @PostMapping("/{accountId}/withdrawals")
    fun withdraw(
        @PathVariable("accountId") accountId: String,
        @RequestBody withdrawFundsRequest: WithdrawFundsRequest
    ): WithdrawFundsRequest {
        // TODO: Add error handling -- catch if there is an error and return something meaningful
        // TODO: Find a way to avoid the "Not enough information to infer type variable R" error if we omit the `val x`
        val x: CompletableFuture<Unit> = commandGateway.send(Commands.WithdrawFunds(withdrawFundsRequest.ledgerEntryId, UUID.fromString(accountId), withdrawFundsRequest.amount))

        return withdrawFundsRequest
    }

    @GetMapping("/{accountId}")
    fun show(
        @PathVariable("accountId") accountId: String
    ): AccountDTO? {
        val accountView = queryGateway.query(Queries.Account(UUID.fromString(accountId)), ResponseTypes.instanceOf(AccountView::class.java)).get()

        return accountView?.toDTO()
    }

    // Using terminology from https://www.notion.so/Ubiquitous-Language-51b1115c4f42410fb2bd10e52548b1ad
    @GetMapping("/{accountId}/ledger")
    fun showLedger(
        @PathVariable("accountId") accountId: String
    ): CompletableFuture<List<LedgerEntryView>> {
        return queryGateway.query(Queries.AccountLedgerEntries(UUID.fromString(accountId)),
            ResponseTypes.multipleInstancesOf(LedgerEntryView::class.java))
    }

    @GetMapping("/{accountId}/balance")
    fun showBalance(
        @PathVariable("accountId") accountId: String
    ): CompletableFuture<Money> {
        return queryGateway.query(
            Queries.AccountBalance(UUID.fromString(accountId)), ResponseTypes.instanceOf(Money::class.java)
        )
    }
}

class OpenAccountRequest(val accountId: UUID)
class DepositFundsRequest(val ledgerEntryId: UUID, val amount: Money)
class WithdrawFundsRequest(val ledgerEntryId: UUID, val amount: Money)
