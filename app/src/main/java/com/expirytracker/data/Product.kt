package com.expirytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val productionDate: Long? = null,
    val shelfLifeDays: Int? = null,
    val expiryDate: Long,
    val reminderDays: Int = 3,
    val calendarEventId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
