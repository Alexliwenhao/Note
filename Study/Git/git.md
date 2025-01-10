# 常用指令

# git 练习网站
https://learngitbranching.js.org/?locale=zh_CN

## git pull
- 拉取远程代码 
## git pull --rebase
- 用rebase方式拉取代码
## git rebase --continue
- 处理冲突后继续rebase合并 
## git push 
- 推送到远程
## git push -u origin <分支名>
  - 推送新分支到远程 
## git checkout -b <分支名>
- 切换新分支 
## git checkout <分支名>
- 切换分支 
## git checkout .
- 重置当前的修改 
## git reset
- 重置当前暂存区的代码 
## git clean -df
- 清理未被追踪的文件和空文件夹 
## git merge <分支名>
- 把指定分支合并到当前分支 
## git merge --continue
- 处理冲突之后继续合并 
## git status
- 查看当前git的状态 
## git add .
- 把所有修改提交到暂存区 
## git commit -m <信息>
- 提交暂存区的的修改 

## git fetch origin --tags
- 拉取远程所有tag到本地
## git push -u origin --tags
- 推送本地所有tags到远程

## git tag
- 查看本地的标签列表
## git tag xxx
- 打标签
## git push origin xxx
- 将标签推送到远程
## git push --delete origin xxx
- 删除远程的标签

# git配置
## 默认账户
>`git config --global user.name <name>`
>`git config --global user.email <email>`
## 当前项目账户
>`git config user.name <name>`
>`git config user.email <email>`

## git log时间格式
  >`git config log.date iso`
>`git config log.date iso-local`
>`git config log.date iso-strict-local`

## 花式log
- <变量> 需要自己修改

### ![[查看自己的提交记录]]

# 彻底清除已经删除的大文件
## 将最大的20个文件输出到临时文本里
>git rev-list --objects --all | grep "$(git verify-pack -v .git/objects/pack/*.idx | sort -k 3 -n | tail -20 | awk '{print$1}')" > /tmp/1.txt

## 将文件名输出到临时文件里
>cat /tmp/1.txt | awk '{print $2}' | tr '\n' ' ' > /tmp/2.txt

## 从所有分支所有tag中移除这些文件的记录
>git filter-branch -f --prune-empty --index-filter "git rm -rf --cached --ignore-unmatch `cat /tmp/2.txt`" --tag-name-filter cat -- --all

## 清除本地缓存
>rm -Rf .git/refs/original
>rm -Rf .git/logs/
>git gc
>git prune

## 清除远程仓库大文件
>将远程仓库改名(备份)，然后新建同名仓库，将本地仓库推到新建的远程仓库上，确认无误后删除备份的远程仓库

# 误操作恢复
1. 以master分支为例，远程有修改A，本地有修改B
`git pull --rebase`，发生冲突
2. 本地解决冲突后，`git rebase --continue`
3. 现在要回滚到rebase之前的状态
4. 打开文件 `.git/logs/refs/heads/<分支名>`
5. 找到rebase之前的header对应的commit，`git reset --hard <commit>`
6. 回滚成功


# 解决git status中文乱码
```shell
git config --global core.quotepath false
```

# 初始化仓库为共享仓库
```
git init --bare
```