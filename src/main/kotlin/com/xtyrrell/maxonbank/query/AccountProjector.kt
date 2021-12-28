package com.xtyrrell.maxonbank.query

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountProjector(private val repository: AccountViewRepository) {

    @EventHandler
    fun on(event: AccountOpenedEvent) {
        val accountView = AccountView(event.accountId, 0)
        repository.save(accountView)
    }

    @EventHandler
    fun on(event: FundsDepositedEvent) {
        val accountView: AccountView = repository.findById(event.accountId).get()

        val entryId = UUID.randomUUID()
        val entry = LedgerEntryView(accountView, entryId, LedgerEntryType.DEPOSIT, event.amount)
        accountView.addLedgerEntry(entry)

        accountView.balance += event.amount

        repository.save(accountView)
    }

    @EventHandler
    fun on(event: FundsWithdrawnEvent) {
        val accountView: AccountView = repository.findById(event.accountId).get()

        val entryId = UUID.randomUUID()
        val entry = LedgerEntryView(accountView, entryId, LedgerEntryType.WITHDRAWAL, event.amount)
        accountView.addLedgerEntry(entry)

        accountView.balance -= event.amount

        repository.save(accountView)
    }

    @QueryHandler
    fun handle(query: GetAccountAvailableBalanceQuery): Money? {
        val accountView = repository.findById(query.accountId).orElse(null)

        return accountView?.balance
    }

    @QueryHandler
    fun handle(query: ListAccountHistoryQuery): List<LedgerEntryView> {
        val accountView = repository.findById(query.accountId)

//        if (accountView.isEmpty) {
//            println("accountView for id ${query.accountId} is empty!")
//            throw Exception("account view is empty :( !!")
//        }

        return accountView.get().ledgerEntries
    }

    // NB: This implementation is placeholder and naive:
    // - it will break if the sum of balances in our system is more than
    //      Int.MAX_VALUE
    // - it loads all accounts into memory, which I presume is not ideal at
    //      scale
    @QueryHandler
    fun handle(query: GetTotalAvailableBalanceQuery): Money {
//        TODO: Rather optimise this for queries: maintain a `totalBalance` field on a LedgerInfo entity
//        and then query against that here.
        return repository.findAll().sumOf { it.balance }
    }

    @QueryHandler
    fun handle(query: ListAccountsQuery): List<AccountView> {
        return repository.findAll()
    }

    @QueryHandler
    fun handle(query: GetAccountDetailsQuery): Optional<AccountView> {
        return repository.findById(query.accountId)
    }
}