package com.expirytracker.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.util.*

class CalendarHelper(private val context: Context) {

    fun hasCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun addEventToCalendar(
        productName: String,
        expiryDate: Long,
        reminderMinutes: Int = 4320,
        reminderMethod: Int = 1,
        reminderHour: Int? = null,
        reminderMinute: Int? = null
    ): Long? {
        if (!hasCalendarPermission()) {
            return null
        }

        try {
            val calendarId = getCalendarId() ?: return null

            val eventValues = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, expiryDate)
                put(CalendarContract.Events.DTEND, expiryDate + 3600000)
                put(CalendarContract.Events.TITLE, "⏰ $productName 即将过期")
                put(CalendarContract.Events.DESCRIPTION, "商品【$productName】将在今天到期，请及时查看。")
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                put(CalendarContract.Events.HAS_ALARM, 1)
            }

            val eventUri = context.contentResolver.insert(
                CalendarContract.Events.CONTENT_URI,
                eventValues
            )

            val eventId = eventUri?.lastPathSegment?.toLongOrNull()

            eventId?.let {
                val adjustedReminderMinutes = if (reminderHour != null && reminderMinute != null) {
                    calculateReminderMinutes(expiryDate, reminderMinutes, reminderHour, reminderMinute)
                } else {
                    reminderMinutes
                }
                addReminderToEvent(it, adjustedReminderMinutes, reminderMethod)
            }

            return eventId
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun calculateReminderMinutes(
        expiryDate: Long,
        reminderDaysInMinutes: Int,
        reminderHour: Int,
        reminderMinute: Int
    ): Int {
        val expiryCalendar = Calendar.getInstance().apply {
            timeInMillis = expiryDate
        }
        
        val reminderCalendar = Calendar.getInstance().apply {
            timeInMillis = expiryDate
            add(Calendar.MINUTE, -reminderDaysInMinutes)
            set(Calendar.HOUR_OF_DAY, reminderHour)
            set(Calendar.MINUTE, reminderMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val diffMillis = expiryCalendar.timeInMillis - reminderCalendar.timeInMillis
        return (diffMillis / (60 * 1000)).toInt()
    }

    private fun addReminderToEvent(eventId: Long, reminderMinutes: Int, reminderMethod: Int) {
        try {
            val reminderValues = ContentValues().apply {
                put(CalendarContract.Reminders.EVENT_ID, eventId)
                put(CalendarContract.Reminders.MINUTES, reminderMinutes)
                put(CalendarContract.Reminders.METHOD, reminderMethod)
            }

            context.contentResolver.insert(
                CalendarContract.Reminders.CONTENT_URI,
                reminderValues
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteEventFromCalendar(eventId: Long): Boolean {
        if (!hasCalendarPermission()) {
            return false
        }

        return try {
            val deleteUri = CalendarContract.Events.CONTENT_URI
            val deletedRows = context.contentResolver.delete(
                deleteUri,
                "${CalendarContract.Events._ID} = ?",
                arrayOf(eventId.toString())
            )
            deletedRows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getCalendarId(): Long? {
        try {
            val projection = arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
            )

            val cursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                "${CalendarContract.Calendars.VISIBLE} = 1",
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val idIndex = it.getColumnIndex(CalendarContract.Calendars._ID)
                    if (idIndex >= 0) {
                        return it.getLong(idIndex)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
