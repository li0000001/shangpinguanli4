package com.expirytracker.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expirytracker.data.Product
import com.expirytracker.data.ReminderMethod
import com.expirytracker.databinding.ItemProductBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ProductAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onItemClick: (Product) -> Unit,
        private val onDeleteClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                textProductName.text = product.name

                val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE)
                textExpiryDate.text = "到期日期：${dateFormat.format(Date(product.expiryDate))}"

                val daysUntilExpiry = TimeUnit.MILLISECONDS.toDays(
                    product.expiryDate - System.currentTimeMillis()
                ).toInt()

                when {
                    daysUntilExpiry < 0 -> {
                        textDaysLeft.text = "已过期 ${-daysUntilExpiry} 天"
                        textDaysLeft.setTextColor(Color.parseColor("#D32F2F"))
                        cardProduct.setCardBackgroundColor(Color.parseColor("#FFEBEE"))
                    }
                    daysUntilExpiry == 0 -> {
                        textDaysLeft.text = "今天到期"
                        textDaysLeft.setTextColor(Color.parseColor("#F57C00"))
                        cardProduct.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
                    }
                    daysUntilExpiry <= product.reminderDays -> {
                        textDaysLeft.text = "还剩 $daysUntilExpiry 天"
                        textDaysLeft.setTextColor(Color.parseColor("#F57C00"))
                        cardProduct.setCardBackgroundColor(Color.parseColor("#FFFDE7"))
                    }
                    else -> {
                        textDaysLeft.text = "还剩 $daysUntilExpiry 天"
                        textDaysLeft.setTextColor(Color.parseColor("#388E3C"))
                        cardProduct.setCardBackgroundColor(Color.WHITE)
                    }
                }

                val reminderMethodName = ReminderMethod.getMethodName(product.reminderMethod)
                val reminderTimeText = if (product.reminderHour != null && product.reminderMinute != null) {
                    val timeStr = ReminderMethod.formatTime(product.reminderHour, product.reminderMinute)
                    " · $timeStr"
                } else {
                    ""
                }
                textReminderDays.text = "提前 ${product.reminderDays} 天提醒 · $reminderMethodName$reminderTimeText"

                root.setOnClickListener { onItemClick(product) }
                buttonDelete.setOnClickListener { onDeleteClick(product) }
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
