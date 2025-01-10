基础要求是系统中必须要有kvm做出基础支撑，搭建基础环境时候需要考虑进去
# 一、加载基础镜像
```
docker load -i ubuntu+jammy4.0.tar
```
# 二、启动镜像
```
docker run --privileged --network=compose_default  --device=/dev/kvm  -v /root/alex:/tmp/qemu/ -itd --name qemuUbuntu2   ubuntu:jammy4.0
```
--network需要查看devops服务的docker-compose启动之后采用那个网络
```
docker network ls
```
--network=compose_default需要修改

挂载目录采用我们基础配置下的data目录
/root/alex:/tmp/qemu/修改

# 三、修改容器内kvm为宿主机同groupID，这样才有权限使用挂载的驱动
```
 //修改为同一个kvm组才能使用宿主机驱动
 cat /etc/group | fgrep kvm
 要和宿主机一致
 groupmod -g GID kvm
```

# 四、启动虚拟机
## 1. 从0搭建新机器
```
 //创建磁盘
 qemu-img create -f qcow2 ./win10.img 128G
```

```
//安装系统
qemu-system -nographic -enable-kvm -cpu host ./win10.img -cdr   om ./zh-cn_windows_10_consumer_editions_version_22h2_updated_jan_2023_x64_dvd_1973df35.iso    -m 8192 -smp 8 -boot d -vnc :10
```

端口转发容器vnc接口方便vnc连接安装系统（注意端口占用5910端口占用则可以修改vnc参数vnc参数意义指定端口为5900+vnc入参）
```
//端口转发让局域网内通过宿主机vnc远程访问到虚拟机
 iptables -t nat -A PREROUTING -p tcp --dport 5910 -j DNAT --to-destination 172.17.0.2:5910
```

执行资料包中（br.sh脚本创建网桥）
chmod +777 br.sh
```
#!/bin/sh

default_interface=$(ip route | awk '/default/ {print $5}')
echo "default_interface:$default_interface"
ip_address=$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1')
echo "ip_address:$ip_address"
gateway_address=$(route | awk '/default/ {print $2}')
echo "gateway_address:$gateway_address"

if [ -z "$default_interface" ] || [ -z "$ip_address" ] || [ -z "$gateway_address" ]; then
  echo "get info has null"
  exit 1
fi
brctl addbr br0

brctl addif br0 $default_interface


ip addr add $ip_address/16 dev br0

ip addr flush dev $default_interface

ifconfig br0 up

route add default gw $gateway_address

```


```
 //启动虚拟机
 qemu-system -nographic -enable-kvm -cpu host ./win10.img -m 8192 -smp 8 -boot c -vnc :10 -net nic,macaddr=D8:5E:D3:54:A9:C9 -net tap,ifname=tap1,script=/etc/qemu-ifup,downscript=no
 
```
****macaddr=D8:5E:D3:54:A9:C9可以要，也可以用默认，如果工具需要绑定mac地址，需要作为参数传入****
***注意：进去之后配置虚拟机IP地址为容器同网段地址信息***

开放虚拟机3389端口映射到宿主机，方便远程连接
```
//端口转发让局域网内通过宿主机直接远程访问到虚拟机
 iptables -t nat -A PREROUTING -p tcp --dport 3389 -j DNAT --to-destination 虚拟机IP地址:3389
```

# 2. 有硬盘直接启动

端口转发容器vnc接口方便vnc连接安装系统（注意端口占用5910端口占用则可以修改vnc参数vnc参数意义指定端口为5900+vnc入参）
```
//端口转发让局域网内通过宿主机vnc远程访问到虚拟机
 iptables -t nat -A PREROUTING -p tcp --dport 5910 -j DNAT --to-destination 172.17.0.2:5910
```

执行资料包中（br.sh脚本创建网桥）
```
#!/bin/sh

default_interface=$(ip route | awk '/default/ {print $5}')
echo "default_interface:$default_interface"
ip_address=$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1')
echo "ip_address:$ip_address"
gateway_address=$(route | awk '/default/ {print $2}')
echo "gateway_address:$gateway_address"

if [ -z "$default_interface" ] || [ -z "$ip_address" ] || [ -z "$gateway_address" ]; then
  echo "get info has null"
  exit 1
fi
brctl addbr br0

brctl addif br0 $default_interface


ip addr add $ip_address/16 dev br0

ip addr flush dev $default_interface

ifconfig br0 up

route add default gw $gateway_address

```


```
 //启动虚拟机
 qemu-system -nographic -enable-kvm -cpu host ./win10.img -m 8192 -smp 8 -boot c -vnc :10 -net nic,macaddr=D8:5E:D3:54:A9:C9 -net tap,ifname=tap1,script=/etc/qemu-ifup,downscript=no
 
```
****macaddr=D8:5E:D3:54:A9:C9可以要，也可以用默认，如果工具需要绑定mac地址，需要作为参数传入****
***注意：进去之后配置虚拟机IP地址为容器同网段地址信息***

开放虚拟机3389端口映射到宿主机，方便远程连接
```
//端口转发让局域网内通过宿主机直接远程访问到虚拟机
 iptables -t nat -A PREROUTING -p tcp --dport 3389 -j DNAT --to-destination 虚拟机IP地址:3389
```


附加：查看信息
```
映射关系建立错误时候将 iptables -t nat -A PREROUTING -p tcp --dport 3389 -j DNAT --to-destination 虚拟机IP地址:3389中参数——A修改为-D则删除

//查看虚拟机是否占用
lsof win64.img

//查看情况 例子
root@751dd4160a93:/# ls /sys/devices/virtual/net/
br0  eth0  lo  tap1  tunl0
root@751dd4160a93:/# brctl show
bridge name     bridge id               STP enabled     interfaces
br0             8000.0242ac110002       yes             eth0
                                                        tap1
```

