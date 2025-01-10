# **Swarm**

Swarm 是以太坊的去中心化和分布式的存储解决方案，与 IPFS 类似。 Swarm 是一种点对点数据共享网络，其中文件通过其内容的哈希来寻址。与 Bittorrent 类似，可以同时从多个节点获取数据，只要单个节点承载分发数据，它就可以随处被访问。这种方法可以在不必依靠托管任何类型服务器的情况下分发数据 - 数据可访问性与位置无关。可以激励网络中的其他节点自己复制和存储数据，从而在原节点未连接到网络时避免了对托管服务的依赖。

Swarm 的激励机制 Swap（Swarm Accounting Protocol）是一种协议，通过该协议，Swarm 网络中的个体可以跟踪传送和接收的数据块，以及由此产生相应的（微）付款。 SWAP 本身可以在更广泛的背景下运行，但它通常表现为适用于点对点之间成对会计的通用微支付方案。虽然设计通用，但它的第一个用途是将带宽计算作为 Swarm 去中心化的点对点存储网络中数据传输的激励的一部分。

要运行 swarm，首先需要安装 `geth` 和 `bzzd`，这是 swarm 背景进程。

```
go get -d github.com/ethereum/go-ethereum
go install github.com/ethereum/go-ethereum/cmd/geth
go install github.com/ethereum/go-ethereum/cmd/swarm
```

然后我们将生成一个新的 geth 帐户。

```
$ geth account new

Your new account is locked with a password. Please give a password. Do not forget this password.
Passphrase:
Repeat passphrase:
Address: {970ef9790b54425bea2c02e25cab01e48cf92573}
```

将环境变量 `BZZKEY` 导出，并设定为我们刚刚生成的 geth 帐户地址。

```
export BZZKEY=970ef9790b54425bea2c02e25cab01e48cf92573
```

然后使用设定的帐户运行 swarm，并作为我们的 swarm 帐户。 默认情况下，Swarm 将在端口“8500”上运行。

```
$ swarm --bzzaccount $BZZKEY
Unlocking swarm account 0x970EF9790B54425BEA2C02e25cAb01E48CF92573 [1/3]
Passphrase:
WARN [06-12|13:11:41] Starting Swarm service
```

现在 swarm 进程已经可以运行了，那么我们会在下个章节学习如何上传文件。

### **完整代码**

Commands

```
go get -d github.com/ethereum/go-ethereum
go install github.com/ethereum/go-ethereum/cmd/geth
go install github.com/ethereum/go-ethereum/cmd/swarm
geth account new
export BZZKEY=970ef9790b54425bea2c02e25cab01e48cf92573
swarm --bzzaccount $BZZKEY
```
