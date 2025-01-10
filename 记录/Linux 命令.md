### 语法

```css
scp [选项] [源文件] [目标地址]
```

### 选项

-   `-r`：递归复制整个目录。
-   `-P`：指定SSH端口号。
-   `-p`：保留源文件的修改时间、访问时间和权限。
-   `-q`：安静模式，不显示传输进度信息。
-   `-C`：压缩传输数据。
-   `-i`：指定私钥文件。
-   `-v`：显示详细的传输信息。
- 1.  将本地文件复制到远程主机：

```ruby
scp /path/to/local/file user@remote:/path/to/remote/directory/
```

这将把本地文件复制到远程主机的指定目录中。

2.  将远程文件复制到本地主机：

```ruby
scp user@remote:/path/to/remote/file /path/to/local/directory/
```

这将把远程文件复制到本地主机的指定目录中。

3.  递归复制整个目录：

```ruby
scp -r /path/to/local/directory user@remote:/path/to/remote/directory/
```

这将递归复制整个本地目录到远程主机的指定目录中。

4.  指定SSH端口号：

```ruby
scp -P 2222 /path/to/local/file user@remote:/path/to/remote/directory/
```

这将使用SSH端口号2222将本地文件复制到远程主机的指定目录中。

5.  保留源文件的修改时间、访问时间和权限：

```ruby
scp -p /path/to/local/file user@remote:/path/to/remote/directory/
```

这将保留本地文件的修改时间、访问时间和权限，并将其复制到远程主机的指定目录中。