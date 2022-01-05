package com.xtyrrell.maxonbank.query

import com.xtyrrell.maxonbank.coreapi.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class AccountProjector(private val repository: AccountViewRepository) {

    @EventHandler
    fun on(event: Events.AccountOpened) {
        val accountView = AccountView(event.accountId, 0)
        repository.save(accountView)
    }

    @EventHandler
    fun on(event: Events.FundsDeposited) {
        val accountView: AccountView = repository.findById(event.accountId).get()

        val entry = LedgerEntryView(accountView, event.ledgerEntryId, LedgerEntryType.DEPOSIT, event.amount)
        accountView.addLedgerEntry(entry)

        accountView.balance += event.amount

        repository.save(accountView)
    }

    @EventHandler
    fun on(event: Events.FundsWithdrawn) {
        val accountView: AccountView = repository.findById(event.accountId).get()

        val entry = LedgerEntryView(accountView, event.ledgerEntryId, LedgerEntryType.WITHDRAWAL, event.amount)
        accountView.addLedgerEntry(entry)

        accountView.balance -= event.amount

        repository.save(accountView)
    }

    @QueryHandler
    fun handle(query: Queries.AccountBalance): Money? {
        val accountView = repository.findById(query.accountId).orElse(null)

        return accountView?.balance
    }

    @QueryHandler
    fun handle(query: Queries.AccountLedgerEntries): List<LedgerEntryView>? {
        val accountView = repository.findById(query.accountId).orElse(null)

        return accountView?.ledgerEntries
    }

    @QueryHandler
    fun handle(query: Queries.Accounts): List<AccountView> {
        return repository.findAll()
    }

    @QueryHandler
    fun handle(query: Queries.Account): AccountView? {
        return repository.findById(query.accountId).orElse(null)
    }
}