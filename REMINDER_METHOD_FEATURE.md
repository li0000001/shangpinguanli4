# 提醒方式功能说明

## 📱 功能概述

新增了提醒方式选择功能，允许用户选择不同的日历提醒方式：
- **通知提醒**：静默弹出通知（默认）
- **闹钟提醒**：带声音警报的闹钟提醒

## 🎯 技术实现

### Android Calendar API 提醒方式

本功能使用 Android Calendar API 的 `CalendarContract.Reminders.METHOD` 字段：

| 常量 | 值 | 说明 |
|------|---|------|
| METHOD_ALERT | 1 | 通知提醒 - 弹出警告通知 |
| METHOD_ALARM | 4 | 闹钟提醒 - 带声音的闹钟警报 |

### 数据库更改

**Product 表新增字段**:
```kotlin
val reminderMethod: Int = ReminderMethod.ALERT  // 默认通知提醒
```

**数据库版本**: 从 v1 升级到 v2
- 自动迁移：添加 `reminderMethod` 字段，默认值为 1（通知提醒）
- 向后兼容：使用 `fallbackToDestructiveMigration()` 确保平滑升级

### 核心代码更改

#### 1. Product.kt
```kotlin
@Entity(tableName = "products")
data class Product(
    // ... 其他字段
    val reminderMethod: Int = ReminderMethod.ALERT,
    // ...
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
```

#### 2. CalendarHelper.kt
```kotlin
fun addEventToCalendar(
    productName: String,
    expiryDate: Long,
    reminderMinutes: Int = 4320,
    reminderMethod: Int = 1  // 新增参数
): Long? {
    // ...
    addReminderToEvent(eventId, reminderMinutes, reminderMethod)
}

private fun addReminderToEvent(
    eventId: Long, 
    reminderMinutes: Int, 
    reminderMethod: Int  // 使用用户选择的方式
) {
    val reminderValues = ContentValues().apply {
        put(CalendarContract.Reminders.METHOD, reminderMethod)
        // ...
    }
}
```

#### 3. AddProductActivity.kt
新增单选按钮组，让用户选择提醒方式：
```kotlin
private var selectedReminderMethod: Int = ReminderMethod.ALERT

binding.radioGroupReminderMethod.setOnCheckedChangeListener { _, checkedId ->
    selectedReminderMethod = when (checkedId) {
        binding.radioReminderAlert.id -> ReminderMethod.ALERT
        binding.radioReminderAlarm.id -> ReminderMethod.ALARM
        else -> ReminderMethod.ALERT
    }
}
```

#### 4. activity_add_product.xml
```xml
<RadioGroup android:id="@+id/radioGroupReminderMethod">
    <MaterialRadioButton
        android:id="@+id/radioReminderAlert"
        android:text="📱 通知提醒（弹出通知）"
        android:checked="true" />
    
    <MaterialRadioButton
        android:id="@+id/radioReminderAlarm"
        android:text="⏰ 闹钟提醒（带声音警报）" />
</RadioGroup>
```

## 🎨 用户界面

### 添加商品界面
在"提前提醒天数"输入框下方新增卡片：

```
┌─────────────────────────────────┐
│ 提醒方式                         │
│ 选择日历提醒方式，影响系统通知形式│
│                                 │
│ ○ 📱 通知提醒（弹出通知）        │
│ ○ ⏰ 闹钟提醒（带声音警报）      │
└─────────────────────────────────┘
```

### 商品列表界面
在每个商品卡片上显示提醒方式：
```
提前 3 天提醒 · 通知提醒
```

## 📝 使用说明

### 添加商品时选择提醒方式

1. 打开"添加商品"界面
2. 填写商品信息
3. 在"提醒方式"卡片中选择：
   - **通知提醒**：适合日常使用，不打扰
   - **闹钟提醒**：重要商品，需要明确提醒
4. 保存商品

### 提醒效果对比

| 方式 | 效果 | 适用场景 |
|------|------|----------|
| 📱 通知提醒 | 静默弹出通知，不发出声音 | 一般商品、日常查看 |
| ⏰ 闹钟提醒 | 发出声音和震动，类似闹钟 | 重要药品、易腐食品 |

## 🔧 技术细节

### 日历事件创建流程
```
1. 用户选择提醒方式 (ALERT/ALARM)
2. Product 对象包含 reminderMethod
3. ProductViewModel 传递给 CalendarHelper
4. CalendarHelper 创建日历事件
5. 设置 Reminder 的 METHOD 字段
6. 日历系统根据 METHOD 触发不同类型提醒
```

### 权限要求
- READ_CALENDAR - 读取日历（已有）
- WRITE_CALENDAR - 写入日历（已有）
- POST_NOTIFICATIONS - 通知权限（已有）

**注意**：闹钟提醒可能还需要：
- SCHEDULE_EXACT_ALARM（Android 12+）
- USE_EXACT_ALARM（Android 14+）

但这些权限由系统日历应用管理，我们的App不需要额外申请。

## 📊 数据迁移

### 现有用户升级
- 数据库自动从 v1 升级到 v2
- 所有现有商品的 reminderMethod 默认设置为 1（通知提醒）
- 无需手动操作，无数据丢失

### SQL 迁移语句
```sql
ALTER TABLE products 
ADD COLUMN reminderMethod INTEGER NOT NULL DEFAULT 1
```

## 🎯 测试建议

### 功能测试
1. **添加商品 - 通知提醒**
   - 选择"通知提醒"
   - 保存后检查日历事件
   - 等待到期时间查看通知效果

2. **添加商品 - 闹钟提醒**
   - 选择"闹钟提醒"
   - 保存后检查日历事件
   - 等待到期时间查看闹钟效果

3. **商品列表显示**
   - 验证列表正确显示提醒方式
   - 检查不同方式的标识

4. **数据库迁移**
   - 安装旧版本（v1）
   - 添加几个商品
   - 升级到新版本（v2）
   - 验证所有商品仍存在
   - 验证默认提醒方式为"通知提醒"

### 兼容性测试
- Android 8.0 - 14
- 不同日历应用（Google日历、系统日历等）
- 不同设备（iQOO、小米、华为等）

## 🐛 已知限制

1. **设备差异**：不同厂商的日历应用对 METHOD_ALARM 的支持可能不同
2. **权限限制**：Android 12+ 可能需要用户在系统设置中手动允许精确闹钟
3. **通知设置**：用户的勿扰模式可能影响提醒效果

## 🔮 未来增强

可以考虑添加更多提醒方式：
- METHOD_DEFAULT (0) - 默认方式
- METHOD_EMAIL (2) - 邮件提醒
- METHOD_SMS (3) - 短信提醒（需要额外权限）

## 📚 参考文档

- [Android CalendarContract.Reminders](https://developer.android.com/reference/android/provider/CalendarContract.Reminders)
- [Room Database Migration](https://developer.android.com/training/data-storage/room/migrating-db-versions)
- [Material Design Radio Buttons](https://m3.material.io/components/radio-button)

## ✅ 完成清单

- [x] 数据模型更新（添加 reminderMethod 字段）
- [x] 数据库迁移（v1 → v2）
- [x] CalendarHelper 支持提醒方式参数
- [x] AddProductActivity UI 添加选择器
- [x] ProductAdapter 显示提醒方式
- [x] 默认值设置（通知提醒）
- [x] 向后兼容保证
- [x] 文档编写

## 🎉 总结

提醒方式功能现已完全集成到商品保质期管家应用中！

用户可以根据商品的重要性选择合适的提醒方式：
- 日常商品使用**通知提醒**，避免打扰
- 重要商品使用**闹钟提醒**，确保不会错过

所有功能都通过调用 Android Calendar API 实现，确保与系统日历完美集成。
