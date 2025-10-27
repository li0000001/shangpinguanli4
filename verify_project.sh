#!/bin/bash

echo "==================================="
echo "项目结构验证"
echo "==================================="
echo ""

echo "📂 检查必需的目录..."
required_dirs=(
    "app/src/main/java/com/expirytracker/data"
    "app/src/main/java/com/expirytracker/ui"
    "app/src/main/java/com/expirytracker/viewmodel"
    "app/src/main/java/com/expirytracker/utils"
    "app/src/main/res/layout"
    "app/src/main/res/values"
    "app/src/main/res/mipmap-hdpi"
)

for dir in "${required_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "✓ $dir"
    else
        echo "✗ 缺失: $dir"
    fi
done

echo ""
echo "📝 检查必需的文件..."
required_files=(
    "app/src/main/java/com/expirytracker/data/Product.kt"
    "app/src/main/java/com/expirytracker/data/ProductDao.kt"
    "app/src/main/java/com/expirytracker/data/ProductDatabase.kt"
    "app/src/main/java/com/expirytracker/ui/MainActivity.kt"
    "app/src/main/java/com/expirytracker/ui/AddProductActivity.kt"
    "app/src/main/java/com/expirytracker/viewmodel/ProductViewModel.kt"
    "app/src/main/java/com/expirytracker/utils/CalendarHelper.kt"
    "app/src/main/AndroidManifest.xml"
    "app/build.gradle.kts"
    "build.gradle.kts"
    "settings.gradle.kts"
)

for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✓ $file"
    else
        echo "✗ 缺失: $file"
    fi
done

echo ""
echo "📦 文件统计..."
echo "Kotlin 文件: $(find . -name '*.kt' ! -path './.git/*' | wc -l)"
echo "XML 文件: $(find . -name '*.xml' ! -path './.git/*' | wc -l)"
echo "PNG 图标: $(find . -name '*.png' ! -path './.git/*' | wc -l)"

echo ""
echo "🎨 图标验证..."
for density in mdpi hdpi xhdpi xxhdpi xxxhdpi; do
    if [ -f "app/src/main/res/mipmap-$density/ic_launcher.png" ]; then
        size=$(wc -c < "app/src/main/res/mipmap-$density/ic_launcher.png")
        echo "✓ mipmap-$density/ic_launcher.png ($size bytes)"
    else
        echo "✗ mipmap-$density/ic_launcher.png 缺失"
    fi
done

echo ""
echo "==================================="
echo "✅ 项目验证完成！"
echo "==================================="
