package com.xtyrrell.maxonbank.coreapi

import java.util.UUID

interface Query

sealed class Queries {

    // Main queries
    data class AccountBalance(
        val accountId: UUID,
    ) : Query

    data class AccountLedgerEntries(
        val accountId: UUID,
    ) : Query

    class TotalBalance : Query

    // Other queries
    class Accounts : Query

    data class Account(
        val accountId: UUID
    ) : Query

}

