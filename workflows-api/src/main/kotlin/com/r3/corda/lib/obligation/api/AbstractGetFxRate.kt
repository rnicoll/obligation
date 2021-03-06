package com.r3.corda.lib.obligation.api

import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow

@InitiatingFlow
abstract class AbstractGetFxRate : FlowLogic<FxRateResponse>()