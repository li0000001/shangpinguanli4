package com.expirytracker.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.expirytracker.data.Product
import com.expirytracker.data.ReminderMethod
import com.expirytracker.databinding.ActivityAddProductBinding
import com.expirytracker.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: ProductViewModel
    
    private var productionDate: Calendar? = null
    private var expiryDate: Calendar? = null
    private var selectedReminderMethod: Int = ReminderMethod.ALERT
    private var selectedReminderHour: Int? = null
    private var selectedReminderMinute: Int? = null
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "添加商品"

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSelectProductionDate.setOnClickListener {
            showDatePicker { date ->
                productionDate = date
                binding.textProductionDate.text = dateFormat.format(date.time)
                updateExpiryDateIfPossible()
            }
        }

        binding.buttonSelectExpiryDate.setOnClickListener {
            showDatePicker { date ->
                expiryDate = date
                binding.textExpiryDate.text = dateFormat.format(date.time)
            }
        }

        binding.editShelfLifeDays.addTextChangedListener {
            updateExpiryDateIfPossible()
        }

        binding.radioGroupReminderMethod.setOnCheckedChangeListener { _, checkedId ->
            selectedReminderMethod = when (checkedId) {
                binding.radioReminderAlert.id -> ReminderMethod.ALERT
                binding.radioReminderAlarm.id -> ReminderMethod.ALARM
                else -> ReminderMethod.ALERT
            }
            updateReminderTimeVisibility()
        }

        binding.buttonSelectReminderTime.setOnClickListener {
            showTimePicker()
        }

        binding.buttonSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun updateExpiryDateIfPossible() {
        val prodDate = productionDate ?: return
        val shelfLifeStr = binding.editShelfLifeDays.text.toString()
        if (shelfLifeStr.isEmpty()) return

        try {
            val shelfLifeDays = shelfLifeStr.toInt()
            val calculatedExpiry = Calendar.getInstance().apply {
                timeInMillis = prodDate.timeInMillis
                add(Calendar.DAY_OF_YEAR, shelfLifeDays)
            }
            expiryDate = calculatedExpiry
            binding.textExpiryDate.text = "自动计算：${dateFormat.format(calculatedExpiry.time)}"
        } catch (e: NumberFormatException) {
        }
    }

    private fun showDatePicker(onDateSelected: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = selectedReminderHour ?: 9
        val currentMinute = selectedReminderMinute ?: 0
        
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedReminderHour = hourOfDay
                selectedReminderMinute = minute
                binding.textReminderTime.text = String.format("%02d:%02d", hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    private fun updateReminderTimeVisibility() {
        if (selectedReminderMethod == ReminderMethod.ALARM) {
            binding.layoutReminderTime.visibility = View.VISIBLE
        } else {
            binding.layoutReminderTime.visibility = View.GONE
            selectedReminderHour = null
            selectedReminderMinute = null
        }
    }

    private fun saveProduct() {
        val name = binding.editProductName.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入商品名称", Toast.LENGTH_SHORT).show()
            return
        }

        val finalExpiryDate = expiryDate
        if (finalExpiryDate == null) {
            Toast.makeText(this, "请选择保质日期或输入生产日期和保质期天数", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedReminderMethod == ReminderMethod.ALARM && (selectedReminderHour == null || selectedReminderMinute == null)) {
            Toast.makeText(this, "请设置闹钟提醒时间", Toast.LENGTH_SHORT).show()
            return
        }

        val reminderDaysStr = binding.editReminderDays.text.toString()
        val reminderDays = if (reminderDaysStr.isEmpty()) 3 else reminderDaysStr.toIntOrNull() ?: 3

        val shelfLifeDaysStr = binding.editShelfLifeDays.text.toString()
        val shelfLifeDays = if (shelfLifeDaysStr.isNotEmpty()) shelfLifeDaysStr.toIntOrNull() else null

        val product = Product(
            name = name,
            productionDate = productionDate?.timeInMillis,
            shelfLifeDays = shelfLifeDays,
            expiryDate = finalExpiryDate.timeInMillis,
            reminderDays = reminderDays,
            reminderMethod = selectedReminderMethod,
            reminderHour = selectedReminderHour,
            reminderMinute = selectedReminderMinute
        )

        if (!viewModel.hasCalendarPermission()) {
            Toast.makeText(this, "需要日历权限才能添加提醒", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.buttonSave.isEnabled = false
        viewModel.insertProduct(product) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "添加失败，请重试", Toast.LENGTH_SHORT).show()
                    binding.buttonSave.isEnabled = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
