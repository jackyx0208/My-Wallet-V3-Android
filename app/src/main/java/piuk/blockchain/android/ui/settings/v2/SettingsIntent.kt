package piuk.blockchain.android.ui.settings.v2

import com.blockchain.commonarch.presentation.mvi.MviIntent
import com.blockchain.nabu.BasicProfileInfo
import com.blockchain.nabu.Tier
import piuk.blockchain.android.domain.usecases.AvailablePaymentMethodType

sealed class SettingsIntent : MviIntent<SettingsState> {

    object LoadHeaderInformation : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState
    }

    object LoadPaymentMethods : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState
    }

    class UpdateContactSupportEligibility(
        private val tier: Tier,
        private val userInformation: BasicProfileInfo? = null
    ) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(
                basicProfileInfo = userInformation,
                tier = tier
            )
    }

    class UpdatePaymentMethodsInfo(
        private val paymentMethodInfo: PaymentMethods
    ) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(
                paymentMethodInfo = paymentMethodInfo
            )
    }

    class UpdateAvailablePaymentMethods(private val available: List<AvailablePaymentMethodType>) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(
                paymentMethodInfo = oldState.paymentMethodInfo?.copy(
                    availablePaymentMethodTypes = available
                )
            )
    }

    object AddBankTransferSelected : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState
    }

    object AddBankAccountSelected : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState
    }

    object Logout : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState
    }

    object UserLoggedOut : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(
                hasWalletUnpaired = true
            )
    }

    class UpdateViewToLaunch(private val viewToLaunch: ViewToLaunch) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState.copy(viewToLaunch = viewToLaunch)
    }

    object ResetViewState : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState = oldState.copy(viewToLaunch = ViewToLaunch.None)
    }

    class OnBankRemoved(private val bankId: String) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState {
            val updatedBankList = oldState.paymentMethodInfo?.linkedBanks?.toMutableList()?.apply {
                removeIf { it.bank.id == bankId }
            } ?: emptyList()

            return oldState.copy(
                paymentMethodInfo = oldState.paymentMethodInfo?.copy(
                    linkedBanks = updatedBankList
                )
            )
        }
    }

    class OnCardRemoved(private val cardId: String) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState {
            val updatedCardList = oldState.paymentMethodInfo?.linkedCards?.toMutableList()?.apply {
                removeIf { it.id == cardId }
            } ?: emptyList()

            return oldState.copy(
                paymentMethodInfo = oldState.paymentMethodInfo?.copy(
                    linkedCards = updatedCardList.toList()
                )
            )
        }
    }

    class UpdateErrorState(private val error: SettingsError) : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(error = error)
    }

    object ResetErrorState : SettingsIntent() {
        override fun reduce(oldState: SettingsState): SettingsState =
            oldState.copy(error = SettingsError.NONE)
    }
}
