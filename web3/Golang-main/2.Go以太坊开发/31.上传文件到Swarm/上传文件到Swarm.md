# **上传文件到 Swarm**

在上个章节我们在端口“8500”上运行了一个作为背景进程的 swarm 节点。 接下来就导入 swarm 包 go-ethereum `swearm/api/client`。 我将把包装别名为 `bzzclient`。

```
import (
  bzzclient "github.com/ethereum/go-ethereum/swarm/api/client"
)
```

调用 `NewClient` 函数向它传递 swarm 背景程序的 url。

```
client := bzzclient.NewClient("http://127.0.0.1:8500")
```

用内容 _hello world_ 创建示例文本文件 `hello.txt`。 我们将会把这个文件上传到 swarm。

```
hello world
```

在我们的 Go 应用程序中，我们将使用 Swarm 客户端软件包中的“Open”打开我们刚刚创建的文件。 该函数将返回一个 `File` 类型，它表示 swarm 清单中的文件，用于上传和下载 swarm 内容。

```
file, err := bzzclient.Open("hello.txt")
if err != nil {
  log.Fatal(err)
}
```

现在我们可以从客户端实例调用 `Upload` 函数，为它提供文件对象。 第二个参数是一个可选添的现有内容清单字符串，用于添加文件，否则它将为我们创建。 第三个参数是我们是否希望我们的数据被加密。

返回的哈希值是文件的内容清单的哈希值，其中包含 hello.txt 文件作为其唯一条目。 默认情况下，主要内容和清单都会上传。 清单确保您可以使用正确的 mime 类型检索文件。

```
manifestHash, err := client.Upload(file, "", false)
if err != nil {
  log.Fatal(err)
}

fmt.Println(manifestHash) // 2e0849490b62e706a5f1cb8e7219db7b01677f2a859bac4b5f522afd2a5f02c0
```

然后我们就可以在这里查看上传的文件 `bzz://2e0849490b62e706a5f1cb8e7219db7b01677f2a859bac4b5f522afd2a5f02c0`，具体如何下载，我们会在下个章节介绍。

### **完整代码**

Commands

```
geth account new
export BZZKEY=970ef9790b54425bea2c02e25cab01e48cf92573
swarm --bzzaccount $BZZKEY
```

hello.txt

```
hello world
```

swarm_upload.go

```
package main

import (
        "fmt"
        "log"

        bzzclient "github.com/ethereum/go-ethereum/swarm/api/client"
)

func main() {
        client := bzzclient.NewClient("http://127.0.0.1:8500")

        file, err := bzzclient.Open("hello.txt")
        if err != nil {
                log.Fatal(err)
        }

        manifestHash, err := client.Upload(file, "", false)
        if err != nil {
                log.Fatal(err)
        }
        fmt.Println(manifestHash) // 2e0849490b62e706a5f1cb8e7219db7b01677f2a859bac4b5f522afd2a5f02c0
}
```
