#!/bin/bash

# -----------------------------
# 配置仓库
# -----------------------------
GITEE_REPO="https://gitee.com/durcframework/code-gen.git"
GITHUB_REPO="git@github.com:jamie20241210/code-gen.git"

# 本地仓库目录（假设当前目录就是仓库）
LOCAL_DIR="$(pwd)"

# 需要额外提交到 GitHub 的本地文件
GITHUB_ONLY_FILES=("local run.md" "sync_gitee_to_github.sh")

# -----------------------------
# 开始同步
# -----------------------------
echo "🚀 开始同步 Gitee -> GitHub"
echo "---------------------------------------"

# 1. 如果本地没有.git目录，则初始化并添加远程
if [ ! -d "$LOCAL_DIR/.git" ]; then
    echo "📥 初始化本地仓库..."
    git init
    git remote add gitee "$GITEE_REPO"
    git remote add github "$GITHUB_REPO"
else
    # 确保远程地址正确
    git remote | grep gitee > /dev/null || git remote add gitee "$GITEE_REPO"
    git remote | grep github > /dev/null || git remote add github "$GITHUB_REPO"
fi

# 2. 备份 GitHub 专属文件
echo "💾 备份 GitHub 专属文件..."
BACKUP_DIR="/tmp/code-gen-github-files-$$"
mkdir -p "$BACKUP_DIR"
for file in "${GITHUB_ONLY_FILES[@]}"; do
    if [ -f "$file" ]; then
        cp "$file" "$BACKUP_DIR/" 2>/dev/null
        echo "  ✓ 已备份: $file"
    fi
done

# 3. 从 Gitee 拉取最新代码
echo "📥 拉取 Gitee 最新代码..."
git fetch gitee
git reset --hard gitee/master

# 4. 恢复 GitHub 专属文件
echo "📂 恢复 GitHub 专属文件..."
for file in "${GITHUB_ONLY_FILES[@]}"; do
    if [ -f "$BACKUP_DIR/$file" ]; then
        cp "$BACKUP_DIR/$file" "$file" 2>/dev/null
        echo "  ✓ 已恢复: $file"
    fi
done

# 5. 清理备份
rm -rf "$BACKUP_DIR"

# 6. 强制添加 GitHub 专属文件并提交
echo "📤 推送到 GitHub（包括 GitHub 专属文件）..."
# 显式添加每个文件，避免被 git 忽略
for file in "${GITHUB_ONLY_FILES[@]}"; do
    if [ -f "$file" ]; then
        git add -f "$file"
        echo "  ✓ 已添加: $file"
    fi
done

# 添加其他所有文件
git add .

# 提交更改
if git diff --staged --quiet; then
    echo "  ℹ️  没有新的更改需要提交"
else
    git commit -m "同步 Gitee 最新代码 + GitHub 专属配置"
    echo "  ✓ 已创建提交"
fi

# 强制推送到 GitHub
git push github master --force

echo "✅ 同步完成！"
echo "📝 已同步 Gitee 代码并包含 GitHub 专属文件"