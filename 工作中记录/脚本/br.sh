#!/bin/sh
sudo brctl addif br0 ens33

sudo ip addr add 192.168.81.132/24 dev br0

sudo ip addr flush dev eth0

sudo ifconfig br0 up