# 开发环境设置指南

## 前置要求

1. **Android Studio**: Hedgehog (2023.1.1) 或更高版本
2. **JDK**: Java 17 或更高版本
3. **Android SDK**: API 34 (Android 14)
4. **Gradle**: 8.2 或更高版本

## 安装步骤

### 1. 克隆项目

```bash
git clone <repository-url>
cd ExpiryTracker
```

### 2. 在 Android Studio 中打开项目

1. 打开 Android Studio
2. 选择 "Open an Existing Project"
3. 选择项目根目录
4. 等待 Gradle 同步完成

### 3. 配置 Android SDK

确保已安装以下组件：
- Android SDK Platform 34
- Android SDK Build-Tools 34.0.0
- Android SDK Platform-Tools

### 4. 运行项目

#### 使用 Android Studio
1. 连接 Android 设备或启动模拟器
2. 点击 "Run" 按钮 (Shift + F10)

#### 使用命令行
```bash
# 构建 Debug APK
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test
```

## Android 13+ 特殊说明

### 运行时权限

应用需要以下权限，都会在运行时请求：

1. **日历权限** (READ_CALENDAR, WRITE_CALENDAR)
   - 用于添加和删除提醒事件
   - 首次打开应用时会自动请求

2. **通知权限** (POST_NOTIFICATIONS)
   - Android 13+ 新增要求
   - 用于显示到期提醒
   - 首次打开应用时会自动请求

### 在 iQOO (Android 13+) 上测试

#### 授予权限
1. 打开应用后，会弹出权限请求对话框
2. 点击"允许"授予日历权限
3. 点击"允许"授予通知权限

#### 手动授予权限（如果错过了）
1. 打开"设置" → "应用" → "商品保质期管家"
2. 点击"权限"
3. 启用"日历"和"通知"权限

#### 验证日历集成
1. 添加一个商品
2. 打开手机自带的日历应用
3. 查找对应的到期提醒事件

#### 可能的问题

**问题1**: 找不到日历账户
- **解决**: 确保手机上至少配置了一个日历账户（Google账户或本地账户）

**问题2**: 日历事件未显示
- **解决**: 
  1. 检查日历应用的设置
  2. 确保显示所有日历
  3. 检查日期筛选器设置

**问题3**: 通知未显示
- **解决**: 
  1. 检查系统通知设置
  2. 确保未开启"勿扰模式"
  3. 检查应用通知权限

## 构建发布版本

```bash
# 生成签名的 Release APK
./gradlew assembleRelease

# 输出位置
# app/build/outputs/apk/release/app-release.apk
```

注意：发布版本需要配置签名密钥。

## 常见问题

### Q: 编译失败，提示找不到 Room 相关类
A: 清理项目并重新构建：
```bash
./gradlew clean
./gradlew build
```

### Q: ViewBinding 报错
A: 确保 `build.gradle.kts` 中启用了 ViewBinding：
```kotlin
buildFeatures {
    viewBinding = true
}
```

### Q: Material Design 3 组件不显示
A: 检查主题配置，确保使用 `Theme.Material3` 父主题。

## 调试技巧

### 查看 Room 数据库
使用 Android Studio 的 Database Inspector：
1. 运行应用（Debug 模式）
2. View → Tool Windows → App Inspection
3. 选择 Database Inspector 标签

### 查看日历事件
使用 ADB 命令：
```bash
adb shell content query --uri content://com.android.calendar/events
```

### 日志过滤
在 Logcat 中使用以下 tag 过滤：
- `ExpiryTracker`
- `ProductViewModel`
- `CalendarHelper`

## 技术支持

如有问题，请查看：
1. [Android 开发者文档](https://developer.android.com)
2. [Material Design 指南](https://m3.material.io)
3. [Room 文档](https://developer.android.com/training/data-storage/room)
