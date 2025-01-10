MySQL场景order by中去做选择，某个三个A,B,C，D，E字段排序，当D字段不为空排前面，并且依据A，B两个字段控制排序A不为空按照A排序倒排，A为空按照B倒排，其中A和B各自排序后其中按照E递增排序，D为空排后面并且按照E字段倒排
```sql
SELECT * FROM table_name 
ORDER BY 
CASE WHEN D IS NOT NULL THEN 0 ELSE 1 END, -- D不为空的记录排在前面 
IFNULL(A, B) DESC, -- A不为空的记录按照A倒排 
IFNULL(B, A) DESC,-- A为空的记录按照B倒排
E ASC,
CASE WHEN D IS NULL THEN 1 ELSE 0 END, -- D为空的记录排在后面 
E DESC; -- D为空的记录按照E倒排
```

启动MySQL主从复制后，您需要执行以下操作：

**1. 创建复制用户**

在主服务器上创建一个用户，该用户将用于从服务器连接到主服务器。执行以下命令：

```sql
CREATE USER 'replication'@'%' IDENTIFIED BY 'yourpassword';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'%';
```

请注意，`replication`是用户名，`yourpassword`是密码。您可以根据需要更改这些值。

**2. 获取主服务器状态**

在主服务器上执行以下命令，以获取主服务器的状态：

```sql
SHOW MASTER STATUS;
```

此命令将显示主服务器上的二进制日志文件名和位置。请记下这些值，因为它们将用于配置从服务器。

**3. 配置从服务器**

在从服务器上，您需要编辑MySQL配置文件`/etc/mysql/my.cnf`，并添加以下行：

```perl
server-id=2 # 从服务器的唯一ID
relay-log=mysqld-relay-bin
log-slave-updates
read-only
```

请注意，`server-id`必须是不同于主服务器的整数。`relay-log`用于启用中继日志记录。`log-slave-updates`用于启用从服务器更新的二进制日志记录。`read-only`用于将从服务器设置为只读模式。

**4. 启动从服务器**

在从服务器上启动MySQL服务。执行以下命令：

```sql
sudo service mysql start
```

**5. 配置从服务器复制**

在从服务器上执行以下命令，以配置从服务器复制主服务器：

```bash
CHANGE MASTER TO MASTER_HOST='master_host_name', MASTER_USER='replication', MASTER_PASSWORD='yourpassword', MASTER_LOG_FILE='master_log_file_name', MASTER_LOG_POS=master_log_file_position;
```

请将`master_host_name`替换为主服务器的主机名或IP地址，`yourpassword`替换为在主服务器上创建的复制用户的密码，`master_log_file_name`替换为主服务器上的二进制日志文件名，`master_log_file_position`替换为主服务器上的二进制日志位置。

**6. 启动从服务器复制**

在从服务器上执行以下命令，以启动从服务器复制：

```sql
START SLAVE;
```

执行此命令后，从服务器将开始复制主服务器上的更改。

请注意，这只是MySQL主从复制的基本操作。您可能需要根据您的特定情况进行更改或添加其他操作。