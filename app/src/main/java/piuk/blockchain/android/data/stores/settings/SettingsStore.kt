package piuk.blockchain.android.data.stores.settings

import info.blockchain.wallet.api.data.Settings
import io.reactivex.Observable
import piuk.blockchain.android.data.stores.Optional

interface SettingsStore {

    fun getSettings(): Observable<Optional<Settings>>

}