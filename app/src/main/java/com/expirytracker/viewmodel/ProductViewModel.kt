package com.expirytracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.expirytracker.data.Product
import com.expirytracker.data.ProductDatabase
import com.expirytracker.data.ProductRepository
import com.expirytracker.utils.CalendarHelper
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository
    private val calendarHelper: CalendarHelper = CalendarHelper(application)
    val allProducts: LiveData<List<Product>>

    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        allProducts = repository.allProducts
    }

    fun insertProduct(product: Product, onComplete: (Boolean) -> Unit) = viewModelScope.launch {
        try {
            val eventId = calendarHelper.addEventToCalendar(
                product.name,
                product.expiryDate,
                product.reminderDays * 24 * 60,
                product.reminderMethod,
                product.reminderHour,
                product.reminderMinute
            )

            val productWithEvent = product.copy(calendarEventId = eventId)
            repository.insert(productWithEvent)
            onComplete(true)
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(false)
        }
    }

    fun deleteProduct(product: Product, onComplete: (Boolean) -> Unit) = viewModelScope.launch {
        try {
            product.calendarEventId?.let {
                calendarHelper.deleteEventFromCalendar(it)
            }
            repository.delete(product)
            onComplete(true)
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(false)
        }
    }

    fun hasCalendarPermission(): Boolean {
        return calendarHelper.hasCalendarPermission()
    }
}
