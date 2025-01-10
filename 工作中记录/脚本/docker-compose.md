```yaml
version: '3'
services:
  nginx:
    restart: always
    image: registry.ghostcloud.cn/amd64/nginx:1.18.0
    volumes:
      - ./conf/nginx/default.conf:/etc/nginx/conf.d/default.conf
    ports:
      - "80:80"
    deploy:
      resources:
        limits:
          cpus: '0.25'
          memory: 50M
    depends_on:
      - devops-ui
      - devops-cloud-gateway
      - user-service
      - common-service
      - issue-service
      - pipeline-service
      - static-analysis-service
      - grafana
  devops-ui:
    image: registry.ghostcloud.cn/devops/devops-cloud-ui:v7.9.2
    restart: always
    expose:
      - "80"
    deploy:
      resources:
        limits:
          memory: 1G
  devops-cloud-gateway:
    image: registry.ghostcloud.cn/devops/devops-cloud-gateway:v7.9.2
    restart: always
    expose:
      - "8090"
    volumes:
      - ./conf/devops-cloud-gateway/bootstrap.yml:/bootstrap.yml
      - ./conf/devops-cloud-gateway/application.yml:/application.yml
    deploy:
      resources:
        limits:
          memory: 1G
    depends_on:
      - mysql
      - consul
  user-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-user-service:v7.9.2
    restart: always
    expose:
      - "8082"
    volumes:
      - ./conf/user-service/bootstrap.yml:/bootstrap.yml
      - ./conf/user-service/application.yml:/application.yml
    depends_on:
      - mysql
      - consul
    deploy:
      resources:
        limits:
          cpus: '1.00'
          memory: 2G
  common-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-common-service:v7.9.2
    restart: always
    expose:
      - "8081"
    volumes:
      - ./conf/common-service/bootstrap.yml:/bootstrap.yml
      - ./conf/common-service/application.yml:/application.yml
    deploy:
      resources:
        limits:
          memory: 2G
    depends_on:
      - mysql
      - consul
  issue-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-issue-service:v7.9.2
    restart: always
    expose:
      - "8098"
    volumes:
      - ./conf/issue-service/bootstrap.yml:/bootstrap.yml
      - ./conf/issue-service/application.yml:/application.yml
    deploy:
      resources:
        limits:
          memory: 2G
    depends_on:
      - mysql
      - consul
  test-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-test-service:v7.9.2
    restart: always
    expose:
      - "8099"
    volumes:
      - ./conf/test-service/bootstrap.yml:/bootstrap.yml
      - ./conf/test-service/application.yml:/application.yml
    deploy:
      resources:
        limits:
          memory: 2G
    depends_on:
      - mysql
      - consul
  pipeline-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-pipeline-service:v7.9.2
    restart: always
    expose:
      - "8084"
    volumes:
      - ./conf/pipeline-service/bootstrap.yml:/bootstrap.yml
      - ./conf/pipeline-service/application.yml:/application.yml
    deploy:
      resources:
        limits:
          memory: 4G
    depends_on:
      - mysql
      - consul
  static-analysis-service:
    image: registry.ghostcloud.cn/devops/devops-cloud-staticanalysis-service:v7.9.2
    restart: always
    expose:
      - "8085"
    volumes:
      - ./conf/static-analysis-service/bootstrap.yml:/root/bootstrap.yml
      - ./conf/static-analysis-service/application.yml:/root/application.yml
      - ./data/jenkins:/var/jenkins_home
      - ./data/Source:/Source
      - ./data/.m2:/root/.m2
    deploy:
      resources:
        limits:
          memory: 4G
    depends_on:
      - mysql
      - consul
    environment:
      MAVEN_OPTS: -Xmx1024m
  fileviewservice:
    image: registry.ghostcloud.cn/devops/kkfileview:4.1.0
    restart: always
    ports:
      - "8012:8012"
    deploy:
      resources:
        limits:
          memory: 4G
    environment:
      MAVEN_OPTS: -Xmx1024m
  redis:
    image: registry.ghostcloud.cn/devops/redis:6.2.7
    container_name: redis
    restart: always
    ports:
      - "6379:6379"
    volumes:
      - ./conf/redis/redis.conf:/redis.conf
    command: [ "redis-server", "/redis.conf" ]
    deploy:
      resources:
        limits:
          memory: 2G
  minio:
    restart: always
    image: registry.ghostcloud.cn/minio/minio:RELEASE.2021-05-26T00-22-46Z
    command: server /data
    environment:
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin
      MINIO_BUCKET: devops
    volumes:
      - ./data/minio:/data
    ports:
      - '9000:9000'
    deploy:
      resources:
        limits:
          memory: 2G
  mysql:
    restart: always
    image: registry.ghostcloud.cn/devops/mysql-amd64:5.7.19
    ports:
       - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: P@ssw0rd
    volumes:
      - ./conf/mysql/conf.d:/etc/mysql/conf.d
      - ./data/mysql:/var/lib/mysql
      - ./conf/mysql/docker-entrypoint-initdb.d:/docker-entrypoint-initdb.d
    deploy:
      resources:
        limits:
          memory: 2G
  nfs-server:
    restart: always
    image: registry.ghostcloud.cn/devops/nfs-server:latest
    ports:
      - "111:111"
      - "111:111/udp"
      - "2049:2049"
      - "2049:2049/udp"
      - "32765:32765"
      - "32765:32765/udp"
      - "32766:32766"
      - "32766:32766/udp"
      - "32767:32767"
      - "32767:32767/udp"
    privileged: true
    cap_add:
      - SYS_ADMIN
      - SYS_MODULE
    environment:
      NFS_EXPORT_0: '/nfs/prod *(rw,insecure,no_subtree_check,no_root_squash)'
    volumes:
      - /lib/modules:/lib/modules:ro
      - ./data/nfs/prod:/nfs/prod
  jenkins:
    restart: always
    image: registry.ghostcloud.cn/devops/jenkins-amd64:2.346.3-20230609
    user: root
    environment:
      JENKINS_JAVA_OPTS: -Dhudson.security.csrf.GlobalCrumbIssuerConfiguration.DISABLE_CSRF_PROTECTION=true
    ports:
      - '8080:8080'
      - '50000:50000'
    volumes:
      - ./data/jenkins:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
      - ./data/nfs/prod:/nfs
      - ./data/.m2:/root/.m2
    deploy:
      resources:
        limits:
          memory: 6G
  gitea:
    restart: always
    image: registry.ghostcloud.cn/gitea/gitea:1.16.9-linux-amd64
    environment:
      # 允许webhook
      GITEA__webhook__ALLOWED_HOST_LIST: '*'
    ports:
      - '3001:3000'
    volumes:
      - ./data/gitea:/data
      # 注意以下挂载依赖于宿主机的文件, 注意配置好宿主机的时区
      - /etc/timezone:/etc/timezone:ro
      - /etc/localtime:/etc/localtime:ro
    deploy:
      resources:
        limits:
          memory: 4G
#  gitlab:
#    image: gitlab/gitlab-ce:13.10.0-ce.0
#    restart: always
#    hostname: '10.0.2.174'
#    environment:
#      TZ: 'Asia/Shanghai'
#      GITLAB_OMNIBUS_CONFIG: |
#        external_url 'http://10.0.2.174:9001'
#        gitlab_rails['gitlab_shell_ssh_port'] = 1022
#        unicorn['port'] = 8888
#        nginx['listen_port'] = 9001
#    ports:
#      - '9001:9001'
#      - '443:443'
#      - '1022:22'
#    volumes:
#      - ./gitlab/config:/etc/gitlab
#      - ./gitlab/data:/var/opt/gitlab
#      - ./gitlab/logs:/var/log/gitlab
  elasticsearch:
    restart: always
    image: registry.ghostcloud.cn/elasticsearch/elasticsearch-oss:7.10.2-amd64
    ports:
      - '9200:9200'
    volumes:
      - ./data/elasticsearch:/usr/share/elasticsearch/data
      - ./conf/elasticsearch/sysctl.conf:/etc/sysctl.conf
    environment:
      discovery.type: single-node
    deploy:
      resources:
        limits:
          memory: 2G
  grafana:
    restart: always
    ports:
      - '3000:3000'
    image: registry.cluster.local/grafana/grafana:7.4.2v10
    volumes:
      - ./data/grafana:/var/lib/grafana
      - ./conf/grafana/defaults.ini:/usr/share/grafana/conf/defaults.ini
    deploy:
      resources:
        limits:
          memory: 2G
  consul:
    restart: always
    ports:
      - '8500:8500'
    image: registry.ghostcloud.cn/devops/consul:1.4.5
    volumes:
      - ./conf/consul/consul-acl.json:/consul/config/consul-acl.json
      - ./data/consul:/consul/data
      - ./data/consul:/consul/config
    command: agent -server -bind=0.0.0.0 -client=0.0.0.0 -ui -bootstrap-expect=1
    deploy:
      resources:
        limits:
          memory: 1G
  rocketmq:
    restart: always
    ports:
      - '9080:8080'
      - '9876:9876'
      - '10911:10911'
      - '10909:10909'
    image: registry.ghostcloud.cn/devops/rocketmq-4.9.4:1.3
    deploy:
      resources:
        limits:
          memory: 3G
    volumes:
      - ./data/rocketmq:/home/app/data
```