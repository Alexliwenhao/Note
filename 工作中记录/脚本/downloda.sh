#!/bin/bash

cd `dirname $0`

cd mirrors

rootDir=`pwd`

osArr=(ubuntu)
versionArr=(jammy jammy-backports jammy-proposed jammy-security jammy-updates focal focal-backports focal-proposed focal-security focal-updates)
indexFileArr=(InRelease Release Release.gpg)


for os in ${osArr[@]}
do
 for version in ${versionArr[@]}
 do
 	mkdir -p $rootDir/$os/dists/$version
 	cd $rootDir/$os/dists/$version
  for indexFile in ${indexFileArr[@]}
  do
   echo "download https://mirrors.aliyun.com/$os/dists/$version/$indexFile"
   curl --fail https://mirrors.aliyun.com/$os/dists/$version/$indexFile -o $indexFile
  done
 done
done