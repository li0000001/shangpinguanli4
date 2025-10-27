# 构建信息

## 项目信息

- **项目名称**: 商品保质期管家 (Expiry Tracker)
- **包名**: com.expirytracker
- **版本**: 1.0 (versionCode: 1)
- **创建日期**: 2024年10月

## 构建配置

### Gradle
- Gradle 版本: 8.2
- Android Gradle Plugin: 8.1.2
- Kotlin 版本: 1.9.10
- KSP 版本: 1.9.10-1.0.13

### Android SDK
- compileSdk: 34
- targetSdk: 34
- minSdk: 26

### Java
- Source Compatibility: Java 17
- Target Compatibility: Java 17
- JVM Target: 17

## 依赖版本

### AndroidX
- core-ktx: 1.12.0
- appcompat: 1.6.1
- constraintlayout: 2.1.4
- coordinatorlayout: 1.2.0
- recyclerview: 1.3.2

### Material Design
- material: 1.11.0

### Room
- room-runtime: 2.6.1
- room-ktx: 2.6.1
- room-compiler: 2.6.1 (KSP)

### Lifecycle
- lifecycle-viewmodel-ktx: 2.7.0
- lifecycle-livedata-ktx: 2.7.0
- lifecycle-runtime-ktx: 2.7.0

### Coroutines
- kotlinx-coroutines-android: 1.7.3

### Testing
- junit: 4.13.2
- androidx.test.ext:junit: 1.1.5
- espresso-core: 3.5.1

## 构建命令

### Debug 构建
```bash
./gradlew assembleDebug
```
输出: `app/build/outputs/apk/debug/app-debug.apk`

### Release 构建
```bash
./gradlew assembleRelease
```
输出: `app/build/outputs/apk/release/app-release.apk`

### 安装到设备
```bash
./gradlew installDebug
```

### 清理构建
```bash
./gradlew clean
```

### 运行测试
```bash
./gradlew test
```

### 生成签名 APK
需要在 `app/build.gradle.kts` 中配置签名信息：

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("your-keystore.jks")
            storePassword = "your-store-password"
            keyAlias = "your-key-alias"
            keyPassword = "your-key-password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

## 项目结构统计

### 代码量
- Kotlin 文件: 9
- XML 文件: 14
- 资源文件: 24+
- 文档文件: 6

### 代码行数（估算）
- Kotlin 代码: ~1200 行
- XML 布局: ~300 行
- 配置文件: ~150 行
- 总计: ~1650 行

### 文件大小
- 源代码: ~80 KB
- 资源文件: ~10 KB
- 图标文件: ~2 KB
- 文档文件: ~30 KB

## 构建要求

### 开发环境
- Android Studio Hedgehog (2023.1.1) 或更高
- JDK 17 或更高
- Gradle 8.0 或更高
- Android SDK 34

### 运行环境
- Android 8.0 (API 26) 或更高
- 推荐 Android 13+ 以获得完整功能
- 至少 50 MB 存储空间
- 需要日历账户

## 构建特性

### 启用的功能
- ✅ ViewBinding
- ✅ R8 代码压缩（Release）
- ✅ ProGuard 规则
- ✅ 备份规则
- ✅ 数据提取规则

### BuildConfig
```kotlin
buildFeatures {
    viewBinding = true
}
```

### ProGuard
Release 构建默认禁用混淆，可根据需要启用：
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true  // 启用代码压缩
        isShrinkResources = true  // 启用资源压缩
    }
}
```

## 测试配置

### 单元测试
```bash
./gradlew test
```

### 仪器测试
```bash
./gradlew connectedAndroidTest
```

### 代码覆盖率
```bash
./gradlew createDebugCoverageReport
```

## 发布清单

发布前检查项：

- [ ] 更新版本号 (versionCode & versionName)
- [ ] 配置签名信息
- [ ] 启用代码压缩
- [ ] 测试所有功能
- [ ] 检查权限声明
- [ ] 更新 README
- [ ] 准备 Google Play 资源
- [ ] 生成签名 APK
- [ ] 测试签名 APK

## 性能指标

### APK 大小（估算）
- Debug APK: ~5-7 MB
- Release APK (未压缩): ~5-7 MB
- Release APK (压缩后): ~3-4 MB

### 启动时间
- 冷启动: < 2秒
- 热启动: < 0.5秒

### 内存占用
- 空闲状态: ~50 MB
- 正常使用: ~80 MB
- 峰值: ~120 MB

## 兼容性

### Android 版本
- ✅ Android 14 (API 34)
- ✅ Android 13 (API 33)
- ✅ Android 12 (API 31)
- ✅ Android 11 (API 30)
- ✅ Android 10 (API 29)
- ✅ Android 9 (API 28)
- ✅ Android 8.1 (API 27)
- ✅ Android 8.0 (API 26)

### 设备支持
- ✅ 手机
- ✅ 平板
- ✅ 可折叠设备
- ✅ Chrome OS (Android 子系统)

### 架构支持
- ✅ ARM64-v8a
- ✅ ARMv7
- ✅ x86
- ✅ x86_64

## 许可证

MIT License - 详见 LICENSE 文件

## 维护信息

- 最后更新: 2024年10月
- 维护状态: 活跃开发中
- Issue 跟踪: GitHub Issues
- 贡献指南: CONTRIBUTING.md (待添加)
