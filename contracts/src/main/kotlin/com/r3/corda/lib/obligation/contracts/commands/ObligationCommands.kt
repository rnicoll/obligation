package com.r3.corda.lib.obligation.contracts.commands

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.r3.corda.lib.obligation.contracts.types.PaymentReference
import com.r3.corda.lib.tokens.contracts.types.TokenType
import net.corda.core.contracts.Amount
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.math.BigDecimal
import java.time.Instant

/** All the things you can do with an obligation. */
interface ObligationCommands : CommandData {

    /** Create a new obligation. */
    class Create : ObligationCommands, TypeOnlyCommandData()

    /** Change the details of an obligation. */
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes(
            JsonSubTypes.Type(value = Novate.UpdateFaceAmountQuantity::class, name = "quantity"),
            JsonSubTypes.Type(value = Novate.UpdateFaceAmountToken::class, name = "token"),
            JsonSubTypes.Type(value = Novate.UpdateDueBy::class, name = "dueBy"),
            JsonSubTypes.Type(value = Novate.UpdateParty::class, name = "party")
    )
    sealed class Novate : ObligationCommands {

        /** Change the face value quantity of the obligation. */
        data class UpdateFaceAmountQuantity(val newAmount: Amount<TokenType>) : ObligationCommands.Novate()

        /** Change the face amount token of the obligation. This involves an fx conversion. */
        data class UpdateFaceAmountToken<OLD : TokenType, NEW : TokenType>(
                val oldToken: OLD,
                val newToken: NEW,
                val oracle: Party,
                val fxRate: BigDecimal? = null
        ) : ObligationCommands.Novate()

        /** Change the due by date. */
        data class UpdateDueBy(val newDueBy: Instant) : ObligationCommands.Novate()

        /** Change one of the parties. */
        data class UpdateParty(val oldParty: AbstractParty, val newParty: AbstractParty) : ObligationCommands.Novate()
    }

    /** Add or update the settlement method. */
    class UpdateSettlementMethod : ObligationCommands, TypeOnlyCommandData()

    /** Record that a payment was made in respect of an obligation. */
    data class AddPayment(val ref: PaymentReference) : ObligationCommands

    /** Update the settlement status of a payment. */
    data class UpdatePayment(val ref: PaymentReference) : ObligationCommands

    /** Cancel the obligation - involves exiting the obligation state from the ledger. */
    data class Cancel(val id: UniqueIdentifier) : ObligationCommands, TypeOnlyCommandData()
}