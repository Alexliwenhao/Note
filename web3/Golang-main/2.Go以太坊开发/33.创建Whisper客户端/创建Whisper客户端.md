# Whisper

Whisper 是一种简单的基于点对点身份的消息传递系统，旨在成为下一去中心化的应用程序的构建块。 它旨在以相当的代价提供弹性和隐私。 在接下来的部分中，我们将设置一个支持 Whisper 的以太坊节点，然后我们将学习如何在 Whisper 协议上发送和接收加密消息。

# **连接 Whisper 客户端**

要使用连接 Whisper 客户端，我们必须首先连接到运行 whisper 的以太坊节点。 不幸的是，诸如 infura 之类的公共网关不支持 whisper，因为没有金钱动力免费处理这些消息。 Infura 可能会在不久的将来支持 whisper，但现在我们必须运行我们自己的 `geth` 节点。一旦你<u>安装 geth</u>, 运行 geth 的时候加 `--shh` flag 来支持 whisper 协议, 并且加 `--ws` flag 和 `--rpc`，来支持 websocket 来接收实时信息，

```go
geth --rpc --shh --ws
```

现在在我们的 Go 应用程序中，我们将导入在 `whisper/shhclient` 中找到的 go-ethereum whisper 客户端软件包并初始化客户端，使用默认的 websocket 端口“8546”通过 websockets 连接我们的本地 geth 节点。

```go
client, err := shhclient.Dial("ws://127.0.0.1:8546")
if err != nil {
  log.Fatal(err)
}

_ = client // we'll be using this in the 下个章节
```

现在我们已经拨打了，让我们创建一个密钥对来加密消息，然后再发送消息在下一章节.

### **完整代码**

Commands

```go
geth --rpc --shh --ws
```

whisper_client.go

```go
package main

import (
        "log"

        "github.com/ethereum/go-ethereum/whisper/shhclient"
)

func main() {
        client, err := shhclient.Dial("ws://127.0.0.1:8546")
        if err != nil {
                log.Fatal(err)
        }

        _ = client // we'll be using this in the 下个章节
        fmt.Println("we have a whisper connection")
}
```
