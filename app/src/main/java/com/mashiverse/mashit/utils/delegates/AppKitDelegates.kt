package com.mashiverse.mashit.utils.delegates

import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.data.models.sys.wallet.WalletType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal

fun createAppKitDelegate(
    onSessionApproved: (WalletPreferences) -> Unit,
    onSessionRejected: () -> Unit
) = object : AppKit.ModalDelegate {
    override fun onSessionApproved(approvedSession: Modal.Model.ApprovedSession) {
        val walletPreferences = when (approvedSession) {
            is Modal.Model.ApprovedSession.CoinbaseSession -> {
                WalletPreferences(
                    wallet = approvedSession.address
                )
            }

            is Modal.Model.ApprovedSession.WalletConnectSession -> {
                WalletPreferences(
                    wallet = AppKit.getAccount()?.address,
                    walletType = WalletType.MM
                )
            }
        }

        onSessionApproved.invoke(walletPreferences)
    }

    override fun onSessionRejected(rejectedSession: Modal.Model.RejectedSession) {
        onSessionRejected.invoke()
    }

    override fun onSessionUpdate(updatedSession: Modal.Model.UpdatedSession) {}

    override fun onSessionEvent(sessionEvent: Modal.Model.SessionEvent) {}

    override fun onSessionExtend(session: Modal.Model.Session) {}

    override fun onSessionDelete(deletedSession: Modal.Model.DeletedSession) {}

    override fun onSessionRequestResponse(response: Modal.Model.SessionRequestResponse) {}

    override fun onProposalExpired(proposal: Modal.Model.ExpiredProposal) {}

    override fun onRequestExpired(request: Modal.Model.ExpiredRequest) {}

    override fun onConnectionStateChange(state: Modal.Model.ConnectionState) {}

    override fun onError(error: Modal.Model.Error) {}
}