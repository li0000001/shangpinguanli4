# 商品保质期管家 - 文档索引

欢迎来到商品保质期管家项目！这个文档将帮助你快速找到所需信息。

## 📚 文档目录

### 新手入门
1. **[README.md](README.md)** - 项目总览和基本信息
2. **[QUICK_START.md](QUICK_START.md)** - 5分钟快速上手指南
3. **[SETUP.md](SETUP.md)** - 详细的开发环境设置

### 功能文档
4. **[FEATURES.md](FEATURES.md)** - 完整功能列表和使用说明
5. **[PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)** - 项目架构和技术栈

### 开发文档
6. **[BUILD_INFO.md](BUILD_INFO.md)** - 构建配置和发布信息
7. **[LICENSE](LICENSE)** - MIT 开源许可证

## 🎯 根据需求选择文档

### 我想运行这个应用
→ 阅读 [QUICK_START.md](QUICK_START.md)

### 我想了解有什么功能
→ 阅读 [FEATURES.md](FEATURES.md)

### 我想参与开发
→ 阅读 [SETUP.md](SETUP.md) 和 [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)

### 我想构建 APK
→ 阅读 [BUILD_INFO.md](BUILD_INFO.md)

### 我想了解技术架构
→ 阅读 [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)

### 我遇到了问题
→ 查看各文档的"常见问题"章节

## 📱 项目快览

### 核心功能
- ✅ 商品保质期管理
- ✅ 灵活的日期输入（两种方式）
- ✅ 日历自动同步
- ✅ 智能颜色提醒
- ✅ Material Design 3 界面

### 技术栈
- **语言**: Kotlin 100%
- **架构**: MVVM
- **数据库**: Room
- **UI**: Material Design 3
- **异步**: Coroutines + LiveData

### 系统要求
- Android 8.0+ (API 26+)
- 推荐 Android 13+
- 需要日历权限

## 🚀 快速链接

### 关键文件位置

#### 源代码
```
app/src/main/java/com/expirytracker/
├── data/           # 数据层
├── ui/             # UI层
├── viewmodel/      # ViewModel层
└── utils/          # 工具类
```

#### 资源文件
```
app/src/main/res/
├── layout/         # XML布局
├── values/         # 字符串、颜色、主题
└── mipmap-*/       # 应用图标
```

#### 配置文件
```
build.gradle.kts            # 项目构建配置
app/build.gradle.kts        # 应用构建配置
settings.gradle.kts         # 项目设置
AndroidManifest.xml         # 应用清单
```

## 🔧 常用命令

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test

# 清理构建
./gradlew clean
```

## 📞 获取帮助

### 问题排查顺序
1. 查看 [QUICK_START.md](QUICK_START.md) 的常见问题
2. 查看 [SETUP.md](SETUP.md) 的故障排除
3. 查看 [BUILD_INFO.md](BUILD_INFO.md) 的构建要求
4. 提交 GitHub Issue

## 🎨 项目统计

- **Kotlin 文件**: 9
- **XML 文件**: 14
- **代码行数**: ~1650
- **文档页数**: 7
- **支持 Android 版本**: 9 个主要版本

## 🌟 项目亮点

1. **零学习成本** - 直观的用户界面
2. **完整文档** - 详尽的使用和开发文档
3. **现代架构** - MVVM + Room + Coroutines
4. **Material 3** - 最新的设计规范
5. **隐私友好** - 完全本地存储，无网络请求

## 📖 阅读顺序建议

### 对于用户
1. README.md - 了解项目
2. QUICK_START.md - 快速上手
3. FEATURES.md - 深入了解功能

### 对于开发者
1. README.md - 了解项目
2. SETUP.md - 配置环境
3. PROJECT_OVERVIEW.md - 理解架构
4. BUILD_INFO.md - 构建发布

### 对于贡献者
1. 阅读所有文档
2. 理解代码结构
3. 遵循编码规范
4. 提交 Pull Request

## 🎯 下一步

根据你的角色选择：

- **🆕 新用户**: 直接跳到 [QUICK_START.md](QUICK_START.md)
- **👨‍💻 开发者**: 前往 [SETUP.md](SETUP.md)
- **📱 体验者**: 下载 APK 直接使用
- **🔍 研究者**: 查看 [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md)

---

**祝你使用愉快！**

如有任何问题或建议，欢迎提交 Issue 或 Pull Request。
