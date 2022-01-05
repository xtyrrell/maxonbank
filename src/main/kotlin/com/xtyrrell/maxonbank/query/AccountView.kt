package com.xtyrrell.maxonbank.query

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.xtyrrell.maxonbank.coreapi.AccountDTO
import com.xtyrrell.maxonbank.coreapi.Money
import com.xtyrrell.maxonbank.coreapi.Queries
import org.axonframework.modelling.command.AggregateMember
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime


@Entity
class AccountView(
    @Id var accountId: UUID,
    var balance: Money = 0,
    @JsonManagedReference
    @OneToMany(mappedBy = "accountView", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @AggregateMember
    var ledgerEntries: MutableList<LedgerEntryView> = Collections.emptyList()
) {
    fun addLedgerEntry(ledgerEntry: LedgerEntryView) {
        ledgerEntry.accountView = this

        ledgerEntries.add(ledgerEntry)
    }
}

fun AccountView.toDTO() = AccountDTO(
    accountId = accountId,
    balance = balance
)

// For now, we're co-locating LedgerEntries with AccountView. We might want to move this out
// into its own file later.

enum class LedgerEntryType {
    DEPOSIT, WITHDRAWAL
}

@Entity
class LedgerEntryView(
    @JsonBackReference
    @ManyToOne var accountView: AccountView,
    @Id var ledgerEntryId: UUID,
    var entryType: LedgerEntryType,
    var amount: Money,
    var description: String? = null,
    ) {
    // Important: server timezone should be set to UTC!
    @CreationTimestamp lateinit var timestamp: LocalDateTime
}

interface AccountViewRepository : JpaRepository<AccountView, UUID>