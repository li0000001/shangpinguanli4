package com.expirytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val productionDate: Long? = null,
    val shelfLifeDays: Int? = null,
    val expiryDate: Long,
    val reminderDays: Int = 3,
    val reminderMethod: Int = ReminderMethod.ALERT,
    val calendarEventId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

object ReminderMethod {
    const val ALERT = 1  // 通知提醒
    const val ALARM = 4  // 闹钟提醒
    
    fun getMethodName(method: Int): String {
        return when (method) {
            ALERT -> "通知提醒"
            ALARM -> "闹钟提醒"
            else -> "通知提醒"
        }
    }
}
