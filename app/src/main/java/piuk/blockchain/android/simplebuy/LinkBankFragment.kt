package piuk.blockchain.android.simplebuy

import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blockchain.koin.scopedInject
import com.blockchain.ui.urllinks.YODLEE_LEARN_MORE
import kotlinx.android.synthetic.main.fragment_link_a_bank.*
import org.koin.android.ext.android.inject
import piuk.blockchain.android.R
import piuk.blockchain.android.ui.base.mvi.MviFragment
import piuk.blockchain.android.ui.base.setupToolbar
import piuk.blockchain.android.util.StringUtils
import piuk.blockchain.androidcore.utils.helperfunctions.unsafeLazy
import piuk.blockchain.androidcoreui.utils.extensions.gone
import piuk.blockchain.androidcoreui.utils.extensions.inflate
import piuk.blockchain.androidcoreui.utils.extensions.visible

class LinkBankFragment : MviFragment<SimpleBuyModel, SimpleBuyIntent, SimpleBuyState>(), SimpleBuyScreen {

    override val model: SimpleBuyModel by scopedInject()
    private val stringUtils: StringUtils by inject()

    private val accountProviderId: String by lazy {
        arguments?.getString(ACCOUNT_PROVIDER_ID) ?: ""
    }

    private val errorState: ErrorState? by unsafeLazy {
        arguments?.getSerializable(ERROR_STATE) as? ErrorState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = container?.inflate(R.layout.fragment_link_a_bank)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && accountProviderId.isNotEmpty()) {
            model.process(SimpleBuyIntent.UpdateAccountProvider(accountProviderId))
        }
        activity.setupToolbar(R.string.link_a_bank, false)
    }

    override fun render(newState: SimpleBuyState) {

        if (newState.isLoading) {
            showLinkingInProgress()
        }

        val error = newState.errorState ?: errorState
        error?.let {
            showErrorState(it)
        }

        if (!newState.isLoading && error == null) {
            newState.selectedPaymentMethod?.label?.let {
                if (it.isNotEmpty()) {
                    showLinkingSuccess(it)
                }
            }
        }
    }

    private fun showErrorState(state: ErrorState) {
        when (state) {
            ErrorState.BankLinkingTimeout -> {
                analytics.logEvent(bankLinkingGenericError(BankPartnerTypes.ACH.name))
                link_bank_btn.text = getString(R.string.common_try_again)
                link_bank_title.text = getString(R.string.yodlee_linking_generic_error_title)
                link_bank_subtitle.text = getString(R.string.yodlee_linking_timeout_error_subtitle)
            }
            ErrorState.LinkedBankAlreadyLinked -> {
                analytics.logEvent(
                    bankLinkingAlreadyLinked(BankPartnerTypes.ACH.name))
                link_bank_btn.text = getString(R.string.yodlee_linking_try_different_bank)
                link_bank_title.text = getString(R.string.yodlee_linking_is_this_your_bank)

                val linksMap = mapOf<String, Uri>(
                    "yodlee_names_dont_match_learn_more" to Uri.parse(YODLEE_LEARN_MORE)
                )

                val tosText = stringUtils.getStringWithMappedAnnotations(
                    R.string.yodlee_linking_already_linked_error_subtitle,
                    linksMap,
                    activity
                )

                link_bank_subtitle.text = tosText
                link_bank_subtitle.movementMethod = LinkMovementMethod.getInstance()
            }
            ErrorState.LinkedBankAccountUnsupported -> {
                analytics.logEvent(
                    bankLinkingIncorrectAccount(BankPartnerTypes.ACH.name))
                link_bank_btn.text = getString(R.string.yodlee_linking_try_different_bank)
                link_bank_title.text = getString(R.string.yodlee_linking_checking_error_title)
                link_bank_subtitle.text = getString(R.string.yodlee_linking_checking_error_subtitle)
            }
            else -> {
                analytics.logEvent(bankLinkingGenericError(BankPartnerTypes.ACH.name))
                link_bank_btn.text = getString(R.string.common_try_again)
                link_bank_title.text = getString(R.string.yodlee_linking_generic_error_title)
                link_bank_subtitle.text = getString(R.string.yodlee_linking_generic_error_subtitle)
            }
        }

        link_bank_icon.setImageResource(R.drawable.ic_bank_details_big)
        link_bank_progress.gone()
        link_bank_state_indicator.setImageResource(R.drawable.ic_alert_white_bkgd)
        link_bank_state_indicator.visible()
        link_bank_btn.visible()
        link_bank_btn.setOnClickListener {
            logRetryAnalytics(state)
            navigator().pop()
        }
        link_bank_cancel.visible()
        link_bank_cancel.setOnClickListener {
            logCancelAnalytics(state)
            navigator().exitSimpleBuyFlow()
        }
    }

    private fun logRetryAnalytics(state: ErrorState) {
        when (state) {
            ErrorState.LinkedBankAlreadyLinked -> analytics.logEvent(
                bankLinkingAlreadyCtaRetry(BankPartnerTypes.ACH.name))
            ErrorState.LinkedBankAccountUnsupported -> analytics.logEvent(
                bankLinkingIncorrectCtaRetry(BankPartnerTypes.ACH.name))
            else -> analytics.logEvent(bankLinkingGenericErrorCtaRetry(BankPartnerTypes.ACH.name))
        }
    }

    private fun logCancelAnalytics(state: ErrorState) {
        when (state) {
            ErrorState.LinkedBankAlreadyLinked -> analytics.logEvent(
                bankLinkingAlreadyCtaCancel(BankPartnerTypes.ACH.name))
            ErrorState.LinkedBankAccountUnsupported -> analytics.logEvent(
                bankLinkingIncorrectCtaCancel(BankPartnerTypes.ACH.name))
            else -> analytics.logEvent(bankLinkingGenericErrorCtaCancel(BankPartnerTypes.ACH.name))
        }
    }

    private fun showLinkingInProgress() {
        link_bank_icon.setImageResource(R.drawable.ic_blockchain_blue_circle)
        link_bank_progress.visible()
        link_bank_state_indicator.gone()
        link_bank_btn.gone()
        link_bank_title.text = getString(R.string.yodlee_linking_title)
        link_bank_subtitle.text = getString(R.string.yodlee_linking_subtitle)
    }

    private fun showLinkingSuccess(label: String) {
        analytics.logEvent(bankLinkingSuccess(BankPartnerTypes.ACH.name))

        link_bank_icon.setImageResource(R.drawable.ic_bank_details_big)
        link_bank_progress.gone()
        link_bank_state_indicator.setImageResource(R.drawable.ic_check_circle)
        link_bank_state_indicator.visible()
        link_bank_btn.visible()
        link_bank_btn.setOnClickListener {
            navigator().goToCheckOutScreen(false)
        }
        link_bank_title.text = getString(R.string.yodlee_linking_success_title)
        link_bank_subtitle.text = getString(R.string.yodlee_linking_success_subtitle, label)
    }

    override fun navigator(): SimpleBuyNavigator =
        (activity as? SimpleBuyNavigator)
            ?: throw IllegalStateException("Parent must implement SimpleBuyNavigator")

    override fun onBackPressed(): Boolean = true

    companion object {
        private const val ACCOUNT_PROVIDER_ID = "ACCOUNT_PROVIDER_ID"
        private const val ERROR_STATE = "ERROR_STATE"

        fun newInstance(accountProviderId: String) = LinkBankFragment().apply {
            arguments = Bundle().apply {
                putString(ACCOUNT_PROVIDER_ID, accountProviderId)
            }
        }

        fun newInstance(errorState: ErrorState) = LinkBankFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ERROR_STATE, errorState)
            }
        }
    }
}