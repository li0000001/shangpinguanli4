package com.expirytracker.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.expirytracker.R
import com.expirytracker.databinding.ActivityMainBinding
import com.expirytracker.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    private val calendarPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Toast.makeText(this, "日历权限已授予", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "需要日历权限才能添加提醒", Toast.LENGTH_LONG).show()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "通知权限已授予", Toast.LENGTH_SHORT).show()
        }
    }

    private val exactAlarmSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (viewModel.canScheduleExactAlarms()) {
            Toast.makeText(this, "精确闹钟权限已授予", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "需要精确闹钟权限才能使用闹钟提醒功能", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        setupRecyclerView()
        setupFab()
        observeProducts()
        requestPermissions()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onItemClick = { product ->
            },
            onDeleteClick = { product ->
                showDeleteConfirmDialog(product)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            if (!viewModel.hasCalendarPermission()) {
                requestCalendarPermissions()
            } else {
                startActivity(Intent(this, AddProductActivity::class.java))
            }
        }
    }

    private fun observeProducts() {
        viewModel.allProducts.observe(this) { products ->
            adapter.submitList(products)
            
            if (products.isEmpty()) {
                binding.textEmptyState.visibility = android.view.View.VISIBLE
                binding.recyclerView.visibility = android.view.View.GONE
            } else {
                binding.textEmptyState.visibility = android.view.View.GONE
                binding.recyclerView.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun showDeleteConfirmDialog(product: com.expirytracker.data.Product) {
        AlertDialog.Builder(this)
            .setTitle("删除商品")
            .setMessage("确定要删除「${product.name}」吗？\n日历中的提醒事件也会被删除。")
            .setPositiveButton("删除") { _, _ ->
                viewModel.deleteProduct(product) { success ->
                    if (success) {
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun requestPermissions() {
        requestCalendarPermissions()
        requestNotificationPermission()
        requestExactAlarmPermission()
    }

    private fun requestCalendarPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )

        val needsPermission = permissions.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needsPermission) {
            calendarPermissionLauncher.launch(permissions)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!viewModel.canScheduleExactAlarms()) {
                AlertDialog.Builder(this)
                    .setTitle("需要精确闹钟权限")
                    .setMessage("为了让闹钟提醒功能正常工作，需要授予精确闹钟权限。\n\n请在接下来的设置页面中，允许本应用设置精确闹钟和提醒。")
                    .setPositiveButton("去设置") { _, _ ->
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = Uri.parse("package:$packageName")
                        }
                        exactAlarmSettingsLauncher.launch(intent)
                    }
                    .setNegativeButton("稍后", null)
                    .show()
            }
        }
    }
}
