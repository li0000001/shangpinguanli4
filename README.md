# 商品保质期管家 (Expiry Tracker)

一款用于管理商品保质期的 Android 应用，帮助您及时了解商品的到期日期。

## 功能特性

### 核心功能

1. **灵活的日期输入**
   - 方式一：输入生产日期 + 保质期天数，自动计算到期日
   - 方式二：直接选择保质日期

2. **智能提醒系统**
   - 自定义提前提醒天数
   - **双重提醒方式**：通知提醒 或 闹钟提醒
   - 自动同步到手机日历
   - 到期前自动提醒

3. **商品列表管理**
   - 按到期日排序显示
   - 智能颜色标识（已过期、即将过期、安全期）
   - 一键删除，自动清理日历事件

4. **现代化界面**
   - Material Design 3 设计
   - 支持深色模式
   - 简洁易用的交互

## 技术栈

- **语言**: Kotlin
- **架构**: MVVM
- **数据库**: Room
- **UI框架**: Material Design 3
- **异步处理**: Kotlin Coroutines
- **依赖注入**: ViewModel + LiveData

## 权限说明

- `READ_CALENDAR`: 读取日历，用于验证日历事件
- `WRITE_CALENDAR`: 写入日历，用于添加和删除提醒事件
- `POST_NOTIFICATIONS`: 发送通知（Android 13+）

## 系统要求

- Android 8.0 (API 26) 或更高版本
- 推荐 Android 13+ 以获得最佳体验

## 使用说明

### 添加商品

1. 点击主界面右下角的 + 按钮
2. 输入商品名称
3. 选择以下任一方式：
   - **方式一**: 选择生产日期 + 输入保质期天数
   - **方式二**: 直接选择保质日期
4. 设置提前提醒天数（默认3天）
5. 点击"保存"按钮

### 查看商品

- 商品按到期日期排序
- 颜色标识：
  - 🔴 红色：已过期
  - 🟠 橙色：今天到期
  - 🟡 黄色：在提醒期内
  - 🟢 绿色：安全期

### 删除商品

点击商品卡片上的"删除"按钮，确认后删除。日历中的提醒事件会自动同步删除。

## 项目结构

```
app/
├── src/main/
│   ├── java/com/expirytracker/
│   │   ├── data/              # 数据层
│   │   │   ├── Product.kt
│   │   │   ├── ProductDao.kt
│   │   │   ├── ProductDatabase.kt
│   │   │   └── ProductRepository.kt
│   │   ├── ui/                # UI层
│   │   │   ├── MainActivity.kt
│   │   │   ├── AddProductActivity.kt
│   │   │   └── ProductAdapter.kt
│   │   ├── viewmodel/         # ViewModel层
│   │   │   └── ProductViewModel.kt
│   │   └── utils/             # 工具类
│   │       └── CalendarHelper.kt
│   └── res/                   # 资源文件
│       ├── layout/
│       ├── values/
│       └── mipmap/
└── build.gradle.kts
```

## 构建项目

```bash
# 克隆仓库
git clone <repository-url>
cd ExpiryTracker

# 构建项目
./gradlew build

# 安装到设备
./gradlew installDebug
```

## 开发者

本项目使用 Kotlin 和现代 Android 开发最佳实践构建。

## 许可证

MIT License
