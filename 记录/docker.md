停止所有正在运行的Docker服务，可以使用以下命令：

```javascript
docker stop $(docker ps -aq)
```

这个命令会先执行`docker ps -aq`来列出所有正在运行的容器的ID，然后将这些ID传递给`docker stop`命令，以停止这些容器。

如果你想要停止并删除所有正在运行的Docker服务，可以使用以下命令：

```javascript
docker stop $(docker ps -aq) && docker rm $(docker ps -aq)
```

要删除所有的Docker镜像，可以使用以下命令：

```javascript
docker rmi $(docker images -aq)
```

这个命令会先执行`docker images -aq`来列出所有镜像的ID，然后将这些ID传递给`docker rmi`命令，以删除这些镜像。

注意，这个命令将删除所有的镜像，包括那些你可能希望保留的镜像。因此，在使用该命令之前，请确保你已经备份了所有重要的镜像。

如果你只想删除未被使用的镜像，可以使用以下命令：

```css
docker image prune -a
```

这个命令会删除未被使用的镜像，包括那些没有被任何容器使用的镜像。如果你只想删除没有标签的镜像，可以使用以下命令：

```python
docker image prune -a --filter "dangling=true"
```

这个命令会删除所有没有标签的镜像。