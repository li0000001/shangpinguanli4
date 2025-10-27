# 更新日志 (Changelog)

本文档记录了商品保质期管家应用的所有重要更改。

---

## [版本 1.1.0] - 2024-10-27

### ✨ 新增功能

#### 提醒方式选择
- **双重提醒方式**：用户可以选择通知提醒或闹钟提醒
  - 📱 **通知提醒**（默认）：静默弹出通知，适合日常商品
  - ⏰ **闹钟提醒**：带声音警报，适合重要商品
- **UI 更新**：在添加商品界面新增提醒方式选择卡片
- **列表显示**：商品列表中显示所选的提醒方式
- **日历集成**：直接调用 Android Calendar API 的提醒方式

### 🔧 技术改进

- **数据库升级**：从 v1 升级到 v2
  - 新增 `reminderMethod` 字段
  - 自动迁移现有数据，默认使用通知提醒
- **向后兼容**：使用 Room 迁移策略，确保平滑升级
- **代码优化**：增加 `ReminderMethod` 对象，统一管理提醒方式常量

### 📝 文档更新

- 新增 `REMINDER_METHOD_FEATURE.md` - 提醒方式功能详细说明
- 新增 `CHANGELOG.md` - 版本更新日志

---

## [版本 1.0.0] - 2024-10-27

### 🎉 首次发布

#### 核心功能

1. **商品管理**
   - 添加商品（两种日期输入方式）
   - 查看商品列表（按到期日排序）
   - 删除商品

2. **日期输入方式**
   - 方式一：生产日期 + 保质期天数（自动计算）
   - 方式二：直接选择保质日期

3. **智能提醒**
   - 自定义提前提醒天数
   - 自动同步到系统日历
   - 删除商品时同步删除日历事件

4. **智能颜色标识**
   - 🔴 红色：已过期商品
   - 🟠 橙色：今天到期
   - 🟡 黄色：提醒期内（临近到期）
   - 🟢 白色：安全期

5. **现代化界面**
   - Material Design 3 设计
   - 深色模式支持
   - 简体中文界面
   - 自定义应用图标

#### 技术栈

- **语言**：Kotlin 100%
- **架构**：MVVM
- **数据库**：Room Persistence Library
- **UI框架**：Material Design 3
- **异步处理**：Kotlin Coroutines + LiveData

#### 系统要求

- **最低版本**：Android 8.0 (API 26)
- **目标版本**：Android 14 (API 34)
- **推荐版本**：Android 13+

#### 权限

- READ_CALENDAR - 读取日历
- WRITE_CALENDAR - 写入日历
- POST_NOTIFICATIONS - 通知权限（Android 13+）

#### 文档

- README.md - 项目总览
- QUICK_START.md - 快速入门
- FEATURES.md - 功能详解
- SETUP.md - 环境配置
- PROJECT_OVERVIEW.md - 架构说明
- BUILD_INFO.md - 构建信息
- INDEX.md - 文档索引
- LICENSE - MIT 许可证

---

## 版本说明

### 版本号格式
本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范：

- **主版本号**：不兼容的 API 修改
- **次版本号**：向下兼容的功能性新增
- **修订号**：向下兼容的问题修正

### 更新类型标识

- ✨ **新增功能** (Added)
- 🔧 **技术改进** (Changed)
- 🐛 **Bug修复** (Fixed)
- ⚠️ **废弃功能** (Deprecated)
- 🗑️ **移除功能** (Removed)
- 🔒 **安全更新** (Security)
- 📝 **文档更新** (Documentation)

---

## 计划功能 (Roadmap)

### v1.2.0 (计划中)
- [ ] 编辑已有商品
- [ ] 商品分类管理
- [ ] 搜索和筛选功能
- [ ] 批量操作

### v1.3.0 (计划中)
- [ ] 统计图表
- [ ] 数据导出/导入
- [ ] 云同步支持
- [ ] Widget 桌面小部件

### v2.0.0 (未来)
- [ ] 图片上传
- [ ] 条码扫描
- [ ] AI 识别保质期
- [ ] 多语言支持

---

## 反馈与贡献

欢迎提交 Issue 和 Pull Request！

- **Bug 报告**：[GitHub Issues](https://github.com/your-repo/issues)
- **功能建议**：[GitHub Discussions](https://github.com/your-repo/discussions)
- **贡献代码**：请阅读 CONTRIBUTING.md（待添加）

---

**最后更新**：2024年10月27日
