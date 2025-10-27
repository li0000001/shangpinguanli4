# 闹钟提醒时间功能说明

## 📱 功能概述

在现有的提醒方式功能基础上，新增了**设置闹钟提醒时间**的功能。当用户选择"闹钟提醒"方式时，可以指定具体的提醒时间（如：上午9:00），而不是在午夜时分提醒。

## 🎯 功能特点

- **条件性显示**：只有选择"闹钟提醒"时，才显示时间选择器
- **必填验证**：选择闹钟提醒后，必须设置提醒时间才能保存
- **智能计算**：系统会根据到期日期、提前天数和指定时间，精确计算提醒时刻
- **友好显示**：在商品列表中显示设置的提醒时间（如：09:00）

## 🔧 技术实现

### 1. 数据模型更新

**Product.kt** - 新增字段：
```kotlin
@Entity(tableName = "products")
data class Product(
    // ... 其他字段
    val reminderHour: Int? = null,      // 提醒时间：小时 (0-23)
    val reminderMinute: Int? = null,    // 提醒时间：分钟 (0-59)
    // ...
)

object ReminderMethod {
    // ... 其他方法
    
    // 新增：格式化时间显示
    fun formatTime(hour: Int?, minute: Int?): String {
        if (hour == null || minute == null) return ""
        return String.format("%02d:%02d", hour, minute)
    }
}
```

### 2. 数据库迁移

**ProductDatabase.kt** - 版本升级 v2 → v3：
```kotlin
@Database(entities = [Product::class], version = 3)
abstract class ProductDatabase : RoomDatabase() {
    
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE products ADD COLUMN reminderHour INTEGER")
            database.execSQL("ALTER TABLE products ADD COLUMN reminderMinute INTEGER")
        }
    }
    
    // 添加到迁移列表
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
}
```

### 3. UI界面更新

**activity_add_product.xml** - 新增时间选择器：
```xml
<LinearLayout
    android:id="@+id/layoutReminderTime"
    android:visibility="gone">  <!-- 默认隐藏 -->
    
    <TextView android:text="提醒时间" />
    
    <Button
        android:id="@+id/buttonSelectReminderTime"
        android:text="选择提醒时间" />
    
    <TextView
        android:id="@+id/textReminderTime"
        android:text="未设置" />
        
</LinearLayout>
```

### 4. 业务逻辑更新

**AddProductActivity.kt** - 核心功能：

```kotlin
// 状态变量
private var selectedReminderHour: Int? = null
private var selectedReminderMinute: Int? = null

// 监听提醒方式变化
binding.radioGroupReminderMethod.setOnCheckedChangeListener { _, checkedId ->
    selectedReminderMethod = when (checkedId) {
        binding.radioReminderAlert.id -> ReminderMethod.ALERT
        binding.radioReminderAlarm.id -> ReminderMethod.ALARM
        else -> ReminderMethod.ALERT
    }
    updateReminderTimeVisibility()  // 动态显示/隐藏时间选择器
}

// 显示/隐藏时间选择器
private fun updateReminderTimeVisibility() {
    if (selectedReminderMethod == ReminderMethod.ALARM) {
        binding.layoutReminderTime.visibility = View.VISIBLE
    } else {
        binding.layoutReminderTime.visibility = View.GONE
        selectedReminderHour = null
        selectedReminderMinute = null
    }
}

// 时间选择器
private fun showTimePicker() {
    TimePickerDialog(
        this,
        { _, hourOfDay, minute ->
            selectedReminderHour = hourOfDay
            selectedReminderMinute = minute
            binding.textReminderTime.text = String.format("%02d:%02d", hourOfDay, minute)
        },
        9, 0, true  // 默认 9:00, 24小时制
    ).show()
}

// 保存验证
private fun saveProduct() {
    // ... 其他验证
    
    if (selectedReminderMethod == ReminderMethod.ALARM && 
        (selectedReminderHour == null || selectedReminderMinute == null)) {
        Toast.makeText(this, "请设置闹钟提醒时间", Toast.LENGTH_SHORT).show()
        return
    }
    
    val product = Product(
        // ... 其他字段
        reminderHour = selectedReminderHour,
        reminderMinute = selectedReminderMinute
    )
}
```

### 5. 日历事件时间计算

**CalendarHelper.kt** - 精确计算提醒时间：

```kotlin
fun addEventToCalendar(
    productName: String,
    expiryDate: Long,
    reminderMinutes: Int = 4320,
    reminderMethod: Int = 1,
    reminderHour: Int? = null,      // 新增参数
    reminderMinute: Int? = null     // 新增参数
): Long? {
    // ... 创建日历事件
    
    eventId?.let {
        val adjustedReminderMinutes = if (reminderHour != null && reminderMinute != null) {
            calculateReminderMinutes(expiryDate, reminderMinutes, reminderHour, reminderMinute)
        } else {
            reminderMinutes
        }
        addReminderToEvent(it, adjustedReminderMinutes, reminderMethod)
    }
}

// 精确计算提醒时间
private fun calculateReminderMinutes(
    expiryDate: Long,
    reminderDaysInMinutes: Int,
    reminderHour: Int,
    reminderMinute: Int
): Int {
    val expiryCalendar = Calendar.getInstance().apply {
        timeInMillis = expiryDate
    }
    
    // 从到期日期往前推指定天数，然后设置为指定时间
    val reminderCalendar = Calendar.getInstance().apply {
        timeInMillis = expiryDate
        add(Calendar.MINUTE, -reminderDaysInMinutes)  // 往前推N天
        set(Calendar.HOUR_OF_DAY, reminderHour)       // 设置小时
        set(Calendar.MINUTE, reminderMinute)          // 设置分钟
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    
    // 计算到期时间和提醒时间的分钟差
    val diffMillis = expiryCalendar.timeInMillis - reminderCalendar.timeInMillis
    return (diffMillis / (60 * 1000)).toInt()
}
```

### 6. 商品列表显示

**ProductAdapter.kt** - 显示提醒时间：

```kotlin
fun bind(product: Product) {
    // ... 其他绑定
    
    val reminderMethodName = ReminderMethod.getMethodName(product.reminderMethod)
    val reminderTimeText = if (product.reminderHour != null && product.reminderMinute != null) {
        val timeStr = ReminderMethod.formatTime(product.reminderHour, product.reminderMinute)
        " · $timeStr"
    } else {
        ""
    }
    textReminderDays.text = "提前 ${product.reminderDays} 天提醒 · $reminderMethodName$reminderTimeText"
}
```

## 📝 使用说明

### 添加商品时设置闹钟提醒时间

1. 打开"添加商品"界面
2. 填写商品名称和保质期信息
3. 在"提醒方式"中选择 **⏰ 闹钟提醒（带声音警报）**
4. 自动显示"提醒时间"选择区域
5. 点击"选择提醒时间"按钮
6. 在时间选择器中选择时间（如：09:00）
7. 点击"保存"按钮

### 显示效果

**商品列表中**：
```
提前 3 天提醒 · 闹钟提醒 · 09:00
```

**时间选择器**：
- 24小时制
- 默认时间：09:00
- 可精确到分钟

## 🎯 功能示例

### 场景 1：药品到期提醒

- 商品名称：感冒药
- 到期日期：2024-02-10 00:00:00
- 提前天数：3 天
- 提醒方式：闹钟提醒
- 提醒时间：09:00

**计算结果**：
- 提醒触发时间：2024-02-07 09:00:00
- 效果：在到期前3天的早上9点，手机会响起闹钟提醒

### 场景 2：食品到期提醒

- 商品名称：牛奶
- 到期日期：2024-02-15 00:00:00
- 提前天数：1 天
- 提醒方式：闹钟提醒
- 提醒时间：20:00

**计算结果**：
- 提醒触发时间：2024-02-14 20:00:00
- 效果：在到期前1天的晚上8点，手机会响起闹钟提醒

## ✅ 完成清单

- [x] Product 数据模型添加 reminderHour 和 reminderMinute 字段
- [x] 数据库迁移 v2 → v3
- [x] UI 添加时间选择器（条件性显示）
- [x] AddProductActivity 实现时间选择逻辑
- [x] CalendarHelper 实现精确时间计算
- [x] ProductViewModel 传递时间参数
- [x] ProductAdapter 显示提醒时间
- [x] 添加必填验证（选择闹钟提醒时）
- [x] 向后兼容（reminderHour/reminderMinute 为可选字段）

## 🔍 技术亮点

1. **条件性显示**：根据提醒方式动态显示/隐藏时间选择器
2. **精确计算**：准确计算提醒时间，避免时区和时间偏移问题
3. **数据兼容**：使用可空类型，确保与现有数据兼容
4. **用户体验**：提供明确的提示和验证，避免用户遗漏设置

## 🐛 已知限制

1. **通知提醒**：选择"通知提醒"时，不支持设置提醒时间（保持原有行为）
2. **时间精度**：只能设置到分钟级别，不支持秒级精度
3. **单次提醒**：每个商品只能设置一个提醒时间

## 🔮 未来增强建议

1. 支持多个提醒时间（如：早、中、晚各一次）
2. 提醒时间模板（如：常用时间快捷选择）
3. 重复提醒设置（如：每天同一时间提醒）
4. 通知提醒也支持时间设置

## 📊 数据迁移说明

### 升级路径

- **从 v1 升级**：先执行 MIGRATION_1_2，再执行 MIGRATION_2_3
- **从 v2 升级**：只执行 MIGRATION_2_3
- **新安装**：直接使用 v3 架构

### 现有数据兼容性

- 所有现有商品的 reminderHour 和 reminderMinute 默认为 NULL
- 不影响现有商品的提醒功能
- 新添加的商品可以选择是否设置提醒时间

## 📚 相关文档

- [提醒方式功能说明](REMINDER_METHOD_FEATURE.md)
- [Android TimePickerDialog](https://developer.android.com/reference/android/app/TimePickerDialog)
- [Calendar API](https://developer.android.com/reference/java/util/Calendar)

## 🎉 总结

闹钟提醒时间功能让用户可以更灵活地设置提醒时间，避免在深夜或不方便的时间被打扰。特别适合：

- **重要药品**：在每天服药时间提醒
- **食品管理**：在做饭前提醒检查食材
- **工作时间**：在上班时间提醒处理即将过期的办公用品

通过精确的时间控制，让商品保质期管理更加智能和人性化！
