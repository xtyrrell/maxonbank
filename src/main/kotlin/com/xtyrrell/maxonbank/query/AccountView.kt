package com.xtyrrell.maxonbank.query

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.xtyrrell.maxonbank.coreapi.Money
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*


@Entity
class AccountView(
    @Id var accountId: UUID,
    var balance: Money = 0,
    @JsonManagedReference
    @OneToMany(mappedBy = "accountView", fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) var ledgerEntries: MutableList<LedgerEntryView> = Collections.emptyList()
) {
    fun addLedgerEntry(ledgerEntry: LedgerEntryView) {
        ledgerEntry.accountView = this

        ledgerEntries.add(ledgerEntry)
    }
}

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
    // https://stackoverflow.com/questions/47617726/creationtimestamp-and-updatetimestamp-dont-work-in-kotlin
//    @field:CreationTimestamp lateinit var timestamp: Date
}

interface AccountViewRepository : JpaRepository<AccountView, UUID>