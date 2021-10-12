package piuk.blockchain.android.ui.locks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blockchain.core.payments.model.WithdrawalLock
import com.blockchain.utils.capitalizeFirstChar
import piuk.blockchain.android.databinding.LockItemBinding
import piuk.blockchain.android.ui.adapters.AdapterDelegate

class LockItemDelegate : AdapterDelegate<WithdrawalLock> {

    override fun isForViewType(items: List<WithdrawalLock>, position: Int): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        LockItemViewHolder(
            LockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(
        items: List<WithdrawalLock>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) = (holder as LockItemViewHolder).bind(items[position])
}

private class LockItemViewHolder(
    private val binding: LockItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(lock: WithdrawalLock) {
        with(binding) {
            dateLock.text = "${lock.date.month.name.capitalizeFirstChar()} ${lock.date.dayOfMonth}"
            amountLock.text = lock.amount.toStringWithSymbol()
        }
    }
}