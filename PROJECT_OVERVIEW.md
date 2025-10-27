# 商品保质期管家 - 项目概览

## 📱 应用简介

这是一个帮助用户管理商品保质期的 Android 应用，使用 Kotlin 开发，采用现代化的 Material Design 3 设计。

## 🎯 核心功能

### 1. 灵活的日期输入
- ✅ 方式一：生产日期 + 保质期天数
- ✅ 方式二：直接选择保质日期
- 自动计算到期日

### 2. 智能日历集成
- ✅ 自动添加到系统日历
- ✅ 自定义提醒时间
- ✅ 删除商品时同步删除日历事件

### 3. 商品管理
- ✅ 按到期日期自动排序
- ✅ 智能颜色标识
  - 🔴 红色：已过期
  - 🟠 橙色：今天到期
  - 🟡 黄色：提醒期内
  - 🟢 绿色：安全期
- ✅ 一键删除

### 4. 现代化界面
- ✅ Material Design 3
- ✅ 支持深色模式
- ✅ 简体中文界面
- ✅ 流畅的动画效果

## 🏗️ 技术架构

### 架构模式
```
MVVM (Model-View-ViewModel)
├── Model: Room Database + Repository
├── View: Activities + XML Layouts
└── ViewModel: LiveData + Coroutines
```

### 核心组件

#### 数据层 (Data Layer)
- `Product.kt` - 商品数据模型（Room Entity）
- `ProductDao.kt` - 数据访问对象
- `ProductDatabase.kt` - Room 数据库
- `ProductRepository.kt` - 数据仓库

#### UI层 (UI Layer)
- `MainActivity.kt` - 主界面（商品列表）
- `AddProductActivity.kt` - 添加商品界面
- `ProductAdapter.kt` - RecyclerView 适配器

#### ViewModel层
- `ProductViewModel.kt` - 业务逻辑处理

#### 工具类 (Utils)
- `CalendarHelper.kt` - 日历集成工具

## 📦 依赖库

```kotlin
// AndroidX 核心库
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1

// Material Design 3
com.google.android.material:material:1.11.0

// Room 数据库
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Lifecycle 组件
androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
androidx.lifecycle:lifecycle-livedata-ktx:2.7.0

// Kotlin 协程
kotlinx-coroutines-android:1.7.3
```

## 📂 项目结构

```
ExpiryTracker/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/expirytracker/
│   │       │   ├── data/          # 数据层
│   │       │   ├── ui/            # UI层
│   │       │   ├── viewmodel/     # ViewModel
│   │       │   └── utils/         # 工具类
│   │       ├── res/               # 资源文件
│   │       │   ├── layout/        # 布局文件
│   │       │   ├── values/        # 值资源
│   │       │   ├── mipmap-*/      # 应用图标
│   │       │   ├── drawable/      # 可绘制资源
│   │       │   └── xml/           # XML配置
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
├── SETUP.md
└── PROJECT_OVERVIEW.md
```

## 🔐 权限说明

| 权限 | 用途 | 必需性 |
|------|------|--------|
| READ_CALENDAR | 读取日历事件 | 必需 |
| WRITE_CALENDAR | 写入日历事件 | 必需 |
| POST_NOTIFICATIONS | 发送通知 (Android 13+) | 必需 |

## 📱 系统要求

- **最低版本**: Android 8.0 (API 26)
- **目标版本**: Android 14 (API 34)
- **推荐设备**: iQOO (Android 13+)

## 🚀 快速开始

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd ExpiryTracker
   ```

2. **在 Android Studio 中打开**
   - File → Open
   - 选择项目根目录
   - 等待 Gradle 同步

3. **运行应用**
   - 连接设备或启动模拟器
   - 点击 Run 按钮

## 🎨 设计特点

### 颜色主题
- **主色**: Purple (#6200EE)
- **次要色**: Teal (#03DAC6)
- **错误色**: Red (#B00020)

### UI组件
- FloatingActionButton - 添加商品
- MaterialCardView - 商品卡片
- MaterialButton - 操作按钮
- TextInputLayout - 输入框
- DatePickerDialog - 日期选择器

## 🔄 数据流

```
User Action
    ↓
Activity/Fragment
    ↓
ViewModel
    ↓
Repository
    ↓
Room Database / Calendar Provider
    ↓
LiveData Update
    ↓
UI Update
```

## 🧪 测试建议

### 功能测试
1. ✅ 添加商品（两种方式）
2. ✅ 查看商品列表
3. ✅ 删除商品
4. ✅ 验证日历事件
5. ✅ 检查颜色标识
6. ✅ 测试权限请求

### 兼容性测试
- Android 8.0 - 14
- 不同屏幕尺寸
- 深色/浅色模式
- 不同日历应用

## 📝 待改进功能

- [ ] 编辑已有商品
- [ ] 批量导入/导出
- [ ] 图片上传（拍照识别保质期）
- [ ] 统计图表
- [ ] 多语言支持
- [ ] 云同步
- [ ] Widget 桌面小部件

## 📄 许可证

MIT License

## 👨‍💻 开发者

使用 Kotlin 和 Android 最佳实践构建
