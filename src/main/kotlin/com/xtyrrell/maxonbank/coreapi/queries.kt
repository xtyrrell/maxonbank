package com.xtyrrell.maxonbank.coreapi

import java.util.*

// Main queries
// TODO: Rename queries to remove verb (I think?)

data class GetAccountAvailableBalanceQuery(
    val accountId: UUID,
)

data class ListAccountHistoryQuery(
    val accountId: UUID,
)

class GetTotalAvailableBalanceQuery

// Other queries
class ListAccountsQuery

data class GetAccountDetailsQuery(
    val accountId: UUID
)
