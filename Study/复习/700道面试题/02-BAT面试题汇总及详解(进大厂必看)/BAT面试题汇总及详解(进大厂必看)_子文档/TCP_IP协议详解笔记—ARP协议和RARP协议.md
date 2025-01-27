# TCP/IP协议详解笔记——ARP协议和RARP协议



# ARP：地址解析协议

对于以太网，数据链路层上是根据48bit的以太网地址来确定目的接口，设备驱动程序从不检查IP数据报中的目的IP地址。ARP协议为IP地址到对应的硬件地址之间提供动态映射。

## 工作过程

在以太网（ARP协议只适用于局域网）中，如果本地主机想要向某一个IP地址的主机（路由表中的下一跳路由器或者直连的主机，注意此处IP地址不一定是IP数据报中的目的IP）发包，但是并不知道其硬件地址，此时利用ARP协议提供的机制来获取硬件地址，具体过程如下：

1) 本地主机在局域网中广播ARP请求，ARP请求数据帧中包含目的主机的IP地址。意思是“如果你是这个IP地址的拥有者，请回答你的硬件地址”。

2) 目的主机的ARP层解析这份广播报文，识别出是询问其硬件地址。于是发送ARP应答包，里面包含IP地址及其对应的硬件地址。

3) 本地主机收到ARP应答后，知道了目的地址的硬件地址，之后的数据报就可以传送了。

点对点链路不使用ARP协议。

## 帧格式

 ![o_ARP_RARP帧格式](Study/复习/700道面试题/02-BAT面试题汇总及详解(进大厂必看)/BAT面试题汇总及详解(进大厂必看)_子文档/TCP_IP协议详解笔记—ARP协议和RARP协议.assets/o_ARP_RARP帧格式.png)

- 以太网目的地址：目的主机的硬件地址。目的地址全为1的特殊地址是广播地址。
- 以太网源地址：源主机的硬件地址。
- 帧类型：对于ARP协议，该字段为0x0806。对于RARP协议，该字段为0x8035。
- 硬件类型：表示硬件地址的类型。值为1时表示以太网地址。也就是说ARP协议不仅仅应用于以太网协议，还可以支持别的链路层协议。
- 协议类型：表示要映射的协议地址类型。值为0x0800时表示IP协议。
- 硬件地址长度：与硬件类型对应的硬件地址的长度，以字节为单位。如果是以太网，则是6字节（MAC长度）。
- 协议地址长度：与协议类型对应的协议地址长度，以字节为单位。如果是IP协议，则是4字节（IP地址长度）。
- 操作类型（op）：四中操作类型。ARP请求（1），ARP应答（2），RARP请求（3），RARP应答（4）。
- 发送端硬件地址：如果是以太网，则是源主机以太网地址，此处和以太网头中的源地址对应。
- 发送端协议地址：如果是IP协议，则表示源主机的IP地址。
- 目的端硬件地址：如果是以太网，则是目的以太网地址，和以太网头中的目的地址对应。
- 目的端协议地址：如果是IP协议，则表示源主机要请求硬件地址的IP地址。
- 对应ARP请求包来说，目的端的硬件地址字段无须填充，其他字段都需要填充。对于ARP回复包来说，所有字段都需要填充。

APR请求包是广播的，但是ARP应答帧是单播的。

以太网数据报最小长度是60字节（14字节的以太网头，不包含4字节的FCS），ARP数据包长度为42字节（14字节的以太网头和28字节的ARP数据），需要加入填充字符到以太网最小长度要求：60字节。

## ARP高速缓存

每个主机都有一个ARP高速缓存表，这样避免每次发包时都需要发送ARP请求来获取硬件地址。默认老化时间是20分钟。利用arp -a命令可以查看显示系统中高速缓存的内容。

Windows下“arp -d”命令可以清除arp高速缓存表。

有时候需要手动清除arp缓存，曾经就是因为arp缓存没有做清理，导致迷惑了很久。遇到的问题：

1) 制作了一个写路由器MAC地址的工具，每次写完MAC地址，重启路由器，会发现无法telnet登陆路由器。IP地址没变，但是MAC地址更改了，而ARP缓存表中IP地址映射的仍然是旧的MAC地址。

2) 类似的问题，有两个路由器具有相同的IP地址。先连接一个路由器，登陆成功后，再去连接另一台路由器，却发现登陆不了。

## ARP代理

如果ARP请求时从一个网络的主机发往另一个网络上的主机，那么连接这两个网络的路由器可以回答该请求，这个过程称作委托ARP或者ARP代理。这样可以欺骗发起ARP请求的发送端，使它误以为路由器就是目的主机。

# RARP：逆地址解析协议

将局域网中某个主机的物理地址转换为IP地址，比如局域网中有一台主机只知道物理地址而不知道IP地址，那么可以通过RARP协议发出征求自身IP地址的广播请求，然后由RARP服务器负责回答。RARP协议广泛应用于无盘工作站引导时获取IP地址。

RARP允许局域网的物理机器从网管服务器ARP表或者缓存上请求其IP地址。

## 帧格式

帧格式同ARP协议，帧类型字段和操作类型不同，具体见ARP帧格式描述。

## 工作原理

\1. 主机发送一个本地的RARP广播，在此广播包中，声明自己的MAC地址并且请求任何收到此请求的RARP服务器分配一个IP地址。

\2. 本地网段上的RARP服务器收到此请求后，检查其RARP列表，查找该MAC地址对应的IP地址。

\3. 如果存在，RARP服务器就给源主机发送一个响应数据包并将此IP地址提供给对方主机使用。

\4. 如果不存在，RARP服务器对此不做任何的响应。

\5. 源主机收到从RARP服务器的响应信息，就利用得到的IP地址进行通讯；如果一直没有收到RARP服务器的响应信息，表示初始化失败。